package net.relatedwork.gwtp.shared;

import com.gwtplatform.dispatch.shared.Result;

public class SendTextToServerResult implements Result {

	private String response;

	@SuppressWarnings("unused")
	private SendTextToServerResult() {
		// For serialization only
	}

	public SendTextToServerResult(String response) {
		this.response = response;
	}

	public String getResponse() {
		return response;
	}
}
