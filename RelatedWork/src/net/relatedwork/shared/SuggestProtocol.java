package net.relatedwork.shared;

import java.util.ArrayList;
import java.util.HashMap;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;

import org.apache.commons.lang.StringUtils;

public class SuggestProtocol {
	
	private static final String SEP = "\t";
	private static final String DOUBLE_SEP = "\t\t";
	private static final String PAPER_IND = "p";
	private static final String AUTHOR_IND = "a";

	
	/**
	 * Generate normailzed auto complete entries. for authors Examples:
	 * 
	 * filippenko, alexei v. \t a \t Filippenko, Alexei V.
     * alexei filippenko \t a \t Alexei Filippenko
     * zhang, b. \t a \t Zhang, B.
     * 
	 * @return List of index entries
	 */
	
	public static ArrayList<String> getSuggestTreeAuthor(String input){
		ArrayList<String> out = new ArrayList<String>();
		// always add lower case version of indexEntry
		out.add(input.toLowerCase() + SEP + AUTHOR_IND + SEP + input);
		
		// Separate first name and second name
		String[] nameParts =  input.split(", ");
		
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

		String reverseIndexEntry = join(goodTokens, " ") + " " + lastName;
		out.add(reverseIndexEntry.toLowerCase() + SEP + AUTHOR_IND + SEP + reverseIndexEntry);		

		return out;
	}

	/**
	 * Generate normailzed auto complete entries. for papers Examples:
	 * 
     * ising models on locally tree-like graphs \t p \t C    -- (for Captialize)
     * k-deformed Poincare algebras \t p \t N                -- (for NonCapitalize)
     * 
	 * @return List of index entries
	 */

	
	public static ArrayList<String> getSuggestTreePaper(String input){

		ArrayList<String> out = new ArrayList<String>();
		// Is the first letter caps?
		String capsSwitch = "N";
		if (Character.isUpperCase(input.charAt(0))) { 
			capsSwitch = "C"; 
			}
		
		out.add(input.toLowerCase() + SEP + PAPER_IND + SEP + capsSwitch);

		return out;
	}
	
	private static String join (ArrayList<String> list, String b){
		String result = "";
		for(String s:list){
			result = result.concat(s);
			result = result.concat(b);
		}
		result = result.substring(0, result.length()-b.length());
		return result;
	}
	
	public static String serializeMap(HashMap<String, Integer> map){
		String result = "";
		for (String k:map.keySet()){
			result = result.concat(k+"#$#"+map.get(k)+"#$#");
		} 
		if (result != ""){
			result = result.substring(0,result.length() -3);
		}
		return result;
	}
	
	public static HashMap<String, Integer> deSerializeHashMap(String input){
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		String[] values = input.split("#$#");
		for (int i = 2;i<values.length;i=i+2){
			map.put(values[i-2], Integer.parseInt(values[i-1]));
		}	
		return map;
	}
}
