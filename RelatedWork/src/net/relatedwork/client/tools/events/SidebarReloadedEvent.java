package net.relatedwork.client.tools.events;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class SidebarReloadedEvent extends
		GwtEvent<SidebarReloadedEvent.SidebarReloadedHandler> {

	public static Type<SidebarReloadedHandler> TYPE = new Type<SidebarReloadedHandler>();

	public interface SidebarReloadedHandler extends EventHandler {
		void onSidebarReloaded(SidebarReloadedEvent event);
	}

	private String test;
	
	public SidebarReloadedEvent() {
	}

	
	public SidebarReloadedEvent(String test) {
		this.test= test;
	}

	public String getTest() {
		return test;
	}

	public void setTest(String test) {
		this.test = test;
	}

	@Override
	protected void dispatch(SidebarReloadedHandler handler) {
		handler.onSidebarReloaded(this);
	}

	@Override
	public Type<SidebarReloadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<SidebarReloadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new SidebarReloadedEvent());
	}
}
