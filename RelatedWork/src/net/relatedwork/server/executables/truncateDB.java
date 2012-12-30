package net.relatedwork.server.executables;

import static org.neo4j.graphdb.DynamicRelationshipType.withName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipTypes;
import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.graphdb.traversal.UniquenessFactory;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;
import org.neo4j.kernel.Traversal;
import org.neo4j.kernel.Uniqueness;

public class truncateDB {

	/**
	 * Truncate the Neo4j database by performing a breadth first search around a
	 * given center node.
	 * 
	 * @param args
	 */
	public static final EmbeddedGraphDatabase graphDB = new EmbeddedGraphDatabase(
			Config.get().neo4jDbPath);

	public static Index<Node> uriIndex = graphDB.index().forNodes(
			DBNodeProperties.URI_INDEX_NAME);
	public static Index<Node> labelIndex = graphDB.index().forNodes(
			DBNodeProperties.LABEL_INDEX_NAME);
	
	private static final EmbeddedGraphDatabase newDB = new EmbeddedGraphDatabase( Config.get().neo4jDbPath + "_truncated" ); 
	private static HashMap<Long,Node> copiedNodes = new HashMap<Long,Node>();
	private static HashMap<Long,Relationship> copiedRelationships = new HashMap<Long,Relationship>();
	

	public static void main(String[] args) {
		
		copySubgraph(getStartNode(), 3);

		graphDB.shutdown();
	}

	public static Node getStartNode() {
		return uriIndex.get("uri", "author_Huybrechts_Daniel").getSingle();
	}

	public static Node copyNode( Node sourceNode ){
		if ( copiedNodes.containsKey( sourceNode.getId()) ) {
			return copiedNodes.get(sourceNode.getId());
		} 
		else {
			// new node
			Node newNode = newDB.createNode();
			
			for (String key: sourceNode.getPropertyKeys()){
				newNode.setProperty(key, sourceNode.getProperty(key));
			}
			
			copiedNodes.put(sourceNode.getId(), newNode );

			return newNode;
		}
	}

	public static Relationship copyRelationship( Relationship sourceRel ){
		if ( copiedRelationships.containsKey( sourceRel.getId()) ) {
			return copiedRelationships.get(sourceRel.getId());
		}
		else {
			Node newStart = copyNode(sourceRel.getStartNode());
			Node newEnd   = copyNode(sourceRel.getEndNode());
			
			Relationship newRel = newStart.createRelationshipTo(newEnd, sourceRel.getType());

			for (String key : sourceRel.getPropertyKeys()){
				newRel.setProperty(key, sourceRel.getProperty(key));
			}
			
			copiedRelationships.put(sourceRel.getId(), newRel);
			
			return newRel;
		} 
	}


	/**
	 * Copy citation and author subgraph of a given width from graphDB to newDB
	 * @param centerNode
	 * @param width
	 */
	public static void copySubgraph(Node centerNode, Integer width) {
		
		Traverser tr = Traversal.description().
				breadthFirst().				
				relationships(DBRelationshipTypes.CITES).
				relationships(DBRelationshipTypes.WRITTEN_BY).
				uniqueness(Uniqueness.NONE).
				evaluator( Evaluators.toDepth( width ) ).
				evaluator( Evaluators.fromDepth( 1 ) ).
				traverse(centerNode);

		
		Transaction tx = newDB.beginTx();
		int count = 0;
		try {
			for (Path p : tr) {
//				IOHelper.log("copying " + p.toString());

				copyRelationship(p.lastRelationship());
				
				if (count++ % 1000 == 0) {
					IOHelper.log("Writing to db. Node count " + count);
					tx.success();
					tx.finish();
					tx = newDB.beginTx();
				}
			}
			tx.success();
		} finally {
			tx.finish();
		}
	}
	
	
	
}
