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
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.client.tools.session.SessionInformationManager;
import net.relatedwork.shared.dto.*;

import static net.relatedwork.shared.dto.Comments.CommentType;

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
    private SessionInformationManager sessionInformationManager;
    private CommentSubmittedEventHandler submission;

    private String targetUri;
    private Comments comment;
    private CommentType type;
    private boolean isReply;

    @Inject
    public CommentBoxPresenter(final EventBus eventBus, final MyView view, final DispatchAsync dispatcher,
                               SessionInformationManager sessionInformationManager) {
        super(eventBus, view);
        this.dispatcher = dispatcher;
        this.sessionInformationManager = sessionInformationManager;
        setNewComment(false /* default to post */, null, "");
    }

    @Inject
    public void initHandlers() {
        getView().setSubmitHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                if (checkLogin() == null) return;

                getEventBus().fireEvent(new LoadingOverlayEvent(true));

                NewCommentAction newCommentAction = new NewCommentAction(getComment());
                dispatcher.execute(newCommentAction, new AsyncCallback<Comments>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Sorry, server error, retry later.");
                        Window.alert(caught.toString());
                        getEventBus().fireEvent(new LoadingOverlayEvent(false));
                    }

                    @Override
                    public void onSuccess(Comments newComment) {
                        Window.alert("New comment success: " + newComment.getComment());

                        getView().setExistingComment(null);
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
                SessionInformation sessionInformation = checkLogin();
                if (sessionInformation == null) return;

                getEventBus().fireEvent(new LoadingOverlayEvent(true));

                String loggedInUser = sessionInformation.getEmailAddress();

                CommentVoteAction commentVoteAction = new CommentVoteAction(loggedInUser, comment.getUri(), up);
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
                            setExistingComment(comment);
                            break;
                        }
                        getEventBus().fireEvent(new LoadingOverlayEvent(false));
                    }
                });
            }
        });
    }

    public void setExistingComment(Comments comment) {
        this.comment = comment;
        this.isReply = comment.getType() == null;
        this.type = comment.getType();
        this.targetUri = comment.getTargetUri();
        getView().setExistingComment(comment);
        getView().setShowExpand(!isReply);
    }

    public void setNewComment(boolean isReply, CommentType type, String targetUri) {
        this.comment = null;
        this.isReply = isReply;
        this.type = type;
        this.targetUri = targetUri;
        getView().setExistingComment(null);
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
        c.setAuthor(checkLogin());
        c.setTargetUri(targetUri);
        c.setType(type);
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


    private SessionInformation checkLogin() {
        SessionInformation sessionInformation = sessionInformationManager.get();
        if (!sessionInformation.isLoggedIn()) {
//            Window.alert("Please login!");
//            return null;
        }
        return sessionInformation;
    }
}
