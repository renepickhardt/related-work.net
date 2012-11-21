package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.ActionImpl;
import net.relatedwork.client.tools.login.NewUserActionResult;
import java.lang.String;
import net.relatedwork.client.tools.session.SessionInformation;

public class NewUserAction extends ActionImpl<NewUserActionResult> {

	private String username;
	private String password;
	private String email;
	private SessionInformation session;

	@SuppressWarnings("unused")
	private NewUserAction() {
		// For serialization only
	}

	// as explained in the video we disable security checks
		@Override
		public boolean isSecured() {
			return false;
		}
		
	
	public NewUserAction(String username, String password, String email,
			SessionInformation session) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.session = session;
	}

	public String getUsername() {
		return username;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public SessionInformation getSession() {
		return session;
	}
}
