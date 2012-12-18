package net.relatedwork.client.discussion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.Comments;

/**
 * The PresenterWidget for {@link CommentBoxView}.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentBoxPresenter extends
        PresenterWidget<CommentBoxPresenter.MyView> {

    public interface MyView extends View {
        void setSubmitHandler(ClickHandler handler);
        void setExistingComment(Comments comment);
        void setExpandHandler(ClickHandler handler);
        void setVoteHandler(VoteEvent vote);
        String getNewComment();
        void markExpanded(boolean selected);
        void setShowExpand(boolean show);
    }

    public static interface VoteEvent {
        void vote(boolean up);
    }

    private DispatchAsync dispatcher;
    private CommentSubmittedEventHandler submission;

    private Comments comment;
    private boolean isReply;

    @Inject
    public CommentBoxPresenter(final EventBus eventBus, final MyView view,
                           final DispatchAsync dispatcher) {
        super(eventBus, view);
        this.dispatcher=dispatcher;
        setComment(null /* default to new comment */, false /* default to post */);
    }

    @Inject
    public void initHandlers() {
        getView().setSubmitHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                // TODO send data back
                Window.alert("submit clicked");

                // suppose this is the new comment coming back from server
                Comments newComment = new Comments(new Author(), getView().getNewComment());
                setComment(newComment, isReply);
                if (submission != null) {
                    submission.success(newComment);
                }
            }
        });

        getView().setVoteHandler(new VoteEvent() {
            @Override
            public void vote(boolean up) {
                int votes = comment.getVoting() + (up ? 1 : -1);
                comment.setVoting(votes);
                setComment(comment, isReply);
                // TODO send data back
            }
        });
    }

    public void setComment(Comments comment, boolean isReply) {
        this.comment = comment;
        this.isReply = isReply;
        getView().setExistingComment(comment);
        getView().setShowExpand(!isReply);
    }

    public Comments getComment() {
        return comment;
    }

    public void markExpanded(boolean selected) {
        getView().markExpanded(selected);
    }

    public void setExpandHandler(ClickHandler handler) {
        getView().setExpandHandler(handler);
    }

    public void setSubmittedEventHandler(CommentSubmittedEventHandler submitted) {
        this.submission = submitted;
    }
}
