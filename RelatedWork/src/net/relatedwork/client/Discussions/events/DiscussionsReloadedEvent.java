package net.relatedwork.client.Discussions.events;

import java.util.ArrayList;

import net.relatedwork.shared.dto.Comments;

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.HasHandlers;

public class DiscussionsReloadedEvent extends
		GwtEvent<DiscussionsReloadedEvent.DiscussionsReloadedHandler> {

	public static Type<DiscussionsReloadedHandler> TYPE = new Type<DiscussionsReloadedHandler>();

	public interface DiscussionsReloadedHandler extends EventHandler {
		void onDiscussionsReloaded(DiscussionsReloadedEvent event);
	}

	
	private ArrayList<Comments> comments;
	
	public DiscussionsReloadedEvent() {
	}

	public DiscussionsReloadedEvent(ArrayList<Comments> comments) {
		this.comments = comments;
	}

	public ArrayList<Comments> getComments() {
		return comments;
	}
	
	@Override
	protected void dispatch(DiscussionsReloadedHandler handler) {
		handler.onDiscussionsReloaded(this);
	}

	@Override
	public Type<DiscussionsReloadedHandler> getAssociatedType() {
		return TYPE;
	}

	public static Type<DiscussionsReloadedHandler> getType() {
		return TYPE;
	}

	public static void fire(HasHandlers source) {
		source.fireEvent(new DiscussionsReloadedEvent());
	}
}
