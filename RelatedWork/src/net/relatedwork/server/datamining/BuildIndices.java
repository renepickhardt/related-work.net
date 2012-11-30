package net.relatedwork.server.datamining;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.lucene.ValueContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.IOHelper;

/**
 * build various lucene indices that are usefull for deployement.
 * @author rpickhardt
 *
 */

public class BuildIndices extends Calculator{
	
	public Index<Node> buildSearchIndex(String indexName){
		Transaction transaction1 = graphDB.beginTx();
		graphDB.index().forNodes(indexName).delete();
		transaction1.success();
		transaction1.finish();
		
		Index<Node> searchIndex = graphDB.index().forNodes(indexName,
				MapUtil.stringMap(
						"type", "fulltext", 
						"to_lower_case", "true"
						)
				);

		Transaction transaction = graphDB.beginTx();
		try {
			int nodeCounter = 0;
			for (Node n:graphDB.getAllNodes()) {
				if (NodeType.isPaperNode(n) && n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE)){
					// Author node with page rank set
					searchIndex.add(n, "title", ((String)n.getProperty("title")).replaceAll("\\W+", " "));
					searchIndex.add(n, "pr", new ValueContext((Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE)).indexNumeric());
				}
				if (NodeType.isAuthorNode(n)) {
					// replace non-word characters with space
					searchIndex.add(n, "title", ((String)n.getProperty("name")).replaceAll("\\W+", " "));
					searchIndex.add(n, "pr", new ValueContext((Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE)).indexNumeric());
				}

				if (nodeCounter++%10000==0){
					// Print statistics
					IOHelper.log((nodeCounter-1) + " nodes added to search index.");
					transaction.success();
					transaction.finish();
					transaction = graphDB.beginTx();
				}
			}
			transaction.success();
		}finally{
			transaction.finish();
		}
		return searchIndex;
	}
	
	public Index<Node> buildAutoCompleteIndex(String indexName){
		Index<Node> autoCompleteIndex = graphDB.index().forNodes(indexName);
		
		return autoCompleteIndex;
	}
}
