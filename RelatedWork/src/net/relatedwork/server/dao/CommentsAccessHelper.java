package net.relatedwork.server.dao;

import com.google.common.collect.ImmutableList;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipTypes;
import net.relatedwork.server.neo4jHelper.DiscussionTypeMapper;
import net.relatedwork.server.userHelper.UserInformation;
import net.relatedwork.server.utils.MD5Util;
import net.relatedwork.shared.dto.Comments;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A database access helper class for comments.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
@Singleton
public class CommentsAccessHelper {
    public static final int INITIAL_COMMENT_VOTES = 0;

    private final Index<Node> uriIndex;
    private final EmbeddedGraphDatabase database;

    @Inject DiscussionTypeMapper discussionTypeMapper;
    @Inject AuthorAccessHandler authorAccessHandler;
    @Inject UserAccessHandler userAccessHandler;
    @Inject Provider<UserInformation> userInformationProvider;

    private TraversalDescription relatedCommentsTraversal;

    @Inject
    public CommentsAccessHelper(ServletContext servletContext) {
        uriIndex = ContextHelper.getUriIndex(servletContext);
        database = ContextHelper.getGraphDatabase(servletContext);
    }

    @Inject
    void setUpTraversals() {
        relatedCommentsTraversal = Traversal.description()
                .breadthFirst()
                .relationships(DBRelationshipTypes.COMMENT_QUESTION)
                .relationships(DBRelationshipTypes.COMMENT_SUMMARY)
                .relationships(DBRelationshipTypes.COMMENT_GENERAL)
                .relationships(DBRelationshipTypes.COMMENT_REPLY)
                .relationships(DBRelationshipTypes.COMMENT_REVIEW)
                .evaluator(Evaluators.toDepth(2))
                .uniqueness(Uniqueness.NODE_GLOBAL);
    }

    /** Get related comments of a Author/Paper */
    public ImmutableList<Comments> getRelatedComments(String targetUri) throws ActionException {
        Node target = getTargetNode(targetUri, false);
        ImmutableList.Builder<Comments> comments = ImmutableList.builder();
        for (Path path: relatedCommentsTraversal.traverse(target)) {
            if (path.length() == 0) continue;
            Node commentNode = path.endNode();
            RelationshipType relationshipType = path.lastRelationship().getType();
            ImmutableList<Node> nodes = ImmutableList.copyOf(path.nodes());
            Node fromNode = nodes.get(nodes.size() - 2);

            comments.add(getCommentObject(commentNode, relationshipType, fromNode));
        }
        return comments.build();
    }

    public Node getTargetNode(String targetUri, boolean targetIsComment) throws ActionException {
        IndexHits<Node> targetNodeHit;
        if (targetIsComment) {
            targetNodeHit = uriIndex.get(DBNodeProperties.COMMENT_URI, targetUri);
        } else {
            targetNodeHit = uriIndex.get(DBNodeProperties.URI, targetUri);
        }

        if (!targetNodeHit.hasNext()) {
            throw new ActionException(
                    "Target node does not exists: " + targetUri +
                    " targetIsComment: " + targetIsComment);
        }
        return targetNodeHit.getSingle();
    }

    /**
     * Add a comment to the database. Will set date and uri of comment.
     */
    public void addComment(Comments comment, final Node targetNode) throws ActionException {
        comment.setDate(now());
        comment.setUri(generateCommentUri(comment));

        Transaction tx = database.beginTx();
        try {
            Node newNode = database.createNode();

            Node userNode = userAccessHandler.getUserNodeFromEmail(comment.getAuthor().getEmailAddress());
            newNode.createRelationshipTo(userNode, DBRelationshipTypes.COMMENT_AUTHOR);
            newNode.setProperty(DBNodeProperties.COMMENT_BODY, comment.getComment());
            newNode.setProperty(DBNodeProperties.COMMENT_DATE, comment.getDate());
            newNode.setProperty(DBNodeProperties.COMMENT_URI, comment.getUri());
            newNode.setProperty(DBNodeProperties.COMMENT_VOTES, INITIAL_COMMENT_VOTES);

            RelationshipType commentRelation = discussionTypeMapper.fromCommentType(comment.getType());
            targetNode.createRelationshipTo(newNode, commentRelation);

            uriIndex.add(newNode, DBNodeProperties.COMMENT_URI, comment.getUri());

            tx.success();
        } catch (Exception e) {
            throw new ActionException("Error occurred when saving comment", e);
        } finally {
            tx.finish();
        }
    }

    /** Vote for a comment.
     *
     * @return the new votes
     */
    public int voteComment(String userUri, String commentUri, boolean upVote) throws ActionException {
        int newVote;
        Node node = getTargetNode(commentUri, true);
        Node userNode = authorAccessHandler.authorNodeFromUri(userUri);

        Transaction tx = database.beginTx();
        try {
            int oldVotes = (Integer)node.getProperty(DBNodeProperties.COMMENT_VOTES, INITIAL_COMMENT_VOTES);
            newVote = upVote ? oldVotes + 1 : oldVotes - 1;
            node.setProperty(DBNodeProperties.COMMENT_VOTES, newVote);
            node.createRelationshipTo(userNode,
                    upVote ? DBRelationshipTypes.COMMENT_UP_VOTE : DBRelationshipTypes.COMMENT_DOWN_VOTE);
            tx.success();
        } catch (Exception e) {
            throw new ActionException("Error occurred when voting comment", e);
        } finally {
            tx.finish();
        }
        return newVote;
    }

    private Comments getCommentObject(Node commentNode, RelationshipType relationshipType, Node fromNode) {
        Node authorNode = null;
        for (Relationship rel: commentNode.getRelationships(DBRelationshipTypes.COMMENT_AUTHOR)) {
            authorNode = rel.getOtherNode(commentNode);
        }

        SessionInformation author;
        if (authorNode == null) {
            // assume a default author if not found
            author = new SessionInformation();
        } else {
            author = fromUserNode(authorNode);
        }

        String targetUri;
        if (fromNode.hasProperty(DBNodeProperties.COMMENT_URI)) {
            // fromNode is a comment
            targetUri = (String) fromNode.getProperty(DBNodeProperties.COMMENT_URI);
        } else {
            // fromNode is a Author/Paper node
            targetUri = (String) fromNode.getProperty(DBNodeProperties.URI);
        }

        Comments comment = new Comments();
        comment.setUri((String) commentNode.getProperty(DBNodeProperties.COMMENT_URI));
        comment.setAuthor(author);
        comment.setComment((String) commentNode.getProperty(DBNodeProperties.COMMENT_BODY));
        comment.setDate((String) commentNode.getProperty(DBNodeProperties.COMMENT_DATE));
        comment.setVoting((Integer) commentNode.getProperty(DBNodeProperties.COMMENT_VOTES));
        comment.setType(discussionTypeMapper.fromDBRelationship(relationshipType));
        comment.setTargetUri(targetUri);

        System.out.println("Got comment of type " + comment.getType() + ": " + comment.getComment());
        return comment;
    }

    // TODO: server code should not depend on client code(SessionInformation).
    // Besides, user info should be a dedicated kind of object.
    private SessionInformation fromUserNode(Node user) {
        UserInformation userInformation = userInformationProvider.get();
        userInformation.loadFromNode(user);
        SessionInformation sessionInformation = new SessionInformation();
        sessionInformation.setUsername(userInformation.username);
        sessionInformation.setEmailAddress(userInformation.email);
        return sessionInformation;
    }

    private static String now() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return format.format(new Date());
    }

    private static String generateCommentUri(Comments comment) {
        long timeSince = new Date().getTime();
        String timestamp = Long.toHexString(timeSince);
        String content = MD5Util.md5Hex(comment.getAuthor().getEmailAddress() + comment.getComment());
        return String.format("%s-%s", timestamp, content.substring(0, 32));
    }
}
