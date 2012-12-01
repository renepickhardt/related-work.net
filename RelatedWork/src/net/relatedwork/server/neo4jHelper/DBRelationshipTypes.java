package net.relatedwork.server.neo4jHelper;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.RelationshipType;

public class DBRelationshipTypes {

	public static DynamicRelationshipType CITES = DynamicRelationshipType.withName("ref");
	public static DynamicRelationshipType WRITTEN_BY = DynamicRelationshipType.withName("author");
	
	public static DynamicRelationshipType CO_CITATION_SCORE = DynamicRelationshipType.withName("RW:DM:CO_CITATION_SCORE");
	public static DynamicRelationshipType CO_AUTHOR_COUNT = DynamicRelationshipType.withName("rw:dm:CoAuthorCount");
	public static final RelationshipType CITES_AUTHOR = DynamicRelationshipType.withName("rw:dm:citesAuthor");
	
	public static final RelationshipType SIM_AUTHOR = DynamicRelationshipType.withName("rw:dm:simAuthor");
	
	// by Heinrich
	
	public static DynamicRelationshipType TYPE = DynamicRelationshipType.withName("type");
	
}
