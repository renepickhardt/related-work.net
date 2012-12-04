package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.ActionImpl;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.LoginActionResult;

import java.lang.String;

public class LoginAction extends ActionImpl<LoginActionResult> {

	private String email;
	private String password;
	private SessionInformation session;

	public String toString(){
		return "LoginActionObject \n email: " + email + "\npassword: " + password;			
	}
	
	// as explained in the video we disable security checks
	@Override
	public boolean isSecured() {
		return false;
	}
	
	@SuppressWarnings("unused")
	private LoginAction() {
		// For serialization only
	}

	public LoginAction(String email, String password, SessionInformation session) {
		this.email = email;
		this.password = password;
		this.session = session;
	}

	public String getEmail() {
		return email;
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
