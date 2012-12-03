package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.userHelper.LoginException;
import net.relatedwork.server.userHelper.UserInformation;
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
	public LoginActionResult execute(LoginAction loginAction, ExecutionContext context)
			throws ActionException {
		//TODO: Implement Serverside user handling
		// Check login
		String email = loginAction.getEmail();
		String password = loginAction.getPassword();
		SessionInformation SIO = loginAction.getSession();
		
		UserInformation UIO = new UserInformation();
		try {
			UIO.loginUser(loginAction);
		} catch (LoginException e) {
			throw new ActionException(e.getMessage());
		}
				
		// return LoginResult object with userdata
		SIO = UIO.updateSIO(SIO);
		
        return new LoginActionResult(SIO);
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
