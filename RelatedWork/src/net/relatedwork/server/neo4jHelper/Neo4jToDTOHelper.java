package net.relatedwork.server.neo4jHelper;

import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.Paper;

import org.neo4j.graphdb.Node;

public class Neo4jToDTOHelper {
	public static Paper paperFromNode(Node n){
		String name = (String)n.getProperty(DBNodeProperties.PAPER_TITLE);
		String uri = (String)n.getProperty(DBNodeProperties.URI);
		String source = (String)n.getProperty(DBNodeProperties.PAPER_SOURCE_URI);
		Integer citeCnt = (Integer)n.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
		return new Paper(name, uri, source, citeCnt);
	}
	
	public static Author authorFromNode(Node n){
		String name = (String)n.getProperty(DBNodeProperties.AUTHOR_NAME);
		String uri  = (String)n.getProperty(DBNodeProperties.URI);
		Double pageRank = (Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
		return new Author(name, uri, (int)(pageRank*1000.));
	}
	
}
