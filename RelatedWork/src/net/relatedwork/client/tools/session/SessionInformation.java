package net.relatedwork.client.tools.session;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.tools.login.LoginActionResult;

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
	// Session info
	public String sessionId;
	
	// Login info
	public String username;
	public String emailAddress;
    // public String gravatarUrl;
	
	public boolean isLoggedIn(){
		return emailAddress != null;
	}
	
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
