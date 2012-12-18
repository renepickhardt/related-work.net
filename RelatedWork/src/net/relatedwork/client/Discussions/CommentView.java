package net.relatedwork.client.Discussions;

import com.google.inject.Provider;
import net.relatedwork.client.discussion.CommentBoxPresenter;
import net.relatedwork.client.tools.ListEntryPresenter;
import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Comments;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
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
//	HTMLPanel commentContainer;
	@UiField DecoratedTabPanel discussionsTabPanel;

    Provider<CommentBoxPresenter> commentBoxPresenterProvider;

    public RichTextArea getCommentRichTextArea() {
		return commentRichTextArea;
	}

	public HTMLPanel getCommentContainer() {
		return commentContainer;
	}
	
	public void addComment(Comments c){
//		HTMLPanel child = new HTMLPanel(c.getComment());
//		VerticalPanel votings = new VerticalPanel();
//		Image down = new Image();
//		down.setUrl("images/down.png");
//		Image up = new Image();
//		up.setUrl("images/up.png");
//		Label voting = new Label("5");
//		votings.add(up);
//		votings.add(voting);
//		votings.add(down);
//
//		HorizontalPanel comment = new HorizontalPanel();
//
//		comment.add(votings);
//		comment.add(c.getAuthor().getAuthorLink());
//		comment.add(child);

        CommentBoxPresenter commentBoxPresenter = commentBoxPresenterProvider.get();
        commentBoxPresenter.setComment(c, false);
        commentContainer.add(commentBoxPresenter.getWidget());
	}
	
	public Button getSendButton() {
		return sendButton;
	}

    @Override
    public void reset() {
        commentContainer.clear();
        CommentBoxPresenter commentBoxPresenter = commentBoxPresenterProvider.get();
        commentContainer.add(commentBoxPresenter.getWidget());
    }

    public interface Binder extends UiBinder<Widget, CommentView> {
	}

	@Inject
	public CommentView(final Binder binder,
                       Provider<CommentBoxPresenter> commentBoxPresenterProvider) {
        widget = binder.createAndBindUi(this);
			
		discussionsTabPanel.setWidth("800px");
		discussionsTabPanel.setAnimationEnabled(true);

		discussionsTabPanel.getElement().getStyle().setMarginBottom(10.0, Unit.PX);

	    // Add a home tab
		discussionsTabPanel.add(commentContainer, "Questions (4)");
		discussionsTabPanel.add(new HTML("reviews"), "Review (2)");
		discussionsTabPanel.add(new HTML("list some summaries"), "Summary (1)");
		discussionsTabPanel.add(new HTML("general discussions"), "General Discussions (6)");
	    
	    // Return the content
	    discussionsTabPanel.selectTab(0);
	    discussionsTabPanel.ensureDebugId("cwTabPanel");

        this.commentBoxPresenterProvider = commentBoxPresenterProvider;
        showDefaultCommentBox();
    }

    private void showDefaultCommentBox() {
        CommentBoxPresenter commentBoxPresenter = commentBoxPresenterProvider.get();
        commentContainer.add(commentBoxPresenter.getWidget());
    }

	@Override
	public Widget asWidget() {
		return widget;
	}
}
