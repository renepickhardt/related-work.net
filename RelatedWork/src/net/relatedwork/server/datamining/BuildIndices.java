package net.relatedwork.server.datamining;

import java.util.Random;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.ValueContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.google.gwt.uibinder.elementparsers.DialogBoxParser;

import net.relatedwork.server.executables.CustomTokenAnalyzer;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.IOHelper;

/**
 * build various lucene indices that are usefull for deployement.
 * 
 * @author rpickhardt
 *
 */

public class BuildIndices extends Calculator{
	
	private static Index<Node> searchIndex;
	private static Index<Node> uriIndex;	
	
	private static Random randGen = new Random();
	

	
	public Index<Node> buildSearchIndex(){
		// reset indices
		deleteSearchIndex();
		setSearchIndex();

		deleteUriIndex();
		setUriIndex();

		// build up new indices
		System.out.println("Building up indices");
		
		Transaction transaction = graphDB.beginTx();
		try {
			int nodeCounter = 0;
			for (Node n:graphDB.getAllNodes()) {
				if (NodeType.isPaperNode(n) && n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE)){
					// add paper node to index 
					searchIndex.add(n, "key", (String)n.getProperty("title"));
					searchIndex.add(n, "score", new ValueContext((Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE)).indexNumeric());
					searchIndex.add(n, "type", "paper");
					registerURI(n, "paper:" + (String)n.getProperty("c_authors") + ";" + (String)n.getProperty("title") );
				} else if (NodeType.isAuthorNode(n) && n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE)) {
					// add author node to index
					searchIndex.add(n, "key", (String)n.getProperty("name"));
					searchIndex.add(n, "score", new ValueContext((Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE)).indexNumeric());
					searchIndex.add(n, "type", "author");
					registerURI(n, "author:" + (String)n.getProperty("name") );
				} else {
					System.out.println("Skipped node: "+ n.toString());
					
				}

				if ( nodeCounter++ % 10000 == 9999){
					// New transactioon and print statistics 
					IOHelper.log((nodeCounter-1) + " nodes added to search index.");
					transaction.success();
					transaction.finish();
					transaction = graphDB.beginTx();
					// REMOVE THIS
					break;
				}
			}
			transaction.success();
			
		} catch (Exception e) {
			System.out.println("ERROR ABORTING");
			System.out.println(e.getMessage());
			transaction.failure();
			
		} finally {
			transaction.finish();
			System.out.println("Finished indexing.");
		}

		return searchIndex;
	}
	

	public static String registerURI(Node n, String uri){
		
		// replace sequences of non-alphanumeric characters with '_'
		uri = uri.replaceAll("\\W+"," ").trim().replace(' ','_');
		
		while (true) {
			if(uriIndex.putIfAbsent(n, "uri", uri) == null){
				// if uri new - set into uri field
				n.setProperty(DBNodeProperties.URI, uri);
				break;
			} else {
				// if uri already given add a random number at the end
				uri = uri + String.valueOf(randGen.nextInt(10));
				IOHelper.log("Auto generated URI " + uri);
			}
		}
		
		// System.out.println("registerd uri " + uri);
		
		return uri;
	}
	
	private void setSearchIndex() {
		// Setup new index
		// The custom token analyzer is case independent and removes "," "-" and 
		// other non-alphanumeric characters.
		searchIndex = graphDB.index().forNodes(
				DBNodeProperties.SEARCH_INDEX_NAME,
				MapUtil.stringMap("analyzer", CustomTokenAnalyzer.class.getName())
				);
	}

	private void setUriIndex() {
		 uriIndex = graphDB.index().forNodes(DBNodeProperties.URI_INDEX_NAME);
	}

	private void deleteSearchIndex(){
		deleteIndex(DBNodeProperties.SEARCH_INDEX_NAME);
	}

	private void deleteUriIndex(){
		deleteIndex(DBNodeProperties.URI_INDEX_NAME);
	}

	private void deleteIndex(String idxName) {
	// Delete index if it does exist
		if (graphDB.index().existsForNodes(idxName)) {
			System.out.println("Deleting index:" + idxName);
			Transaction tx = graphDB.beginTx();
			graphDB.index().forNodes(idxName).delete();
			tx.success();
			tx.finish();
		}
	}

	
	public Index<Node> buildAutoCompleteIndex(String indexName){
		Index<Node> autoCompleteIndex = graphDB.index().forNodes(indexName);		
		return autoCompleteIndex;
	}
}
