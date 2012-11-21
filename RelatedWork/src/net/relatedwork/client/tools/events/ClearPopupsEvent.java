package net.relatedwork.client.tools.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class ClearPopupsEvent extends GwtEvent<ClearPopupsEvent.ClearPopupsHandler> {

	public static Type<ClearPopupsHandler> TYPE = new Type<ClearPopupsHandler>();

	public interface ClearPopupsHandler extends EventHandler {
		void onclearP(ClearPopupsEvent event);
	}

	public ClearPopupsEvent() {
	}

	@Override
	protected void dispatch(ClearPopupsHandler handler) {
		handler.onclearP(this);
	}

	@Override
	public Type<ClearPopupsHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<ClearPopupsHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new ClearPopupsEvent());
	}
}
