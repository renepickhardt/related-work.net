package net.relatedwork.client.tools.session;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.String;

public class SesssionInformation implements Result {

	private String sessionId;

	@SuppressWarnings("unused")
	private SesssionInformation() {
		// For serialization only
	}

	public SesssionInformation(String session) {
		this.sessionId = session;
	}

	public String getSessionId() {
		return sessionId;
	}
}
