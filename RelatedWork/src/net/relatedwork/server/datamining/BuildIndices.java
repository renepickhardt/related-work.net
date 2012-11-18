package net.relatedwork.server.datamining;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.lucene.ValueContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import net.relatedwork.server.utils.IOHelper;

/**
 * build various lucene indices that are usefull for deployement.
 * @author rpickhardt
 *
 */

public class BuildIndices extends Calculator{
	
	public Index<Node> buildSearchIndex(String indexName){
		Index<Node> searchIndex = graphDB.index().forNodes(indexName);
		Transaction tx = graphDB.beginTx();
		try {
			int cnt = 0;
			for (Node n:graphDB.getAllNodes()){
				if (n.hasProperty("title") && n.hasProperty("pageRankValue")){
					searchIndex.add(n, "title", (String)n.getProperty("title"));
					searchIndex.add(n, "pr", new ValueContext((Double)n.getProperty("pageRankValue")).indexNumeric());
				}
				if (n.hasProperty("name")){
					searchIndex.add(n, "title", (String)n.getProperty("name"));
					searchIndex.add(n, "pr", new ValueContext((Double)n.getProperty("pageRankValue")).indexNumeric());
				}
				if (cnt++%10000==0){
					IOHelper.log((cnt-1) + " nodes added to search index.");
					tx.success();
					tx.finish();
					tx = graphDB.beginTx();
				}
			}
			tx.success();
		}finally{
			tx.finish();
		}
		return searchIndex;
	}
	
	public Index<Node> buildAutoCompleteIndex(String indexName){
		Index<Node> autoCompleteIndex = graphDB.index().forNodes(indexName);
		
		return autoCompleteIndex;
	}
}
