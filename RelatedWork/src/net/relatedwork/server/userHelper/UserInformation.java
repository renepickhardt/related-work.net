package net.relatedwork.server.userHelper;

import com.google.inject.Inject;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.dao.UserAccessHandler;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.LoginAction;
import net.relatedwork.shared.dto.NewUserAction;
import net.relatedwork.shared.dto.UserVerifyAction;
import net.relatedwork.shared.dto.UserVerifyActionResult;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class UserInformation {
	
	public String email = "";
	public String passwordHash = "";
	public String username = "";
	public ArrayList<String> sessionList = new ArrayList<String>();
	public Boolean isVerified = false;
	public String authSecret = "";
	
	private Node userNode = null;

    public void print() {
		IOHelper.log("email       : "+ email);
		IOHelper.log("passwordHash: "+ passwordHash);
		IOHelper.log("verified?   : "+ isVerified.toString());
		IOHelper.log("username    : "+ username);
		IOHelper.log("sessionList : "+ sessionList.toString());
	}

    private final EmbeddedGraphDatabase graphDB;

    @Inject UserAccessHandler userAccessHandler;

    @Inject
	public UserInformation(ServletContext servletContext){
        graphDB = ContextHelper.getGraphDatabase(servletContext);
    }
	
//	public UserInformation(String email){
//		this.email = email;
//	}

	/**
	 * NewUser action handler 
	 */
	public void registerNewUser(NewUserAction newUserAction) throws NewUserError {
		/* Check details */
		if (! userDetailsOk(newUserAction)) {
			throw new NewUserError("Error in user details");
		}
		
		/* Check if user is new */
		if (userRecordExists(newUserAction.getEmail())) {
			throw new NewUserError("User exists already");
		}
		
		/* set local variables */
		email    = newUserAction.getEmail();
		username = newUserAction.getUsername();
		// TODO: Secure password hashing with salt
		passwordHash = newUserAction.getPassword();
		registerSessionId(newUserAction.getSession().sessionId);
		authSecret = generateAuthSecret();

		/* save to database */
		createUserNode();
		
		/* send verification email */
		sendVerificationEmail();
	}



	/**
	 * Email verification handler
	 */
	
	public UserVerifyActionResult verifyUser(UserVerifyAction action) throws VerificationException {

		/* Fetch user node from db */
		try {
			loadFromEmail(action.getEmail());
		} catch (IOException e) {
			throw new VerificationException(e.getMessage());
		}
		
		/* Check authentication secret */
		if (! authSecret.equals(action.getSecret())){
			throw new VerificationException("Verification failed! " + 
					authSecret + " != " + action.getSecret()
					);
		}
		
		/* User is correctly authenticated */
		this.isVerified = true;
		IOHelper.log("User "+ this.email+ " authenticated: "+ 
		 authSecret + " == " + action.getSecret());
		
		/* Login user */
		registerSessionId(action.getSession().sessionId);
		SessionInformation session = updateSIO(action.getSession());
		
		save();
		
		return new UserVerifyActionResult(session);
		
	}
	

	/**
	 * Login action handler
	 */
	public void loginUser(LoginAction loginAction) throws LoginException {
		Node userLoginNode = userAccessHandler.getUserNodeFromEmail(loginAction.getEmail());
		
		if ( userLoginNode == null ) {
			throw new LoginException("No such user "+ email);
		}
		
		if (!checkPassword(userLoginNode, loginAction.getPassword())){
			throw new LoginException("Wrong password ");
		}
		
		loadFromNode(userLoginNode);
		
		registerSessionId(loginAction.getSession().sessionId);
		SessionInformation session = updateSIO(loginAction.getSession());
		
		save();
		
		IOHelper.log("User logged in");
		print();	
	}

	/************ DATABASE ACTIONS **************/
	
	private void loadFromEmail(String email) throws IOException {
		Node userLoginNode = userAccessHandler.getUserNodeFromEmail(email);

		if ( userLoginNode == null ) {
			throw new IOException("Address not found"+ email);
		}
		
		loadFromNode(userLoginNode);		
	}

	private void loadFromNode(Node userLoginNode) {
		email        = (String) userLoginNode.getProperty(DBNodeProperties.USER_EMAIL);
		passwordHash = (String) userLoginNode.getProperty(DBNodeProperties.USER_PW_HASH);
		username     = (String) userLoginNode.getProperty(DBNodeProperties.USER_NAME);
		isVerified   = (Boolean) userLoginNode.getProperty(DBNodeProperties.USER_VERIFIED);
		authSecret   = (String) userLoginNode.getProperty(DBNodeProperties.USER_AUTH_SECRET);
		
		userNode     = userLoginNode;
		
		// TODO: OMG THIS IS DIRTY!
		String[] sessions = (String[])userLoginNode.getProperty(DBNodeProperties.USER_SESSIONS);
		for (String session:sessions){
			sessionList.add(session);
		}
		
		
		}
	
	private void save() {
		if (userNode == null) {
			IOHelper.log("Error saving to node: userNode not set.");
			print();
			return;
		}
		
		Transaction tx = graphDB.beginTx();
		try{
			userNode.setProperty(DBNodeProperties.USER_EMAIL, email);
			userNode.setProperty(DBNodeProperties.USER_PW_HASH, passwordHash);
			userNode.setProperty(DBNodeProperties.USER_NAME, username);
			userNode.setProperty(DBNodeProperties.USER_SESSIONS, sessionList.toArray(new String[sessionList.size()]));
			userNode.setProperty(DBNodeProperties.USER_VERIFIED, isVerified);
			userNode.setProperty(DBNodeProperties.USER_AUTH_SECRET, authSecret);
			
			userAccessHandler.indexUserNode(userNode, email);
			
			tx.success();
		} catch (Exception e){
			tx.failure();
			IOHelper.log(e.getMessage());
		} finally {
			tx.finish();
		}
	}


	private void createUserNode() {
		IOHelper.log("Creating new user");
		print();
		
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
	
	private void deleteUser() {
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
	
	private boolean userRecordExists(String email) {
		Node userNode = userAccessHandler.getUserNodeFromEmail(email);
		if (userNode == null){ 
			return false;
		} else if ((Boolean) userNode.getProperty(DBNodeProperties.USER_DELETED) == true) {
			return false;
		} else if ((Boolean) userNode.getProperty(DBNodeProperties.USER_VERIFIED) == false) {
			return false;
		}

		return true;
	}
	
	/***************** SESSION OBJECT MANAGEMENT *************/

	public SessionInformation updateSIO(SessionInformation SIO) {
		(new ServerSIO(SIO)).save();
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
	

	/*************** SMALL HELPER METHODS *************/
	
	private static boolean userDetailsOk( NewUserAction newUser ) {
		// TODO: More checks
		if (newUser.getUsername().trim().length() == 0) {
			IOHelper.log("Username invalid: "+ newUser.getUsername());
			return false;
		}
		if (! newUser.getEmail().contains("@")){
			IOHelper.log("Email address invalid: "+ newUser.getEmail());
			return false;
		}
		return true;
	}
	
	private boolean checkPassword(Node userLoginNode, String passwordHash) {
		String storedPwHash = (String) userLoginNode.getProperty(DBNodeProperties.USER_PW_HASH);
		return (storedPwHash.equals(passwordHash));
	}


	private void sendVerificationEmail() {
		String VERIFY_URL = "http://127.0.0.1:8888/RelatedWork.html?gwt.codesvr=127.0.0.1:9997#!userverify";
		
		String linkTarget = VERIFY_URL + 
				";email=" + this.email + 
				";secret=" + this.authSecret; 
				
		String subject = "Related-Work.net registration";
		String body = "Hello, "+ this.username  + "\n" +
				"Thank you for registering with Related-Work.net\n" +
				"Click here to verify your email address: \n"+
				linkTarget;
		
		Email mailHandler = new Email(this.email, subject, body);
		mailHandler.send();
	}

	private String generateAuthSecret() {
		Random generator = new Random();
		return ((Integer)generator.nextInt()).toString(); 
	}

	
}
