package net.relatedwork.client.discussion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import net.relatedwork.client.tools.events.LoadingOverlayEvent;
import net.relatedwork.shared.dto.*;

import static net.relatedwork.shared.dto.CommentVoteResult.VoteResult;

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
        this.dispatcher = dispatcher;
        setComment(null /* default to new comment */, false /* default to post */);
    }

    @Inject
    public void initHandlers() {
        getView().setSubmitHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                getEventBus().fireEvent(new LoadingOverlayEvent(true));

                dispatcher.execute(new NewCommentAction(getComment()), new AsyncCallback<Comments>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Sorry, server error, retry later.");
                        Window.alert(caught.toString());
                        getEventBus().fireEvent(new LoadingOverlayEvent(false));
                    }

                    @Override
                    public void onSuccess(Comments newComment) {
                        Window.alert("New comment success: " + newComment.getComment());

                        setComment(newComment, isReply);
                        if (submission != null) {
                            submission.success(newComment);
                        }

                        getEventBus().fireEvent(new LoadingOverlayEvent(false));
                    }
                });
            }
        });

        getView().setVoteHandler(new VoteEvent() {
            @Override
            public void vote(boolean up) {
                getEventBus().fireEvent(new LoadingOverlayEvent(true));

                CommentVoteAction commentVoteAction = new CommentVoteAction(null, null, up);
                dispatcher.execute(commentVoteAction, new AsyncCallback<CommentVoteResult>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Sorry, server error, retry later.");
                        Window.alert(caught.toString());
                        getEventBus().fireEvent(new LoadingOverlayEvent(false));
                    }

                    @Override
                    public void onSuccess(CommentVoteResult result) {
                        switch (result.getResult()) {
                        case ALREADY_VOTED:
                            Window.alert("You already voted!");
                            break;
                        case SUCCESS:
                            comment.setVoting(result.getVotes());
                            setComment(comment, isReply);
                            break;
                        }
                        getEventBus().fireEvent(new LoadingOverlayEvent(false));
                    }
                });
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
        if (comment != null) {
            // existing comment
            return comment;
        }
        // new comment
        Comments c = new Comments();
        c.setComment(getView().getNewComment());
        c.setVoting(0);
        // TODO
        c.setAuthor(new Author());
        //c.setDate();
        //c.setTarget();
        //c.setType();
        return c;
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
