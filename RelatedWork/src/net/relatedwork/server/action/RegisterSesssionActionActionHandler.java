package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.client.tools.session.RegisterSesssionAction;
import net.relatedwork.client.tools.session.SesssionInformation;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class RegisterSesssionActionActionHandler implements
		ActionHandler<RegisterSesssionAction, SesssionInformation> {

	@Inject
	public RegisterSesssionActionActionHandler() {
	}

	@Override
	public SesssionInformation execute(RegisterSesssionAction action,
			ExecutionContext context) throws ActionException {
		System.out.println("Called Session Handler");
		return new SesssionInformation("myNewSessionId");
	}

	@Override
	public void undo(RegisterSesssionAction action,
			SesssionInformation result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<RegisterSesssionAction> getActionType() {
		return RegisterSesssionAction.class;
	}
}
