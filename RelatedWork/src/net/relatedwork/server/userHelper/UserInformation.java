package net.relatedwork.server.userHelper;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;

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
	
	public String emailAddress = "";
	public String username = "";
	public ArrayList<String> sessionList = new ArrayList<String>();
	
	// used for db interaction
	@Inject ServletContext servletContext;
	
	public UserInformation(){
	}
	
	private String getUri() {
		return emailAddress;
	}
	
	/**
	 * Registers new User to database.
	 * 
	 * Performs checks on the way and throws NewUserError if something goes wrong.
	 * 
	 * @param newUserAction DTO transfered on submit
	 */
	public static void registerNewUser( NewUserAction newUserAction ) throws NewUserError {
		if (userExists( newUserAction.getUri() )) {
			throw new NewUserError("User exists already");
		} 
		if (userDetailsOk(newUserAction)) {
			throw new NewUserError("Error in details");
		}

		// create new URI and save to db
		UserInformation UIO = new UserInformation();
		UIO.emailAddress = newUserAction.getEmail();
		UIO.username = newUserAction.getUsername();
		UIO.registerSessionId(newUserAction.getSession().sessionId);
		
		UIO.save();
	}


	public static boolean userExists( String uri ) {
		// TODO: Check in db
		return false;
	}
	
	public static boolean userDetailsOk( NewUserAction newUser ) {
		// TODO: Check Email address, etc.
		return true;
	}

	
	/**
	 * Factory method for Login Action
	 * 
	 * @param loginAction
	 * @return
	 * @throws LoginException
	 */
	public static UserInformation fromLogin(LoginAction loginAction) throws LoginException {
		// TODO: Implementation
		// Lookup userId = email in db
		// return restore dUIO
		
		
		return null;
	}

	public static UserInformation fromUri( String uri ){
		UserInformation UIO = new UserInformation();
		UIO.loadUri(uri);
		return UIO;
	}

	public void loadUri(String uri){
		Node userNode = ContextHelper.getUserNode(uri,servletContext);
		this.username        = (String) userNode.getProperty("foaf:username");
		this.emailAddress    =  (String) userNode.getProperty("foaf:emailadress");
		this.sessionList     =  (ArrayList<String>) userNode.getProperty("rw:user:sessionIds");
		}

		
	public SessionInformation updateSIO(SessionInformation SIO) {
		SIO.setEmailAddress(emailAddress);
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
		// Mark as deleted
	}
	

	
	public void save(){
		saveToDb();
	}
	
	private void saveToDb() {
		
	}


	/**
	 * Save UIO to file -- deprecated
	 */
	public void saveToFile() {
		File target = new File(Config.get().userDir, emailAddress + ".txt");
		target.getParentFile().mkdirs();
		
		IOHelper.log("Writing file: " + getSavePath());
		BufferedWriter writer;
		
		try {
			if ( (new File(getSavePath())).exists() ){
				writer = IOHelper.openWriteFile(getSavePath());
				writer.write("username: " + username + "\n");
				writer.write("email: " + emailAddress + "\n");
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
			return (new File(Config.get().userDir, emailAddress + ".txt")).getCanonicalPath();
		} catch (IOException e) {
			return null;
		}
	}


}
