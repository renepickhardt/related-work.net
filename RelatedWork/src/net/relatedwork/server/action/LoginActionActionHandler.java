package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.shared.dto.LoginAction;
import net.relatedwork.shared.dto.LoginActionResult;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoginActionActionHandler implements
		ActionHandler<LoginAction, LoginActionResult> {

	@Inject
	public LoginActionActionHandler() {
	}

	@Override
	public LoginActionResult execute(LoginAction action, ExecutionContext context)
			throws ActionException {
		//TODO: Implement Serverside user handling
		// Check login
		String username = action.getUsername();
		String password = action.getPassword();
		SessionInformation session = action.getSession();
		
		// Lookup data from userdb
		String emailAddress = "userseamil@hotmail.com";
		
		// register session to user
		// userAccount.addSession(sessionId);
		
		// return LoginResult object with userdata
		session.setEmailAddress(emailAddress);
		session.setUsername(username);
		
        return new LoginActionResult(session);
	}

	@Override
	public void undo(LoginAction action, LoginActionResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoginAction> getActionType() {
		return LoginAction.class;
	}
}
