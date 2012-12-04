package net.relatedwork.server.userHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import scala.actors.threadpool.Arrays;

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
	
	private Node userNode;

	// used for db interaction
	private ServletContext servletContext;
	// Remark: Since the servlet context is not static,
	// we can not have static methods using the db!!
	
	
	public void print() {
		IOHelper.log("email       : "+ email);
		IOHelper.log("passwordHash: "+ passwordHash);
		IOHelper.log("username    : "+ username);
		IOHelper.log("sessionList : "+ sessionList.toString());
	}
	
	public UserInformation(ServletContext servletContext){
		this.servletContext = servletContext;
	}
	
	public UserInformation(String email){
		this.email = email;
	}

	/**
	 * Initialize UIO from NewUser Action.
	 * check if details are consistent
	 * check if user is new
	 * writes to db.
	 */
	public void registerNewUser(NewUserAction newUserAction) throws NewUserError {
		if (! userDetailsOk(newUserAction)) {
			throw new NewUserError("Error in user details");
		}

		if (userRecordExists(newUserAction.getEmail())) {
			throw new NewUserError("User exists already");
		}
		
		email    = newUserAction.getEmail();
		username = newUserAction.getUsername();
		// TODO: Secure password hashing with salt
		passwordHash = newUserAction.getPassword();
		registerSessionId(newUserAction.getSession().sessionId);		
		
		createUserNode();
	}

	private void createUserNode() {
		IOHelper.log("Creating new user");
		print();
		
		EmbeddedGraphDatabase graphDB = ContextHelper.getGraphDatabase(servletContext);
		
		Transaction tx = graphDB.beginTx();
		try{
			userNode = graphDB.createNode();
			save();
			
			tx.success();
		} catch (Exception e){
			tx.failure();
			IOHelper.log(e.getMessage());
		} finally {
			tx.finish();
		}
	}

	public boolean userRecordExists(String email) {
		Node userNode = ContextHelper.getUserNodeFromEamil(email, servletContext);
		if (userNode == null){ 
			return false;
		} else if ((Boolean) userNode.getProperty(DBNodeProperties.USER_DELETED) == true) {
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
	public void loginUser(LoginAction loginAction) throws LoginException {
		IOHelper.log("User login data: " + loginAction.toString());
		
		Node userLoginNode = ContextHelper.getUserNodeFromEamil(loginAction.getEmail(), servletContext);
		
		if ( userLoginNode == null ) {
			throw new LoginException("No such user "+ email);
		}
		
		if (!checkPassword(userLoginNode, loginAction.getPassword())){
			throw new LoginException("Wrong password ");
		}
		
		this.loadFromNode(userLoginNode);
		
	}

	private boolean checkPassword(Node userLoginNode, String passwordHash) {
		String storedPwHash = (String) userLoginNode.getProperty(DBNodeProperties.USER_PW_HASH);
		return (storedPwHash.equals(passwordHash));
	}

	public void loadFromNode(Node userLoginNode) {
		email        = (String) userLoginNode.getProperty(DBNodeProperties.USER_EMAIL);
		passwordHash = (String) userLoginNode.getProperty(DBNodeProperties.USER_PW_HASH);
		username     = (String) userLoginNode.getProperty(DBNodeProperties.USER_NAME);
		// OMG THIS IS DIRTY!
		sessionList  =  (ArrayList<String>) Arrays.asList(((String[])userLoginNode.getProperty(DBNodeProperties.USER_SESSIONS)));
		userNode = userLoginNode;
		}

	
	public SessionInformation updateSIO(SessionInformation SIO) {
		((ServerSIO)SIO).save();
		SIO.clearLogs();
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
		saveToNeo4J();
	}
	
	private void saveToNeo4J() {
		EmbeddedGraphDatabase graphDB = ContextHelper.getGraphDatabase(servletContext);

		Transaction tx = graphDB.beginTx();
		try{
			userNode.setProperty(DBNodeProperties.USER_EMAIL, email);
			userNode.setProperty(DBNodeProperties.USER_PW_HASH, passwordHash);
			userNode.setProperty(DBNodeProperties.USER_NAME, username);
			userNode.setProperty(DBNodeProperties.USER_SESSIONS, sessionList.toArray(new String[sessionList.size()]));
			
			ContextHelper.indexUserNode(userNode, email, servletContext);
			
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
