package net.relatedwork.server.datamining;

import java.util.HashMap;
import java.util.Set;
import java.util.TreeMap;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipProperties;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.server.utils.Algo;
import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;

/**
 * calculates similar papers based on the tanimoto score of co-citations. 
 * so for two papers A and B the similarity score will be
 * |A cut B| / (|A| + |B| - |A cut B|)
 * where:
 * |A cut B| is the number of cocitations of A and B
 * |A| is the number of citations of A and
 * |B| is the number of citations of B
 * 
 * the similarity score is put in a new edge of edge type: RW:DM:CO_CITATION_SCORE
 * the edge has one property: SCORE
 * 
 * so far all edges that can possible exist will be stored papers are calculated and
 * inserted. In future we might want to store only the top k edges! with k around 50.
 * 
 * this class also triggers the calculation of similar authors based on cocitations. 
 * 
 * algorithms exist as a top k version (which can still be improved and there is code redundancy)
 * 
 * @author rpickhardt
 *
 */
public class SimilarPaperCalculator extends Calculator {
	public SimilarPaperCalculator() {
		graphDB = new EmbeddedGraphDatabase(Config.get().neo4jDbPath);
	}
	
	public SimilarPaperCalculator(EmbeddedGraphDatabase db) {
		graphDB = db;
	}

	
	public boolean startCalculation(int transactionThreshhold){
		calculateBackLinks(transactionThreshhold);

		Transaction tx = graphDB.beginTx();
		int transactionCount = 0;
		try {
			for (Node paper:graphDB.getAllNodes()){
				if (paper.hasProperty(DBNodeProperties.PAPER_TITLE)){
					HashMap<Node, Integer> coCitationCount = new HashMap<Node, Integer>();
					Integer citationCount = (Integer)paper.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
					for (Relationship rel:paper.getRelationships(RelationshipTypes.CITES,Direction.INCOMING)){//get backlinks
						Node parentPaper = rel.getOtherNode(paper);
						for (Relationship coCiteRel:parentPaper.getRelationships(RelationshipTypes.CITES,Direction.OUTGOING)){
							Node coCitedPaper = coCiteRel.getOtherNode(parentPaper);
							if (coCitedPaper.getId()==paper.getId())continue;
							if (coCitationCount.containsKey(coCitedPaper)){
								Integer tmp = coCitationCount.get(coCitedPaper);
								coCitationCount.put(coCitedPaper, tmp + 1);
							}
							else{
								coCitationCount.put(coCitedPaper, 1);
							}
						}
					}
						
					for (Node coCitation:coCitationCount.keySet()){
						Relationship coRel = paper.createRelationshipTo(coCitation, RelationshipTypes.CO_CITATION_SCORE);
						Integer citationCount1 = (Integer)coCitation.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
						Integer cut = coCitationCount.get(coCitation);
						Double tanimotoScore = (cut*1.0) / (citationCount + citationCount1 - cut);
						coRel.setProperty(DBRelationshipProperties.CO_CITATION_SCORE, tanimotoScore);
						if (++transactionCount % transactionThreshhold == 0){
							tx.success();
							tx.finish();
							tx = graphDB.beginTx();
							IOHelper.log(transactionCount + " weighted coCitation relations have been inserted so far");
							System.out.println("current relations similartiy score is " +tanimotoScore);			
						}
					}
				}
			}
			tx.success();
		}finally{
			tx.finish();
		}
		return true;
	}
	
	
	public boolean startTopKCalculation(int transactionThreshhold,int k){
		calculateBackLinks(transactionThreshhold);

		Transaction tx = graphDB.beginTx();
		int transactionCount = 0;
		try {
			for (Node paper:graphDB.getAllNodes()){
				if (paper.hasProperty(DBNodeProperties.PAPER_TITLE)){
					HashMap<Node, Integer> coCitationCount = new HashMap<Node, Integer>();
					Integer citationCount = (Integer)paper.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
					for (Relationship rel:paper.getRelationships(RelationshipTypes.CITES,Direction.INCOMING)){//get backlinks
						Node parentPaper = rel.getOtherNode(paper);
						for (Relationship coCiteRel:parentPaper.getRelationships(RelationshipTypes.CITES,Direction.OUTGOING)){
							Node coCitedPaper = coCiteRel.getOtherNode(parentPaper);
							if (coCitedPaper.getId()==paper.getId())continue;
							if (coCitationCount.containsKey(coCitedPaper)){
								Integer tmp = coCitationCount.get(coCitedPaper);
								coCitationCount.put(coCitedPaper, tmp + 1);
							}
							else{
								coCitationCount.put(coCitedPaper, 1);
							}
						}
					}
		
					
					Algo<Node, Integer> a = new Algo<Node, Integer>();
					
					TreeMap<Integer, Set<Node>> topkCoCitation = a.getTopkElements(coCitationCount, k);				

					int topkCnt=0;

					for (Integer cut: topkCoCitation.descendingKeySet()){
						for (Node coCitation: topkCoCitation.get(cut)){
							Relationship coRel = paper.createRelationshipTo(coCitation, RelationshipTypes.CO_CITATION_SCORE);
							Integer citationCount1 = (Integer)coCitation.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
							Double tanimotoScore = (cut*1.0) / (citationCount + citationCount1 - cut);
							coRel.setProperty(DBRelationshipProperties.CO_CITATION_SCORE, tanimotoScore);
						}
					}
					if (++transactionCount % transactionThreshhold == 0){
						tx.success();
						tx.finish();
						tx = graphDB.beginTx();
						IOHelper.log(transactionCount + " papers have been equiped with coCitation relations so far");
					}

				}
			}
			tx.success();
		}finally{
			tx.finish();
		}
		return true;
	}
	
	private boolean calculateBackLinks(int transactionThreshhold){
		Transaction tx = graphDB.beginTx();
		int transactionCount = 0;
		try {
			for (Node n:graphDB.getAllNodes()){
				if (n.hasProperty(DBNodeProperties.PAPER_TITLE)){
					transactionCount++;
					int citationCount = 0;
					for (Relationship rel:n.getRelationships(RelationshipTypes.CITES,Direction.INCOMING)){
						citationCount++;
					}
					n.setProperty(DBNodeProperties.PAPER_CITATION_COUNT, citationCount);
					if (++transactionCount % transactionThreshhold == 0){
						IOHelper.log(transactionCount + " nodes have citation Count Property");
						System.out.println("current paper has " +citationCount+ " citations");
						tx.success();
						tx.finish();
						tx = graphDB.beginTx();
					}

				}
			}
			tx.success();
		}finally{
			tx.finish();
		}
		return true;
	}

	public boolean similarTopKAuthors(int transactionThreshhold, int k) {
		Transaction tx = graphDB.beginTx();
		try {
			int transactionCount = 0;
			for (Node author:graphDB.getAllNodes()){
				if (!author.hasProperty("name"))continue;
				HashMap<Node, Double> simAuthors = new HashMap<Node, Double>();

				for (Relationship rel:author.getRelationships(RelationshipTypes.AUTHOROF)){
					Node paper = rel.getOtherNode(author);
					if (paper.hasProperty("title")){// i found a paper
						for (Relationship simPaperRel:paper.getRelationships(RelationshipTypes.CO_CITATION_SCORE, Direction.OUTGOING)){
							Node simPaper = simPaperRel.getEndNode();
							if (simPaperRel.hasProperty(DBRelationshipProperties.CO_CITATION_SCORE)){
								Double score = (Double)simPaperRel.getProperty(DBRelationshipProperties.CO_CITATION_SCORE);
								for (Relationship authorRel:simPaper.getRelationships(RelationshipTypes.AUTHOROF)){
									Node simAuthor = authorRel.getOtherNode(simPaper);
									if (simAuthor.getId()==author.getId())continue;
									if (simAuthors.containsKey(simAuthor))
										simAuthors.put(simAuthor, simAuthors.get(simAuthor) + score);
									else
										simAuthors.put(simAuthor, score);
								}
							}
						}
					}
				}
				
				//get top k as usual and enter to data base:				
				Algo<Node, Double> a = new Algo<Node, Double>();
				TreeMap<Double, Set<Node>> topkSimAuthors = a.getTopkElements(simAuthors, k);				
				int topkCnt=0;
				for (Double score: topkSimAuthors.descendingKeySet()){
					for (Node simAuthor: topkSimAuthors.get(score)){
						Relationship simAuthorRel = author.createRelationshipTo(simAuthor, RelationshipTypes.SIM_AUTHOR);
						simAuthorRel.setProperty(DBRelationshipProperties.SIM_AUTHOR_SCORE, score);
					}
				}
				if (++transactionCount % transactionThreshhold == 0){
					tx.success();
					tx.finish();
					tx = graphDB.beginTx();
					IOHelper.log(transactionCount + " papers have been equiped with similarAuthor relations so far");
				}
				
				
			}
			tx.success();
		}finally{
			tx.finish();
		}
		return true;
	}
	
}
