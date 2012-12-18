package net.relatedwork.client.discussion;

import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;
import net.relatedwork.shared.dto.Comments;

/**
 * This is a widget for the comment box. It can show two comment state:
 * 1. New comment: text area, submit button
 * 2. Existing comment: votes, author, date, comment text
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentBoxView extends ViewImpl implements CommentBoxPresenter.MyView {
    public interface Binder extends UiBinder<Widget, CommentBoxView> {
    }

    private final Widget widget;

    @UiField HorizontalPanel viewCommentPanel;
    @UiField VerticalPanel newCommentPanel;
    @UiField HTML commentContainer;
    @UiField RichTextArea commentRichTextArea;
    @UiField Button submitButton;

    @Inject
    public CommentBoxView(final Binder binder) {
        widget = binder.createAndBindUi(this);
        setExistingComment(null);
    }

    @Override
    public void setSubmitHandler(ClickHandler handler) {
        submitButton.addClickHandler(handler);
    }

    @Override
    public void setExistingComment(Comments comment) {
        if (comment == null) {
            // show the new comment box
            viewCommentPanel.setVisible(false);
            newCommentPanel.setVisible(true);
        } else {
            // show existing comment
            viewCommentPanel.setVisible(true);
            newCommentPanel.setVisible(false);
            commentContainer.setHTML(comment.getComment());
        }
    }

    @Override
    public Widget asWidget() {
        return widget;
    }
}
