package net.relatedwork.server.neo4jHelper;

import net.relatedwork.shared.dto.Paper;

import org.neo4j.graphdb.Node;

public class Neo4jToDTOHelper {
	
	public static Paper generatePaperFromNode(Node n){
		String name = (String)n.getProperty(DBNodeProperties.PAPER_TITLE);
		String source = (String)n.getProperty(DBNodeProperties.PAPER_SOURCE_URI);
		String date = (String)n.getProperty(DBNodeProperties.PAPER_DATE);
		Integer citeCnt = (Integer)n.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
		return new Paper(name,name, source, date, citeCnt);
	}

}
