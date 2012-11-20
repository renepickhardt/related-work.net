package net.relatedwork.client.tools.session;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.String;

public class RegisterSesssionActionResult implements Result {

	private String session;

	@SuppressWarnings("unused")
	private RegisterSesssionActionResult() {
		// For serialization only
	}

	public RegisterSesssionActionResult(String session) {
		this.session = session;
	}

	public String getSession() {
		return session;
	}
}
