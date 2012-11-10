package net.relatedwork.gwtp.server;

import javax.servlet.ServletContext;

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
}
