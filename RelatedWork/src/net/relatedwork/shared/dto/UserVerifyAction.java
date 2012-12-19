package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.shared.dto.UserVerifyActionResult;

import java.lang.String;

public class UserVerifyAction extends UnsecuredActionImpl<UserVerifyActionResult> {

	private String email;
	private String secret;
	private SessionInformation session;
	
	@SuppressWarnings("unused")
	private UserVerifyAction() {
		// For serialization only
	}

	public UserVerifyAction(String email, String secret, SessionInformation session) {
		this.session = session;
		this.email = email;
		this.secret = secret;
	}

	public String getEmail() {
		return email;
	}

	public String getSecret() {
		return secret;
	}

	public SessionInformation getSession() {
		return session;
	}
}
