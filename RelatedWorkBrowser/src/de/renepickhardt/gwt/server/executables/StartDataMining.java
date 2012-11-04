package de.renepickhardt.gwt.server.executables;

import org.neo4j.kernel.EmbeddedGraphDatabase;

import de.renepickhardt.gwt.server.datamining.CoAuthorCalculator;
import de.renepickhardt.gwt.server.datamining.SimilarPaperCalculator;
import de.renepickhardt.gwt.server.utils.Config;

/**
 * Class to start the data mining process of related-work.net data base.
 * 
 * if data mining results are supposed to be inserted as a bulk process this class can trigger 
 * everything
 * 
 * TODO: methods have to be generalized in the way data mining can be called locally for single data entries and nodes.
 * 
 * @author rpickhardt
 *
 */

public class StartDataMining {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		EmbeddedGraphDatabase graphDB = new EmbeddedGraphDatabase(Config.get().neo4jDbPath);
		
		CoAuthorCalculator cac = new CoAuthorCalculator(graphDB);
		//cac.calculateTopKCoAuthors(500,10);
		//cac.

		SimilarPaperCalculator spc = new SimilarPaperCalculator(graphDB);
		//spc.startTopKCalculation(500, 15);
		spc.similarTopKAuthors(500, 15);
		
		
		graphDB.shutdown();
	}

}
