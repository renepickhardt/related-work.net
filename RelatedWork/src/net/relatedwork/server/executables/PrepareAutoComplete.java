package net.relatedwork.server.executables;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.server.utils.Config;
import net.relatedwork.shared.dto.Author;

import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import com.google.gwt.uibinder.elementparsers.DialogBoxParser;

public class PrepareAutoComplete {

	/**
	 * This class prepares strings for the auto complete feature.
	 * We read out author names and paper titles from neo4j, 
	 * sort and normailze them and write them to a text file.
	 * 
	 * @param args
	 */

	
	public static void main(String[] args) {
		int maxPapers = 10;
		int maxAuthors = 10;

		// Import Neo4j DB
		System.out.println("Reading neo4j db from " + Config.get().neo4jDbPath);
		EmbeddedReadOnlyGraphDatabase graphDB = new EmbeddedReadOnlyGraphDatabase(Config.get().neo4jDbPath);

		
		// Get 'author names' and 'paper titles' along with their page ranks into one big list
		ArrayList<CompleteEntry> PaperEntryList = new ArrayList<CompleteEntry>();
		ArrayList<CompleteEntry> AuthorEntryList = new ArrayList<CompleteEntry>();

		int counter = 0; 
		for (Node node: graphDB.getAllNodes()) {
			if (isPaperNode(node)) {
				CompleteEntry entry = new CompleteEntry();
				entry.FromPaperNode(node);
				PaperEntryList.add(entry);
			}
			
			if (isAuthorNode(node)) {
				CompleteEntry entry = new CompleteEntry();
				entry.FromAuthorNode(node);
				AuthorEntryList.add(entry);
			}
			
			counter++;
			if (counter % 1000 == 0){
				System.out.println("Adding entries. Processed " + counter + " nodes. Filled " + (PaperEntryList.size() + AuthorEntryList.size())+ " index entries.");
				break;
			}

		}

		// Finished db reading.
		graphDB.shutdown();

		// sort list by page rank 
		System.out.println("Sorting index entries.");
		Collections.sort(PaperEntryList, new Comparator<CompleteEntry>() {
			@Override
			public int compare(CompleteEntry o1, CompleteEntry o2) {
				return -o1.score.compareTo(o2.score);
			}
		});
		Collections.sort(AuthorEntryList, new Comparator<CompleteEntry>() {
			@Override
			public int compare(CompleteEntry o1, CompleteEntry o2) {
				return -o1.score.compareTo(o2.score);
			}
		});

		
		// Forget lower elements
		ArrayList<CompleteEntry> OutputList = new ArrayList<CompleteEntry>(
				AuthorEntryList.subList(0, Math.min( AuthorEntryList.size(), maxAuthors))
				);

		OutputList.addAll(
				new ArrayList<CompleteEntry>(
				PaperEntryList.subList(0, Math.min( PaperEntryList.size(), maxPapers)))
				);
						
		// write list to file
		System.out.println("Writing entries to fike: "+ Config.get().autoCompleteFile);
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(Config.get().autoCompleteFile);
			  BufferedWriter out = new BufferedWriter(fstream);
			  for (CompleteEntry entry: OutputList) {
				  out.write(entry.getSerialization()+ "\n");
			  }	
			  out.close();
		 }catch (Exception e){ //Catch exception if any
			 System.err.println("Error: " + e.getMessage());
		 }
		
	}

	private static boolean isPaperNode(Node node){
		return getType(node).equals(DBNodeProperties.PAPER_LABEL_VALUE);
	}

	private static boolean isAuthorNode(Node node){
		return getType(node).equals(DBNodeProperties.AUTHOR_LABEL_VALUE);
	}
	
	private static String getType(Node node){
		for (Relationship type_rel: node.getRelationships(Direction.OUTGOING,RelationshipTypes.TYPE)) {
			return (String)type_rel.getEndNode().getProperty(DBNodeProperties.LABEL);
		}
		return "";
	}

	/**
	 *  Container class for Auto complete entries  
	 */
	private static class CompleteEntry {
		
		public String indexEntry;  // to be added to auto complete index
		public Double score;       // used by ranking
		public Long id;          // identify node in db
		
		// Alternative: use node.getId() for id field
		
		public void FromAuthorNode(Node node){
			this.score = (Double) node.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
			this.id  = (Long) node.getId();
			this.indexEntry = (String) node.getProperty(DBNodeProperties.AUTHOR_NAME);						
		}
		
		public void FromPaperNode(Node node) {
			this.score = (Double) node.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
			this.id  = (Long) node.getId();
			this.indexEntry = (String) node.getProperty(DBNodeProperties.PAPER_TITLE);			
		}
		
		public String getSerialization(){
			return score +"|" + id + "|"+ indexEntry;
		}
	} 

	
}
