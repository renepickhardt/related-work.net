package net.relatedwork.server.action;

import javax.servlet.ServletContext;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.server.userHelper.UserInformation;
import net.relatedwork.server.userHelper.VerificationException;
import net.relatedwork.shared.dto.UserVerifyAction;
import net.relatedwork.shared.dto.UserVerifyActionResult;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class UserVerifyActionActionHandler implements
		ActionHandler<UserVerifyAction, UserVerifyActionResult> {

	@Inject ServletContext servletContext;
	
	@Inject
	public UserVerifyActionActionHandler() {
	}

	@Override
	public UserVerifyActionResult execute(UserVerifyAction action,
			ExecutionContext context) throws ActionException {

		UserInformation UIO = new UserInformation(servletContext);
		
		try {
			return UIO.verifyUser(action);
		} catch (VerificationException e) {
			throw new ActionException(e.getMessage());
		}
	}

	@Override
	public void undo(UserVerifyAction action, UserVerifyActionResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<UserVerifyAction> getActionType() {
		return UserVerifyAction.class;
	}
}
