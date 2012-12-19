package net.relatedwork.server.neo4jHelper;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Singleton;
import net.relatedwork.shared.dto.Comments;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

import static net.relatedwork.shared.dto.Comments.CommentType;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
@Singleton
public class DiscussionTypeMapper {
    private static BiMap<String, Comments.CommentType> commentTypeMap = HashBiMap.create(
        ImmutableMap.<String, Comments.CommentType>builder()
                .put(DBRelationshipTypes.COMMENT_QUESTION.name(), CommentType.Question)
                .put(DBRelationshipTypes.COMMENT_REVIEW.name(), CommentType.Review)
                .put(DBRelationshipTypes.COMMENT_SUMMARY.name(), CommentType.Summary)
                .put(DBRelationshipTypes.COMMENT_GENERAL.name(), CommentType.GeneralDiscussion)
        .build());

    public CommentType fromDBRelationship(RelationshipType type) {
        if (type.name().equals(DBRelationshipTypes.COMMENT_REPLY.name())) return null;
        else return commentTypeMap.get(type.name());
    }

    public RelationshipType fromCommentType(CommentType type) {
        if (type == null) return DBRelationshipTypes.COMMENT_REPLY;
        else return DynamicRelationshipType.withName(commentTypeMap.inverse().get(type));
    }
}
