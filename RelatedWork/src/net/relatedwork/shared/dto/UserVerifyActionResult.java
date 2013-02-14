package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.String;
import net.relatedwork.client.tools.session.SessionInformation;

public class UserVerifyActionResult implements Result {

	private SessionInformation session;

	@SuppressWarnings("unused")
	private UserVerifyActionResult() {
		// For serialization only
	}

	public UserVerifyActionResult( SessionInformation session) {
		this.session = session;
	}

	public SessionInformation getSession() {
		return session;
	}
}
