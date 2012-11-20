package net.relatedwork.client.tools.login;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.tools.session.SessionInformation;

import com.gwtplatform.dispatch.shared.Result;

/**
 * This is a shared document transfer object (dto) used created 
 * by the login action handler. It contains all session information 
 * about the user, like:
 *
 *  email          (not implemented, yet)
 *  username   
 *  session id
 *  etc.
 *
 * It should be available for all presenters in a given session, therefore
 * we store it as a static variable in the Main presenter.
 *  
 * @author heinrich
 *
 */

public class LoginActionResult implements Result {

	public SessionInformation session;
//	public String username;
//	public String emailAddress;

	public LoginActionResult(SessionInformation session) {
		this.session = session;
	}
		
	@SuppressWarnings("unused")
	private LoginActionResult() {
		// For serialization only
	}

	public String getEmailAddress() {
		return session.getEmailAddress();
	}
	
	public String getUsername(){
		return session.getUsername(); 
	}

	public SessionInformation getSession() {
		return session;
	}

	public void setSession(SessionInformation session) {
		this.session = session;
	}
	
	
}
