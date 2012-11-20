package net.relatedwork.client.Discussions;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class CommentView extends ViewImpl implements CommentPresenter.MyView {

	private final Widget widget;

	@UiField RichTextArea commentRichTextArea;
	@UiField Button sendButton;
	
	public RichTextArea getCommentRichTextArea() {
		return commentRichTextArea;
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
