package net.relatedwork.server.neo4jHelper;

public class DBNodeProperties {
	// All nodes
	public static String LABEL = "label";
	public static String PAGE_RANK_VALUE = "pageRankValue";
	
	// Paper nodes
	public static String PAPER_TITLE = "title";
	public static String PAPER_CITATION_COUNT = "rw:citationCount";
	public static String PAPER_DATE = "date";
	public static String PAPER_ID = "source_id";
	public static String PAPER_SOURCE_URI = "source_url";

	// Author nodes
	public static String AUTHOR_NAME = "name";
	
	// Propery values for TYPE master nodes
	public static String PAPER_LABEL_VALUE = "PAPER";
	public static String AUTHOR_LABEL_VALUE = "AUTHOR";	
}
