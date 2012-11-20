package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.ActionImpl;
import net.relatedwork.client.tools.login.LoginActionResult;
import net.relatedwork.client.tools.session.SessionInformation;

import java.lang.String;

public class LoginAction extends ActionImpl<LoginActionResult> {

	private String username;
	private String password;
	private SessionInformation session;

	// as explained in the video we disable security checks
	@Override
	public boolean isSecured() {
		return false;
	}
	
	@SuppressWarnings("unused")
	private LoginAction() {
		// For serialization only
	}

	public LoginAction(String username, String password, SessionInformation session) {
		this.username = username;
		this.password = password;
		this.session = session;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
	
	public String getSessionId() {
		return session.getSessionId();
	}

	public SessionInformation getSession() {
		return session;
	}


}
