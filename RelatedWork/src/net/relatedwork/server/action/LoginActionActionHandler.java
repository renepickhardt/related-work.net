package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.client.tools.login.LoginAction;
import net.relatedwork.client.tools.login.LoginActionResult;
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
		//TODO: Implement serverside user handling
		// Check login
		String username = action.getUsername();
		String password = action.getPassword();
		String sessionId = action.getSessionId();
		
		// Lookup data from userdb
		String emailAddress = "userseamil@hotmail.com";

		// register session to user
		// userAccount.addSession(sessionId);
		
		// return LoginResult object with userdata
        return new LoginActionResult(emailAddress,username);
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
