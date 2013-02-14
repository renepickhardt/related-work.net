package net.relatedwork.server.datamining;

import org.neo4j.kernel.EmbeddedGraphDatabase;

/**
 * base class for data mining tasks on a graph data base.
 * @author rpickhardt
 *
 */

public class Calculator {

	static protected EmbeddedGraphDatabase graphDB;

	public Calculator() {
		super();
	}

	public EmbeddedGraphDatabase getGraphDB() {
		return graphDB;
	}

	public void setGraphDB(EmbeddedGraphDatabase graphDB) {
		this.graphDB = graphDB;
	}


}