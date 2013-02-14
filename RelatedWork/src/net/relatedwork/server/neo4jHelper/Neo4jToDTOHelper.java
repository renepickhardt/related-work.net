package net.relatedwork.server.neo4jHelper;

import net.relatedwork.shared.dto.Paper;
import org.neo4j.graphdb.Node;

public class Neo4jToDTOHelper {
	public static Paper paperFromNode(Node n){
		Double pageRank = (Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
		Integer score = (int)(pageRank*1000.);
		return paperFromNode(n, score);
	}
	
	public static Paper paperFromNode(Node n, Integer score){
		String name = (String)n.getProperty(DBNodeProperties.PAPER_TITLE);
		String uri = (String)n.getProperty(DBNodeProperties.URI);
		String source = (String)n.getProperty(DBNodeProperties.PAPER_SOURCE_URI);
		return new Paper(name, uri, source, score);
	}
}
