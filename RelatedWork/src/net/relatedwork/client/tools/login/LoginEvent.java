package net.relatedwork.client.tools.login;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import net.relatedwork.client.tools.session.SessionInformation;
import com.google.gwt.event.shared.HasHandlers;
import com.google.gwt.user.client.Window;

public class LoginEvent extends GwtEvent<LoginEvent.LoginHandler> {

	public static Type<LoginHandler> TYPE = new Type<LoginHandler>();
	private SessionInformation session;

	public interface LoginHandler extends EventHandler {
		void onLogin(LoginEvent event);
	}

	public LoginEvent(SessionInformation session) {
		this.session = session;
	}

	public SessionInformation getSession() {
		return session;
	}

	@Override
	protected void dispatch(LoginHandler handler) {
		handler.onLogin(this);
	}

	@Override
	public Type<LoginHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoginHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, SessionInformation session) {
		source.fireEvent(new LoginEvent(session));
	}



}
