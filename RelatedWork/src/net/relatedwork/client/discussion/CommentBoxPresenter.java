package net.relatedwork.client.discussion;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
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
    }

    private DispatchAsync dispatcher;

    private Comments comment;

    @Inject
    public CommentBoxPresenter(final EventBus eventBus, final MyView view,
                           final DispatchAsync dispatcher) {
        super(eventBus, view);
        this.dispatcher=dispatcher;
        setComment(null /* default to new comment */);
    }

    @Inject
    public void initHandlers() {
        getView().setSubmitHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent clickEvent) {
                Window.alert("submit clicked");
            }
        });
    }

    public void setComment(Comments comment) {
        this.comment = comment;
        getView().setExistingComment(comment);
    }

}
