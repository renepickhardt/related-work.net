package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.client.tools.login.LoginAction;
import net.relatedwork.client.tools.login.UserInformation;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoginActionActionHandler implements
		ActionHandler<LoginAction, UserInformation> {

	@Inject
	public LoginActionActionHandler() {
	}

	@Override
	public UserInformation execute(LoginAction action, ExecutionContext context)
			throws ActionException {
		String userId = Integer.toString((action.getUsername() + action.getPassword()).hashCode()); 
		return new UserInformation(userId,action.getUsername());
	}

	@Override
	public void undo(LoginAction action, UserInformation result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoginAction> getActionType() {
		return LoginAction.class;
	}
}
