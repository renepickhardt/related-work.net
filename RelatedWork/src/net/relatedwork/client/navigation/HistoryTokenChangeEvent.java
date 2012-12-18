package net.relatedwork.client.navigation;


import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import java.lang.String;
import com.google.gwt.event.shared.HasHandlers;
import com.google.web.bindery.event.shared.HandlerRegistration;

public class HistoryTokenChangeEvent extends
		GwtEvent<HistoryTokenChangeEvent.HistoryTokenChangeHandler> {

	public static Type<HistoryTokenChangeHandler> TYPE = new Type<HistoryTokenChangeHandler>();
	private String nameToken;
	private String title;

	public interface HistoryTokenChangeHandler extends EventHandler {
		void onHistoryTokenChange(HistoryTokenChangeEvent event);
	}

	public interface HistoryTokenChangeHasHandlers extends HasHandlers {
		HandlerRegistration addHistoryTokenChangeHandler(
				HistoryTokenChangeHandler handler);
	}

	public HistoryTokenChangeEvent(String nameToken, String title) {
		this.nameToken = nameToken;
		this.title = title;
	}

	public String getNameToken() {
		return nameToken;
	}

	public String getTitle() {
		return title;
	}
	
	@Override
	protected void dispatch(HistoryTokenChangeHandler handler) {
		handler.onHistoryTokenChange(this);
	}

	@Override
	public Type<HistoryTokenChangeHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<HistoryTokenChangeHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source, String nameToken, String title) {
		source.fireEvent(new HistoryTokenChangeEvent(nameToken,title));
	}
}
