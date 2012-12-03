package net.relatedwork.server.userHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.google.inject.Inject;

import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.LoginAction;
import net.relatedwork.shared.dto.NewUserAction;
import net.relatedwork.shared.dto.NewUserActionResult;

public class UserInformation {
	
	public String email = "";
	public String passwordHash = "";
	public String username = "";
	public ArrayList<String> sessionList = new ArrayList<String>();
	

	// used for db interaction
	@Inject ServletContext servletContext;
	// Remark: Since the servlet context is not static,
	// we can not have static methods using the db!!
	
	
	public UserInformation(){
	}
	
	public UserInformation(String email){
		this.email = email;
	}

	/**
	 * Initialize UIO from NewUser Action.
	 * does consisency check
	 * does _not_ check if user is new
	 * does _not_ write to db.
	 */
	public UserInformation(NewUserAction newUserAction) throws Exception {
		if (! userDetailsOk(newUserAction)) {
			throw new Exception("Error in user details");
		}
		email    = newUserAction.getEmail();
		username = newUserAction.getUsername();
		// TODO: Secure password hashing with salt
		passwordHash = newUserAction.getPassword();
		registerSessionId(newUserAction.getSession().sessionId);
	}

	/**
	 * Register new user to db.
	 * Checks for uniquicy
	 * @throws NewUserError
	 */
	public void registerNewUser() throws NewUserError {
		if (email == null) {
			throw new NewUserError("No Email provided");
		} if (userRecordExists()) {
			throw new NewUserError("User exists already");
		}
		
		save();
	}

	public boolean userRecordExists() {
		Node userNode = getUserNode();
		if (userNode == null){ 
			// user node not in index
			return false; 
		} else if ((Boolean) userNode.getProperty(DBNodeProperties.USER_DELETED) == true) {
			// user marked as deleted
			return false;
		}
		return true;
	}
	
	public static boolean userDetailsOk( NewUserAction newUser ) {
		// TODO: More checks
		if (newUser.getUsername() == "") {
			IOHelper.log("Usernam invalid: "+ newUser.getUsername());
			return false;
		}
		if (! newUser.getEmail().contains("@")){
			IOHelper.log("Email address invalid: "+ newUser.getEmail());
			return false;
		}
		return true;
	}

	
	/**
	 * Handle login Login Action
	 */
	public void loadLogin(LoginAction loginAction) throws LoginException {
		Node userNode = getUserNode(email);
		
		if (userNode == null ) {
			throw new LoginException("No such user "+ email);
		}
		
		if (!checkPassword(userNode, loginAction.getPassword())){
			throw new LoginException("Wrong password "+ loginAction.getPassword());
		}
		
		this.loadFromNode(userNode);
		
	}

	private boolean checkPassword(Node userNode, String password) {
		return (userNode.getProperty(DBNodeProperties.USER_PW_HASH).equals(password));
	}

	public void loadFromNode(Node userNode) {
		this.email        = (String) userNode.getProperty(DBNodeProperties.USER_EMAIL);
		this.passwordHash = (String) userNode.getProperty(DBNodeProperties.USER_PW_HASH);
		this.username     = (String) userNode.getProperty(DBNodeProperties.USER_NAME);
		this.sessionList  = (ArrayList<String>) userNode.getProperty(DBNodeProperties.USER_SESSIONS);		
		}
	
	
	private Node getUserNode(){
		return getUserNode(email);
	}
	
	private Node getUserNode(String email){
		if (email == null){
			return null;
		} else {
			return ContextHelper.getUserNodeFromEamil(email,servletContext);
		}
	}
	
	public SessionInformation updateSIO(SessionInformation SIO) {
		SIO.setEmailAddress(email);
		SIO.setUsername(username);
		return SIO;
	}
	
	public void registerSessionId(String sessionId) {
		// TODO Auto-generated method stub
		if (! sessionList.contains(sessionId)) {
			sessionList.add(sessionId);
		}
	}
	
	public void deleteUser() {
		EmbeddedGraphDatabase graphDB = ContextHelper.getGraphDatabase(servletContext);
		
		Node userNode = getUserNode();
		
		Transaction tx = graphDB.beginTx();
		try{
			userNode.setProperty(DBNodeProperties.USER_DELETED, true);
			tx.success();
		} catch (Exception e){
			tx.failure();
			IOHelper.log(e.getMessage());
		} finally {
			tx.finish();
		}
	}
	
	
	public void save(){
		saveToDb();
	}
	
	private void saveToDb() {
		EmbeddedGraphDatabase graphDB = ContextHelper.getGraphDatabase(servletContext);
		
		Node userNode = getUserNode();
		
		Transaction tx = graphDB.beginTx();
		try{
			userNode.setProperty(DBNodeProperties.USER_EMAIL, email);
			userNode.setProperty(DBNodeProperties.USER_PW_HASH, passwordHash);
			userNode.setProperty(DBNodeProperties.USER_NAME, username);
			userNode.setProperty(DBNodeProperties.USER_SESSIONS, sessionList);
			
			tx.success();
		} catch (Exception e){
			tx.failure();
			IOHelper.log(e.getMessage());
		} finally {
			tx.finish();
		}
		
	}


	/**
	 * Save UIO to file -- deprecated
	 */
	public void saveToFile() {
		File target = new File(Config.get().userDir, email + ".txt");
		target.getParentFile().mkdirs();
		
		IOHelper.log("Writing file: " + getSavePath());
		BufferedWriter writer;
		
		try {
			if ( (new File(getSavePath())).exists() ){
				writer = IOHelper.openWriteFile(getSavePath());
				writer.write("username: " + username + "\n");
				writer.write("email: " + email + "\n");
			} else {
				writer = IOHelper.openWriteFile(getSavePath());
			}
			for (String sessionId: sessionList){
				writer.write(sessionId + "\n");
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private String getSavePath(){
		try {
			return (new File(Config.get().userDir, email + ".txt")).getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}


}
