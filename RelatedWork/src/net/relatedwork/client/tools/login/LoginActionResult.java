package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.Result;

/**
 * This is a shared document transfer object (dto) used created 
 * by the login action handler. It contains all session information 
 * about the user, like:
 * 
 *  userId     (for cookie)
 *  username
 *  email      (not implemented, yet)
 *  etc.
 * 
 *  It should be available for all presenters in a given session, therefore
 *  we store it as a static variable in the Main presenter.
 *  
 * @author heinrich
 *
 */

public class LoginActionResult implements Result {

	public String userId;
	public String username;
	public String email;

	public LoginActionResult(String userId, String username) {
		this.userId = userId;
		this.username = username;
	}
	
	
	@SuppressWarnings("unused")
	private LoginActionResult() {
		// For serialization only
	}

	public String getUserId() {
		return userId;
	}
	
	public String getUsername(){
		return username; 
	}
}
