package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.client.tools.session.RegisterSesssionAction;
import net.relatedwork.client.tools.session.RegisterSesssionActionResult;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class RegisterSesssionActionActionHandler implements
		ActionHandler<RegisterSesssionAction, RegisterSesssionActionResult> {

	@Inject
	public RegisterSesssionActionActionHandler() {
	}

	@Override
	public RegisterSesssionActionResult execute(RegisterSesssionAction action,
			ExecutionContext context) throws ActionException {
		System.out.println("Called Session Handler");
		return new RegisterSesssionActionResult("myNewSessionId");
	}

	@Override
	public void undo(RegisterSesssionAction action,
			RegisterSesssionActionResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<RegisterSesssionAction> getActionType() {
		return RegisterSesssionAction.class;
	}
}
