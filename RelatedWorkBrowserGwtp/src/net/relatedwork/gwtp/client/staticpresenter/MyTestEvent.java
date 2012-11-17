package net.relatedwork.gwtp.client.staticpresenter;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;

public class MyTestEvent extends GwtEvent<MyTestEvent.MyTestHandler> {

	public static Type<MyTestHandler> TYPE = new Type<MyTestHandler>();
	private String eventString;

	public interface MyTestHandler extends EventHandler {
		void onMyTest(MyTestEvent event);
	}

	public MyTestEvent(String eventString) {
		this.eventString = eventString;
	}

	public String getEventString() {
		return eventString;
	}

	@Override
	protected void dispatch(MyTestHandler handler) {
		handler.onMyTest(this);
	}

	@Override
	public Type<MyTestHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<MyTestHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String eventString) {
		source.fireEvent(new MyTestEvent(eventString));
	}
}
