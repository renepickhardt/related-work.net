package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.Result;

public class LoginActionResult implements Result {

	private long userId;

	@SuppressWarnings("unused")
	private LoginActionResult() {
		// For serialization only
	}

	public LoginActionResult(long userId) {
		this.userId = userId;
	}

	public long getUserId() {
		return userId;
	}
}
