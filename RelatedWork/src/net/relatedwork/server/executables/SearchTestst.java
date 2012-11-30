package net.relatedwork.server.executables;

import java.io.Reader;
import java.util.Arrays;
import java.util.HashSet;

import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.action.GlobalSearchActionHandler;
import net.relatedwork.server.datamining.BuildIndices;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.Neo4jToDTOHelper;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.Config;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.GlobalSearchResult;
import net.relatedwork.shared.dto.Paper;

import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.apache.lucene.search.WildcardQuery;
import org.apache.lucene.util.Version;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.index.IndexManager;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.index.lucene.ValueContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;

/**
 * 
 * Lucene Docs:
 * http://people.apache.org/~yonik/presentations/lucene_intro.pdf
 * http://alias-i.com/lingpipe-book/lucene-3-tutorial-0.5.pdf
 * http://oak.cs.ucla.edu/cs144/projects/lucene/index.html
 * 
 * Custom Analyzer
 * https://groups.google.com/forum/#!msg/neo4j/SW84fJlTDTM/eXCC4g9L8TYJ
 * 
 * Lucene GUI
 * http://www.getopt.org/luke/ -- nice but did not work for me
 * 
 * @author heinrich
 *
 */

public class SearchTestst {
	private static EmbeddedGraphDatabase graphDB;
	private static Index<Node> index;
	
	public static void fillDummyValues(){
		System.out.println("Filling Dummy Nodes");
		Transaction tx = graphDB.beginTx();
		
		Node node1 = graphDB.createNode();
		Node node2 = graphDB.createNode();
		Node node3 = graphDB.createNode();
		Node node4 = graphDB.createNode();
		
		node1.setProperty("label", "my first, and node- stopword working practice Title!");
		node2.setProperty("label", "Secondos, and Nodos");
		node3.setProperty("label", "Tertios, Nodos");
		node4.setProperty("label", "Quartios- Nodos");

		index.add(node1 ,"score", new ValueContext( 1 ).indexNumeric());
		index.add(node2 ,"score", new ValueContext( 0 ).indexNumeric());
		index.add(node3 ,"score", new ValueContext( 2 ).indexNumeric());
		index.add(node4 ,"score", new ValueContext( 3 ).indexNumeric());

		
		index.add(node1, "label", node1.getProperty("label"));
		index.add(node2, "label", node2.getProperty("label"));
		index.add(node3, "label", node3.getProperty("label"));
		index.add(node4, "label", node4.getProperty("label"));
		
		// DynamicRelationshipType testType = DynamicRelationshipType.withName("test");
		// node1.createRelationshipTo(node2, testType);
		
		tx.success();
		tx.finish();
		
	}

	
	public static void main(String[] args) {
		graphDB = new EmbeddedGraphDatabase("myTestDb");
		index = graphDB.index().forNodes("custom4Index", 
				MapUtil.stringMap( "analyzer", MyStandardAnalyzer.class.getName() ) 
				);
		
		
		fillDummyValues();
		
		
		doSearches();
		
		graphDB.shutdown();
	}
	
	public static void doSearches() {
		String queryString = "label:(*)";
		
		System.out.println("Querry index: "+ queryString);
		
		IndexHits<Node> results = index.query(
				new QueryContext(queryString).
					defaultOperator(Operator.AND).
					sort("score", "label").
					top(200)
				);
		
		System.out.println("Hits "+results.size());
		
		for (Node n: results){
			System.out.println(
					"Result: " +
			(String)n.getProperty("label")
			);
		}
		
		
	}
	
	public static void doOldSearch() {
		Index<Node> index = graphDB.index().forNodes("test_idx");
		
		IndexHits<Node> res = index.query("title", "*");

		System.out.println("Query returned "+res.size()+" results");
		
		String query = "Ed";

		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		for (Node n : res) {
			if (!n.hasProperty(DBNodeProperties.LABEL)) continue;
			System.out.println(n.getProperty(DBNodeProperties.LABEL));
		}

	
	}
}
