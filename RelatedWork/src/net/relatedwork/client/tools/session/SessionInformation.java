package net.relatedwork.client.tools.session;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.dto.LoginActionResult;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Random;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.inject.Inject;


/**
 * 
 * This class contains all User information available for the client: 
 * * Session information generated on page load
 * * Login infromation as soon as available
 * 
 * @author heinrich
 *
 */
public class SessionInformation implements IsSerializable {
	/** Data fields **/
	// Session info
	public String sessionId;
	
	// Login info
	public String username;
	public String emailAddress;	
    // public String gravatarUrl;
	
	// Logging Information 
	public ArrayList<String> eventLogList = new ArrayList<String>();

	
	/** Constructors **/
	public SessionInformation() {
	}
	
	public SessionInformation(SessionInformation sessionInformation){
		this.sessionId = sessionInformation.sessionId;
		this.username = sessionInformation.username;
		this.emailAddress = sessionInformation.emailAddress;		
		this.eventLogList = sessionInformation.eventLogList;
	}
	
	public SessionInformation(String sessionId) {
		this.sessionId = sessionId;
	}
	
	/** Status Control **/
	
	public void continueSession(){
		String sessionCookie = Cookies.getCookie(NameTokens.SESSION_ID);
		if (sessionCookie == null) {
			// new user
			this.sessionId = Integer.toString(Random.nextInt());
			Cookies.setCookie(NameTokens.SESSION_ID, sessionId);		
		} else {
			// old user
			this.sessionId = sessionCookie;
//			Window.alert("Found old Cookie");
		}
	}
		
	public void stopSession(){
		Cookies.removeCookie(NameTokens.SESSION_ID);
	}

	public void RegisterLogIn(LoginActionResult login){
		this.username = login.getUsername();
		this.emailAddress = login.getEmailAddress();		
	}

	public boolean isLoggedIn(){
		return emailAddress != null;
	}
	
	//Remark: Logout destroys object
	
	/** Log functions **/

	public void logEvent(String eventDescription){
		this.eventLogList.add(getUnixTimeStamp() + ": " + eventDescription);
	}

	public void clearLogs(){
		this.eventLogList.clear();
	}
	
	public void logUrl(String url){
		this.logEvent("visisted: " + url);
	}

	public void logSearch(String querryString){
		this.logEvent("search: " + querryString);
	}

	public void logPaper(String paper_uri) {
		this.logEvent("paper: " + paper_uri);
	}

	public void logAuthor(String author_uri) {
		this.logEvent("author: " + author_uri);
	}

	// Date helper
	private static int getUnixTimeStamp() {
        Date date = new Date();
        int iTimeStamp = (int) (date.getTime() * .001);
        return iTimeStamp;
	}
		
	/* setters and getters */
	
	public ArrayList<String> getEventLog() {
		return eventLogList;
	}

	public void setEventLog(ArrayList<String> visitedUrls) {
		this.eventLogList = visitedUrls;
	}

	public String getSessionId() {
		return sessionId;
	}

	public String getUsername() {
		return username;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	
}
