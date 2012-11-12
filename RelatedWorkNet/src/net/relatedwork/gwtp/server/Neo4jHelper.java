package net.relatedwork.gwtp.server;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

public class Neo4jHelper {
	
	
	public static EmbeddedGraphDatabase getGraphDatabase(ServletContext servletContext){
		EmbeddedGraphDatabase graphDB;
		graphDB = (EmbeddedGraphDatabase)servletContext.getAttribute("graphDB");
		if (graphDB==null){
			graphDB = new EmbeddedGraphDatabase("/var/lib/datasets/rawdata/relatedwork/db_folder");
			servletContext.setAttribute("graphDB", graphDB);
		}
		return graphDB;
	}
	
	public static EmbeddedReadOnlyGraphDatabase getReadOnlyGraphDatabase(ServletContext servletContext){
		EmbeddedReadOnlyGraphDatabase graphDB;
		graphDB = (EmbeddedReadOnlyGraphDatabase)servletContext.getAttribute("graphDB");
		if (graphDB==null){
			graphDB = new EmbeddedReadOnlyGraphDatabase("/var/lib/datasets/rawdata/relatedwork/db_folder");
			servletContext.setAttribute("readOnlyGraphDB", graphDB);
		}
		return graphDB;		
	}
	
	public static Index<Node> getSearchIndex(ServletContext servletContext){
		Index<Node> searchIndex;
		searchIndex = (Index<Node>)servletContext.getAttribute("searchIndex");
		if (searchIndex==null){
			EmbeddedGraphDatabase graphDB = getGraphDatabase(servletContext);
			searchIndex = graphDB.index().forNodes("prsearch_idx");
			((LuceneIndex<Node>) searchIndex).setCacheCapacity("title", 300000);
			servletContext.setAttribute("searchIndex",searchIndex);
		}
		return searchIndex;
	}
	
}
