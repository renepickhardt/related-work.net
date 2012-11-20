package net.relatedwork.server.neo4jHelper.benchmarks;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.neo4j.cypher.ExecutionEngine;
import org.neo4j.cypher.ExecutionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Traverser;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.UniquenessFactory;
import org.neo4j.graphdb.traversal.UniquenessFilter;
import org.neo4j.helpers.collection.IteratorUtil;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

import net.relatedwork.server.neo4jHelper.DBRelationshipProperties;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.server.utils.IOHelper;

/**
 * This class evaluates a friend of a friend query on the related work axiv data base. The goal is to calculate coAuthor relations. 
 * 
 * The query is benchmarked with 3 different methods. 
 * 
 * 1.) The Java Core API relating on methods like node.getRelationships(...)
 * 2.) The Neo4j Traverser Framework
 * 3.) Cypher Query language. 
 * 
 * Findings: Cypher is slow by an order of magnitute. 
 * 
 * Core API outperforms Traverserframework by 20 - 25 % 
 * 
 * Traverserframework is most elegant.
 *  
 * @author rpickhardt
 *
 */
public class FriendOfAFriendQueryBenchmark {
	private EmbeddedReadOnlyGraphDatabase graphDB;
	private BufferedWriter bw;
	FriendOfAFriendQueryBenchmark(){
		bw = IOHelper.openWriteFile("benchmarkStats");		
	}
	
	public FriendOfAFriendQueryBenchmark(EmbeddedReadOnlyGraphDatabase graphDB){
		this();
		this.graphDB = graphDB;
	}
	
	public void coAuthorBenchmark(int numQueries){
		long startTime;
		long endTime;
		startTime = System.currentTimeMillis();
		coAuthorOwnQueries(numQueries);
		endTime = System.currentTimeMillis();
		System.out.println("Own Queries: " + numQueries + "\ttime: " + (endTime-startTime));

		startTime = System.currentTimeMillis();
		coAuthorTraverser(numQueries);
		endTime = System.currentTimeMillis();
		System.out.println("Traverser: " + numQueries + "\ttime: " + (endTime-startTime));

		startTime = System.currentTimeMillis();
		coAuthorCypher(numQueries);
		endTime = System.currentTimeMillis();
		System.out.println("Cypher: " + numQueries + "\ttime: " + (endTime - startTime ));

		System.out.println("now again with warm caches");
		
		long aggregatedOwnTraversalTime = 0;
		long aggregatedTraversalTime = 0;
		long aggregatedCypherTime = 0;
		
		long tmp = 0;
		int resCnt = 0;
		for (int i = 0 ; i< 10; i++){
			startTime = System.currentTimeMillis();
			resCnt = coAuthorOwnQueries(numQueries);
			endTime = System.currentTimeMillis();
			tmp = (endTime-startTime);
			aggregatedOwnTraversalTime += tmp;
			System.out.println("Own Queries: " + numQueries + "\ttime: " + tmp + "\t resCnt" + resCnt);

			startTime = System.currentTimeMillis();
			resCnt = coAuthorTraverser(numQueries);
			endTime = System.currentTimeMillis();
			tmp = (endTime-startTime);
			aggregatedTraversalTime += tmp;
			System.out.println("Traverser: " + numQueries + "\ttime: " + tmp + "\t resCnt" + resCnt);
			
			startTime = System.currentTimeMillis();
			resCnt = coAuthorCypher(numQueries);
			endTime = System.currentTimeMillis();
			tmp = (endTime-startTime);
			aggregatedCypherTime += tmp;
			System.out.println("Cypher: " + numQueries + "\ttime: " + tmp + "\t resCnt" + resCnt);
		}
		
		System.out.println("Own Queries: " + aggregatedOwnTraversalTime);
		System.out.println("Traversal queries: " + aggregatedTraversalTime);
		System.out.println("Cypher queries: " + aggregatedCypherTime);
		
		try {
			bw.write(numQueries + "\t" + aggregatedOwnTraversalTime + "\t"+ aggregatedTraversalTime + "\t" + aggregatedCypherTime + "\n");
			bw.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private int coAuthorOwnQueries(int numQueries){
		int qCnt = 0;
		int resCnt = 0;
		for (Node author:graphDB.getAllNodes()){
			if (!author.hasProperty("name"))continue;
			if (++qCnt>numQueries)break;
			for (Relationship rel: author.getRelationships(RelationshipTypes.AUTHOROF)){
				Node paper = rel.getOtherNode(author);
				for (Relationship coAuthorRel: paper.getRelationships(RelationshipTypes.AUTHOROF)){
					Node coAuthor = coAuthorRel.getOtherNode(paper);
					if (coAuthor.getId()==author.getId())continue;
					resCnt++;
				}
			}
		}		
		return resCnt;
	}
	
	private int coAuthorTraverser(int numQueries){
		int qCnt = 0;
		int resCnt =0;
		for (Node author:graphDB.getAllNodes()){
			if (!author.hasProperty("name"))continue;
			if (++qCnt>numQueries)break;
			Traversal t = new Traversal();
			for (Path p:t.description().breadthFirst().relationships(RelationshipTypes.AUTHOROF).evaluator(Evaluators.atDepth(2)).uniqueness(Uniqueness.NONE).traverse(author)){
				Node coAuthor = p.endNode();
				resCnt++;
			}			
		}		
		return resCnt;
	}
	
	private int coAuthorCypher(int numQueries){
		int qCnt = 0;
		int resCnt = 0;
		ExecutionEngine engine = new ExecutionEngine( graphDB );
		for (Node author:graphDB.getAllNodes()){
			if (!author.hasProperty("name"))continue;
			if (++qCnt>numQueries)break;
			
			Map<String, Object> params = new HashMap<String, Object>();
			params.put( "node", author );
			//ExecutionResult result = engine.execute( "start n=node({node}) return n.name", params );
			
			String query = "START author=node({node}) MATCH author-[:"+RelationshipTypes.AUTHOROF.name()+"]-()-[:"+RelationshipTypes.AUTHOROF.name()+"]- coAuthor RETURN coAuthor";
			ExecutionResult result = engine.execute( query, params);
			scala.collection.Iterator<Node> it = result.columnAs("coAuthor");
			while (it.hasNext()){
				Node coAuthor = it.next();
				resCnt++;
			}
		}
		return resCnt;
	}	
}
