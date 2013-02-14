package net.relatedwork.server.neo4jHelper;

import org.neo4j.graphdb.Node;

public class NodeType {
	
	public static boolean isAuthorNode(Node n){
		return n.hasProperty(DBNodeProperties.AUTHOR_NAME);
	}

	public static boolean isPaperNode(Node n){
		return n.hasProperty(DBNodeProperties.PAPER_TITLE);
	}
}
