package net.relatedwork.client.Discussions;

import net.relatedwork.client.Discussions.events.DiscussionsReloadedEvent;
import net.relatedwork.client.Discussions.events.DiscussionsReloadedEvent.DiscussionsReloadedHandler;
import net.relatedwork.client.staticpresenter.NotYetImplementedPresenter;
import net.relatedwork.shared.dto.Comments;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.RichTextArea;

public class CommentPresenter extends PresenterWidget<CommentPresenter.MyView> {

	public interface MyView extends View {
		public RichTextArea getCommentRichTextArea();
		public Button getSendButton();	
		public HTMLPanel getCommentContainer();
		public void addComment(Comments c);
	}

	@Inject
	public CommentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Inject NotYetImplementedPresenter notYetImplementedPresenter;
	
	@Override
	protected void onBind() {
		registerHandler(getView().getSendButton().addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				addToPopupSlot(notYetImplementedPresenter);
			}}));
		
		registerHandler(getEventBus().addHandler(DiscussionsReloadedEvent.getType(),new DiscussionsReloadedHandler(){
			@Override
			public void onDiscussionsReloaded(DiscussionsReloadedEvent event) {
				getView().getCommentContainer().clear();
				for(Comments c:event.getComments()){
					getView().addComment(c);
				}
			}}));
		super.onBind();
	}
}
