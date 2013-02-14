package net.relatedwork.server.userHelper;

import javax.servlet.ServletContext;

import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.WildcardQuery;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;

public class UserManagement {

	private static EmbeddedGraphDatabase graphDB;
	private static Index<Node> uriIndex;

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setupDB();
		listUsers();
		shutdownDB();
	}

	private static void setupDB() {
		graphDB = new EmbeddedGraphDatabase(Config.get().neo4jDbPath);
		
		uriIndex = graphDB.index().forNodes(DBNodeProperties.URI_INDEX_NAME);
		((LuceneIndex<Node>) uriIndex).setCacheCapacity(DBNodeProperties.URI, 300000);
	}
	
	private static void shutdownDB(){
		graphDB.shutdown();
	}
	

	public static Node getNodeByUri(String uri) {
		return uriIndex.get(DBNodeProperties.URI, uri).getSingle();
	}

	public static Node getUserNodeFromEamil(String email) {
		return getNodeByUri("rw:user:" + email);
	}
	
	public static void listUsers() {
		for (Node userNode: uriIndex.query(
				new WildcardQuery( new Term( DBNodeProperties.URI, "rw:user:*" ) ) 
				)) {
			String email = null, passwordHash = null, username = null, authSecret = null;
			Boolean isVerified = false;
			
			try {
				email        = (String) userNode.getProperty(DBNodeProperties.USER_EMAIL);
				passwordHash = (String) userNode.getProperty(DBNodeProperties.USER_PW_HASH);
				username     = (String) userNode.getProperty(DBNodeProperties.USER_NAME);
				isVerified  = (Boolean) userNode.getProperty(DBNodeProperties.USER_VERIFIED);
				authSecret   = (String) userNode.getProperty(DBNodeProperties.USER_AUTH_SECRET);
			} catch (NotFoundException e){
				IOHelper.log("Incomplete user record for node: ");
				IOHelper.log(userNode.getPropertyKeys());
				IOHelper.log(userNode.getPropertyValues());
			}
			IOHelper.log("-- "+ email + " --\n" +
					"username:     " + username + "\n" +
					"passwordHash: " + passwordHash + "\n" +
					"isVerified:   " + isVerified.toString() + "\n" +
					"authSecret:   " + authSecret );
		}
	}
	
	public static void deleteUser(String email){
		
	}
	
	public static void deleteAllUsers() {
		
	}
	
}
