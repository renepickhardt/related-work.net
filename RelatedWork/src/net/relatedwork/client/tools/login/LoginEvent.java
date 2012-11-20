package net.relatedwork.client.tools.login;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import net.relatedwork.client.tools.login.LoginActionResult;
import com.google.gwt.event.shared.HasHandlers;

public class LoginEvent extends GwtEvent<LoginEvent.LoginHandler> {

	public static Type<LoginHandler> TYPE = new Type<LoginHandler>();
	private LoginActionResult loginResult;

	public interface LoginHandler extends EventHandler {
		void onLogin(LoginEvent event);
	}

	public LoginEvent(LoginActionResult loginResult) {
		this.loginResult = loginResult;
	}

	public LoginActionResult getLoginResult() {
		return loginResult;
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

	public static void fire(HasHandlers source, LoginActionResult userInformation) {
		source.fireEvent(new LoginEvent(userInformation));
	}
}
