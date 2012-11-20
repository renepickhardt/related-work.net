package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.ActionImpl;
import net.relatedwork.client.tools.login.UserInformation;
import java.lang.String;

public class LoginAction extends ActionImpl<UserInformation> {

	private String username;
	private String password;

	// as explained in the video we disable security checks
	@Override
	public boolean isSecured() {
		return false;
	}
	
	@SuppressWarnings("unused")
	private LoginAction() {
		// For serialization only
	}

	public LoginAction(String username, String password) {
		this.username = username;
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}
}
