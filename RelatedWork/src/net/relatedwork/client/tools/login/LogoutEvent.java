package net.relatedwork.client.tools.login;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class LogoutEvent extends GwtEvent<LogoutEvent.LogoutHandler> {

	public static Type<LogoutHandler> TYPE = new Type<LogoutHandler>();

	public interface LogoutHandler extends EventHandler {
		void onLogout(LogoutEvent event);
	}

	public LogoutEvent() {
	}

	@Override
	protected void dispatch(LogoutHandler handler) {
		handler.onLogout(this);
	}

	@Override
	public Type<LogoutHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LogoutHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new LogoutEvent());
	}
}
