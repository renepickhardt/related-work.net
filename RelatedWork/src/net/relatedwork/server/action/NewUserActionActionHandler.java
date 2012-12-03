package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.server.userHelper.NewUserError;
import net.relatedwork.server.userHelper.ServerSIO;
import net.relatedwork.server.userHelper.UserInformation;
import net.relatedwork.shared.dto.NewUserAction;
import net.relatedwork.shared.dto.NewUserActionResult;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class NewUserActionActionHandler implements
		ActionHandler<NewUserAction, NewUserActionResult> {

	@Inject
	public NewUserActionActionHandler() {
	}

	@Override
	public NewUserActionResult execute(NewUserAction action,
			ExecutionContext context) throws ActionException {

		NewUserActionResult resultObject;
		
		// Update sessionInformation object for client
		ServerSIO session = new ServerSIO(action.getSession());
		session.save();
		
		// Register new user on server 		
		try {
			UserInformation.registerNewUser(action);
			resultObject= new NewUserActionResult(true);
			
		} catch (NewUserError e) {
			// something went wrong
			resultObject = new NewUserActionResult(false);
		}
		
		return resultObject;
	}

	@Override
	public void undo(NewUserAction action, NewUserActionResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<NewUserAction> getActionType() {
		return NewUserAction.class;
	}
}
