package net.relatedwork.client.tools.session;

import com.gwtplatform.dispatch.shared.ActionImpl;

import net.relatedwork.client.tools.session.RegisterSesssionActionResult;

import java.lang.String;

public class RegisterSesssionAction extends
		ActionImpl<RegisterSesssionActionResult> {

	private String sessionId;
	
	@Override
	public boolean isSecured() {
		return false;
	}

	@SuppressWarnings("unused")
	private RegisterSesssionAction() {
		// For serialization only
	}

	public RegisterSesssionAction(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getSessionId() {
		return sessionId;
	}
}
