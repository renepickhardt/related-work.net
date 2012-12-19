package net.relatedwork.server.neo4jHelper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import net.relatedwork.shared.dto.Comments;

import static net.relatedwork.server.neo4jHelper.DBRelationshipTypes.Discussions;
import static net.relatedwork.shared.dto.Comments.CommentType;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
@Singleton
public class DiscussionTypeMapper {
    private static BiMap<DBRelationshipTypes.Discussions, Comments.CommentType> commentTypeMap = HashBiMap.create(
        ImmutableMap.<DBRelationshipTypes.Discussions, Comments.CommentType>builder()
                .put(Discussions.COMMENT_QUESTION, CommentType.Question)
                .put(Discussions.COMMENT_REVIEW, CommentType.Review)
                .put(Discussions.COMMENT_SUMMARY, CommentType.Summary)
                .put(Discussions.COMMENT_GENERAL, CommentType.GeneralDiscussion)
        .build());

    public CommentType fromDBRelationship(Discussions type) {
        if (type == Discussions.COMMENT_REPLY) return null;
        else return commentTypeMap.get(type);
    }

    public Discussions fromCommentType(CommentType type) {
        if (type == null) return Discussions.COMMENT_REPLY;
        else return commentTypeMap.inverse().get(type);
    }
}
