package net.relatedwork.server.action;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipTypes;
import net.relatedwork.server.neo4jHelper.DiscussionTypeMapper;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.server.utils.MD5Util;
import net.relatedwork.shared.dto.Comments;
import net.relatedwork.shared.dto.NewCommentAction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import javax.servlet.ServletContext;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The ActionHandler deals with creation of comments.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class NewCommentActionHandler implements ActionHandler<NewCommentAction, Comments> {

    public static final int INITIAL_COMMENT_VOTES = 0;

    private final EmbeddedGraphDatabase database;
    private ServletContext servletContext;
    private final Index<Node> uriIndex;

    @Inject DiscussionTypeMapper discussionTypeMapper;

    @Inject
    public NewCommentActionHandler(ServletContext servletContext) {
        uriIndex = ContextHelper.getUriIndex(servletContext);
        database = ContextHelper.getGraphDatabase(servletContext);
    }

    @Override
    public Comments execute(NewCommentAction newCommentAction, ExecutionContext executionContext) throws ActionException {
        Comments comment = newCommentAction.getComment();
        String targetUri = comment.getTargetUri();

        IOHelper.log(
                String.format("Creating new comment. Target[%s] Comment[%s]", targetUri, comment.getComment()));

        IndexHits<Node> targetNodeHit;
        if (comment.isReply()) {
            targetNodeHit = uriIndex.get(DBNodeProperties.COMMENT_URI, targetUri);
        } else {
            targetNodeHit = uriIndex.get(DBNodeProperties.URI, targetUri);
        }

        if (!targetNodeHit.hasNext()) {
            throw new ActionException("Target node does not exists: " + targetUri);
        }
        Node targetNode = targetNodeHit.getSingle();

        String now = now();
        String commentUri = generateCommentUri(comment);

        Transaction tx = database.beginTx();
        try {
            Node newNode = database.createNode();

            newNode.setProperty(DBNodeProperties.COMMENT_BODY, comment.getComment());
            newNode.setProperty(DBNodeProperties.COMMENT_DATE, now);
            newNode.setProperty(DBNodeProperties.COMMENT_URI, commentUri);
            newNode.setProperty(DBNodeProperties.COMMENT_VOTES, INITIAL_COMMENT_VOTES);

            DBRelationshipTypes.Discussions commentRelation = discussionTypeMapper.fromCommentType(comment.getType());
            targetNode.createRelationshipTo(newNode, commentRelation);

            tx.success();
        } catch (Exception e) {
            throw new ActionException("Error occurred when saving comment", e);
        } finally {
            tx.finish();
        }

        Comments newComment = comment;
        newComment.setComment(comment.getComment());
        newComment.setDate(now);
        newComment.setUri(commentUri);
        return newComment;
    }

    @Override
    public Class<NewCommentAction> getActionType() {
        return NewCommentAction.class;
    }

    @Override
    public void undo(NewCommentAction newCommentAction, Comments comments, ExecutionContext executionContext) throws ActionException {
        throw new UnsupportedOperationException();
    }

    private static String now() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        return format.format(new Date());
    }

    private static String generateCommentUri(Comments comment) {
        long timeSince = new Date().getTime();
        String timestamp = Long.toHexString(timeSince);
        String content = MD5Util.md5Hex(comment.getAuthor().getUri() + comment.getComment());
        return String.format("%s-%s", timestamp, content.substring(0, 32));
    }
}
