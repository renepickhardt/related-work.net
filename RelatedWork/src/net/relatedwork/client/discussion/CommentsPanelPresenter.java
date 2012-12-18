package net.relatedwork.client.discussion;

import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import net.relatedwork.client.Discussions.events.DiscussionsReloadedEvent;
import net.relatedwork.shared.dto.Comments;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * The Presenter for {@link CommentsPanelView}.
 * Handles DiscussionsReloadedEvent.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentsPanelPresenter
        extends Presenter<CommentsPanelPresenter.MyView, CommentsPanelPresenter.MyProxy> {

    private static final List<String> COMMENT_TAB_TITLES = Arrays.asList(
            "Question",
            "Review",
            "Summary",
            "General Discussion"
    );

    public interface MyView extends View {
        void setSelectionHandler(SelectionHandler<Integer> handler);
        void addPost(int tab, Widget widget);
        void addReply(int tab, Widget widget);

        void initTabs(List<String> tabTitles);

        void switchTab(int tab);
    }

    @ProxyCodeSplit
    public interface MyProxy extends Proxy<CommentsPanelPresenter> {
    }

    private Provider<CommentBoxPresenter> commentBoxPresenterProvider;

    @Inject
    public CommentsPanelPresenter(EventBus eventBus, MyView view, MyProxy proxy,
                                  Provider<CommentBoxPresenter> commentBoxPresenterProvider) {
        super(eventBus, view, proxy);
        this.commentBoxPresenterProvider = commentBoxPresenterProvider;
    }

    @Override
    protected void onBind() {
        registerHandler(getEventBus().addHandler(DiscussionsReloadedEvent.getType(), new DiscussionsReloadedEvent.DiscussionsReloadedHandler() {
            @Override
            public void onDiscussionsReloaded(DiscussionsReloadedEvent event) {
                setComments(event.getComments());
            }
        }));

        super.onBind();
    }

    public void setComments(ArrayList<Comments> comments) {
        getView().initTabs(COMMENT_TAB_TITLES);

        // Existing comments
        for (Comments c : comments) {
            CommentBoxPresenter commentBoxPresenter = commentBoxPresenterProvider.get();
            commentBoxPresenter.setComment(c);
            getView().addPost(0, commentBoxPresenter.getWidget());
        }

        // New comment boxes
        for (int i = 0; i < COMMENT_TAB_TITLES.size(); i++){
            CommentBoxPresenter postPresenter = commentBoxPresenterProvider.get();
            postPresenter.setComment(null);
            getView().addPost(i, postPresenter.getWidget());

            CommentBoxPresenter replyPresenter = commentBoxPresenterProvider.get();
            replyPresenter.setComment(null);
            getView().addReply(i, replyPresenter.getWidget());
        }

        getView().switchTab(0);
    }

    @Override
    protected void revealInParent() {
        throw new UnsupportedOperationException();
    }

}
