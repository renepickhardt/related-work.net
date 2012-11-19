package net.relatedwork.server.executables;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Author;

import org.apache.commons.lang.StringUtils;
import org.apache.tools.ant.types.CommandlineJava.SysProperties;
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
		int maxPapers = 1000000;
		int maxAuthors = 1000000;

		// Import Neo4j DB
		System.out.println("Reading neo4j db from " + Config.get().neo4jDbPath);
		EmbeddedReadOnlyGraphDatabase graphDB = new EmbeddedReadOnlyGraphDatabase(Config.get().neo4jDbPath);

		// Get 'author names' and 'paper titles' along with their page ranks into one big list
		ArrayList<CompleteEntry> PaperEntryList = new ArrayList<CompleteEntry>(1000000);
		ArrayList<CompleteEntry> AuthorEntryList = new ArrayList<CompleteEntry>(500000);

		//final int PAPER_TYPE = 0;
		//final int AUTHOR_TYPE = 1;

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
			if (counter % 10000 == 0){
				IOHelper.log("Adding entries. Processed " + counter + " nodes. Filled " + (PaperEntryList.size() + AuthorEntryList.size())+ " index entries.");
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
		ArrayList<CompleteEntry> outputList = new ArrayList<CompleteEntry>(
				AuthorEntryList.subList(0, Math.min( AuthorEntryList.size(), maxAuthors ))
				);

		outputList.addAll(
				new ArrayList<CompleteEntry>(
				PaperEntryList.subList(0, Math.min( PaperEntryList.size(), maxPapers)))
				);

		// write list to file
		System.out.println("Writing entries to fike: "+ Config.get().autoCompleteFile);
		
		BufferedWriter out = IOHelper.openWriteFile(Config.get().autoCompleteFile);

		try{
			  for (CompleteEntry entry: outputList) {
				  out.write(entry.getSerialization());
			  }	
			  out.close();
		} catch (IOException e) {
			 System.err.println("Error: " + e.getMessage());
		}
	}

	private static boolean isPaperNode(Node node){
		return node.hasProperty(DBNodeProperties.PAPER_TITLE);
//		return getType(node).equals(DBNodeProperties.PAPER_LABEL_VALUE);
	}

	private static boolean isAuthorNode(Node node){
		return node.hasProperty(DBNodeProperties.AUTHOR_NAME);
//		return getType(node).equals(DBNodeProperties.AUTHOR_LABEL_VALUE);
	}

	private static String getType(Node node){
		for (Relationship type_rel: node.getRelationships(Direction.OUTGOING,RelationshipTypes.TYPE)) {
			return (String)type_rel.getEndNode().getProperty(DBNodeProperties.LABEL);
		}
		return "None";
	}


	/**
	 *  Container class for Auto complete entries  
	 */
	private static class CompleteEntry {
		private static Double maxAuthorScore = 0.0; 
		private static Double maxPaperScore = 0.0;

		private static final String SEP = "\t";
		private static final String DOUBLE_SEP = "\t\t";
		private static final String PAPER_IND = "p";
		private static final String AUTHOR_IND = "a";
		
		private static final int MULTIPLICATOR = 100000;
		
		public String indexEntry;  // to be added to auto complete index
		public Double score;       // used by ranking
		public String nodeType;		   // "PAPER" or "AUTHOR"
				
		/**
		 * Contruct CompleteEntry from Author Node
		 * @param node
		 */

		public void FromAuthorNode(Node node){
			this.nodeType = DBNodeProperties.AUTHOR_LABEL_VALUE;
			this.indexEntry = ((String) node.getProperty(DBNodeProperties.AUTHOR_NAME)).replaceAll("[\\t\\n]", "");
			this.score = (Double) node.getProperty(DBNodeProperties.PAGE_RANK_VALUE);

			if (score > maxAuthorScore) { 
				maxAuthorScore = score;
			}
		}
				
		public void FromPaperNode(Node node) {
			this.nodeType = DBNodeProperties.PAPER_LABEL_VALUE;
			this.score = (Double) node.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
			this.indexEntry = ((String) node.getProperty(DBNodeProperties.PAPER_TITLE)).replaceAll("[\\t\\n]", "");

			
			if (score > maxPaperScore) { 
				maxPaperScore = score;
			}

		}
		
		public int getNormalizedScore(){
			int nScore = 0;
			if (nodeType == DBNodeProperties.PAPER_LABEL_VALUE) {
				nScore = (int) Math.floor(score/maxPaperScore * MULTIPLICATOR);
			} else if (nodeType == DBNodeProperties.AUTHOR_LABEL_VALUE) {
				nScore = (int) Math.floor(score/maxAuthorScore * MULTIPLICATOR);				
			}
			return nScore;
		}
		
		public String getSerialization(){
			String out = "";
			for (String nEntry: getNormalizedEntries()) {
				out += getNormalizedScore() + DOUBLE_SEP + nEntry + "\n";
			}
			return out;
		}
		
		
		/**
		 * Generate normailzed auto complete entries. Examples:
		 * 
		 * filippenko, alexei v. \t a \t Filippenko, Alexei V.
         * alexei filippenko \t a \t Alexei Filippenko
         * zhang, b. \t a \t Zhang, B.
         * ising models on locally tree-like graphs \t p \t C    -- (for Captialize)
         * k-deformed Poincare algebras \t p \t N                -- (for NonCapitalize)
         * 
		 * @return List of index entries
		 */
		public ArrayList<String> getNormalizedEntries(){

			ArrayList<String> out = new ArrayList<String>();

			if (nodeType == DBNodeProperties.PAPER_LABEL_VALUE){
				// Is the first letter caps?
				String capsSwitch = "N";
				if (Character.isUpperCase(indexEntry.charAt(0))) { 
					capsSwitch = "C"; 
					}
				
				out.add(indexEntry.toLowerCase() + SEP + PAPER_IND + SEP + capsSwitch);
				
			} else if (nodeType == DBNodeProperties.AUTHOR_LABEL_VALUE) {
				// always add lower case version of indexEntry
				out.add(indexEntry.toLowerCase() + SEP + AUTHOR_IND + SEP + indexEntry);
				
				// Separate first name and second name
				String[] nameParts =  indexEntry.split(", ");
				
				if ( nameParts.length < 2) {
					// Only one name? -> return
					return out;
					}

				String firstName = nameParts[1];
				String lastName = nameParts[0];

				// Remove initials e.g. "A." from first name
				String[] firstNameTokens =  firstName.split("\\s+"); // split at whitespace
				ArrayList<String> goodTokens = new ArrayList<String>();
				
				for (String token: firstNameTokens) {
					if (token.length() == 2 && token.charAt(1) == '.' ){
						// skip this 'part'
					} else {
						goodTokens.add(token);
					}
				}
				
				if (goodTokens.size() == 0) { return out; }

				String reverseIndexEntry = StringUtils.join(goodTokens, " ") + " " + lastName;
				out.add(reverseIndexEntry.toLowerCase() + SEP + AUTHOR_IND + SEP + reverseIndexEntry);		
			}
			return out;
		}
	} 

	
}
