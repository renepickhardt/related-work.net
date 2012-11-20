package net.relatedwork.server.executables;

import org.neo4j.kernel.EmbeddedGraphDatabase;

import net.relatedwork.server.datamining.BuildIndices;
import net.relatedwork.server.datamining.CalculatePageRank;
import net.relatedwork.server.datamining.CoAuthorCalculator;
import net.relatedwork.server.datamining.SimilarPaperCalculator;
import net.relatedwork.server.utils.Config;

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
		
		//CoAuthorCalculator cac = new CoAuthorCalculator(graphDB);
		//cac.calculateTopKCoAuthors(500,10);
		//cac.calculateTopKLikesToCiteAuthors(500, 12);

		//SimilarPaperCalculator spc = new SimilarPaperCalculator(graphDB);
		//spc.startTopKCalculation(500, 15);
		//spc.similarTopKAuthors(500, 15);
		
//		CalculatePageRank cpr = new CalculatePageRank(graphDB);
//		cpr.dcalculatePageRank(0.85, 3);
		
//		BuildIndices bi = new BuildIndices();
//		bi.setGraphDB(graphDB);
//		bi.buildSearchIndex("prsearch_idx");
		
		graphDB.shutdown();
	}

}
