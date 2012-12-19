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
    public static final RelationshipType COMMENT_QUESTION = withName("rw:comment:question");
    public static final RelationshipType COMMENT_SUMMARY = withName("rw:comment:summary");
    public static final RelationshipType COMMENT_GENERAL = withName("rw:comment:general");
    public static final RelationshipType COMMENT_REPLY = withName("rw:comment:reply");
    public static final RelationshipType COMMENT_REVIEW = withName("rw:comment:review");

    public static final RelationshipType COMMENT_UP_VOTE = withName("rw:comment:upVote");
    public static final RelationshipType COMMENT_DOWN_VOTE = withName("rw:comment:downVote");
}
