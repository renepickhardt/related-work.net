package net.relatedwork.client.discussion;

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

    /** Comment target: either Paper or Author */
    private String targetUri;
    /** A list of comments and replies for the target */
    private ArrayList<Comments> comments;
	
	public DiscussionsReloadedEvent() {
	}

	public DiscussionsReloadedEvent(String targetUri, ArrayList<Comments> comments) {
        this.targetUri = targetUri;
        this.comments = comments;
	}

    public String getTargetUri() {
        return targetUri;
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
