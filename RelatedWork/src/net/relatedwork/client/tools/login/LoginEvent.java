package net.relatedwork.client.tools.login;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import net.relatedwork.client.tools.login.UserInformation;
import com.google.gwt.event.shared.HasHandlers;

public class LoginEvent extends GwtEvent<LoginEvent.LoginHandler> {

	public static Type<LoginHandler> TYPE = new Type<LoginHandler>();
	private UserInformation userInformation;

	public interface LoginHandler extends EventHandler {
		void onLogin(LoginEvent event);
	}

	public LoginEvent(UserInformation userInformation) {
		this.userInformation = userInformation;
	}

	public UserInformation getUserInformation() {
		return userInformation;
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

	public static void fire(HasHandlers source, UserInformation userInformation) {
		source.fireEvent(new LoginEvent(userInformation));
	}
}
