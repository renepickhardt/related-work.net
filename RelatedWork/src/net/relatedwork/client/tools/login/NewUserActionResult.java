package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.Result;
import net.relatedwork.client.tools.session.SessionInformation;

public class NewUserActionResult implements Result {

	private SessionInformation session;

	@SuppressWarnings("unused")
	private NewUserActionResult() {
		// For serialization only
	}

	public NewUserActionResult(SessionInformation session) {
		this.session = session;
	}

	public SessionInformation getSession() {
		return session;
	}
}
