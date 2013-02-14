package net.relatedwork.client.tools.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.Boolean;
import com.google.gwt.event.shared.HasHandlers;

public class LoadingOverlayEvent extends
		GwtEvent<LoadingOverlayEvent.LoadingOverlayHandler> {

	public static Type<LoadingOverlayHandler> TYPE = new Type<LoadingOverlayHandler>();
	private Boolean show;

	public interface LoadingOverlayHandler extends EventHandler {
		void onLoadingOverlay(LoadingOverlayEvent event);
	}

	public LoadingOverlayEvent(Boolean show) {
		this.show = show;
	}

	public Boolean getShow() {
		return show;
	}

	@Override
	protected void dispatch(LoadingOverlayHandler handler) {
		handler.onLoadingOverlay(this);
	}

	@Override
	public Type<LoadingOverlayHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<LoadingOverlayHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, Boolean show) {
		source.fireEvent(new LoadingOverlayEvent(show));
	}
}
