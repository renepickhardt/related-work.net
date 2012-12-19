package net.relatedwork.server.neo4jHelper;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

public class DBRelationshipTypes {

	public static DynamicRelationshipType CITES = withName("ref");
	public static DynamicRelationshipType WRITTEN_BY = withName("author");
	
	public static DynamicRelationshipType CO_CITATION_SCORE = withName("RW:DM:CO_CITATION_SCORE");
	public static DynamicRelationshipType CO_AUTHOR_COUNT = withName("rw:dm:CoAuthorCount");
	public static final RelationshipType CITES_AUTHOR = withName("rw:dm:citesAuthor");
	
	public static final RelationshipType SIM_AUTHOR = withName("rw:dm:simAuthor");
	
	// by Heinrich
	
	public static DynamicRelationshipType TYPE = withName("type");

    // Discussions

    public static final RelationshipType COMMENT_AUTHOR = withName("rw:comment:author");

    // These relations have names of the enum.
    public static enum Discussions implements RelationshipType {
        COMMENT_QUESTION,
        COMMENT_REVIEW,
        COMMENT_SUMMARY,
        COMMENT_GENERAL,
        COMMENT_REPLY;
    }

    public static enum CommentVoter implements RelationshipType {
        COMMENT_UP_VOTE,
        COMMENT_DOWN_VOTE;
    }
}
