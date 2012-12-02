package net.relatedwork.client.Discussions;

import net.relatedwork.shared.dto.Comments;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class CommentView extends ViewImpl implements CommentPresenter.MyView {

	private final Widget widget;

	@UiField RichTextArea commentRichTextArea;
	@UiField Button sendButton;
	@UiField HTMLPanel commentContainer;
	
	public RichTextArea getCommentRichTextArea() {
		return commentRichTextArea;
	}

	public HTMLPanel getCommentContainer() {
		return commentContainer;
	}
	
	public void addComment(Comments c){
		HTMLPanel child = new HTMLPanel(c.getComment());
		VerticalPanel votings = new VerticalPanel();
		Image down = new Image();
		down.setUrl("images/down.png");
		Image up = new Image();
		up.setUrl("images/up.png");
		Label voting = new Label("5");
		votings.add(up);
		votings.add(voting);
		votings.add(down);

		HorizontalPanel comment = new HorizontalPanel();

		comment.add(votings);
		comment.add(c.getAuthor().getAuthorLink());
		comment.add(child);
		
		commentContainer.add(comment);
	}
	
	public Button getSendButton() {
		return sendButton;
	}

	public interface Binder extends UiBinder<Widget, CommentView> {
	}

	@Inject
	public CommentView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
