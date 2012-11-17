package net.relatedwork.client.Discussions;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.RichTextArea;

public class CommentPresenter extends PresenterWidget<CommentPresenter.MyView> {

	public interface MyView extends View {
		public RichTextArea getCommentRichTextArea();
		public Button getSendButton();	
	}

	@Inject
	public CommentPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
