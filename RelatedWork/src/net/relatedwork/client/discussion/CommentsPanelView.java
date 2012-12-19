package net.relatedwork.client.discussion;

import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.*;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

import java.util.List;

/**
 * The comments panel contains several tabs, each for one kind of comment.
 * Each tab contains a list of posts (on the left), and a list of replies (right).
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentsPanelView  extends ViewImpl implements CommentsPanelPresenter.MyView {

    private final Widget widget;

    @UiField DecoratedTabPanel commentsTabPanel;

    private DockLayoutPanel[] tabs;

    // TODO should have dedicated new post/reply boxes in each tab

    public interface Binder extends UiBinder<Widget, CommentsPanelView> {
    }

    @Inject
    public CommentsPanelView(final Binder binder) {
        widget = binder.createAndBindUi(this);
    }

    @Override
    public void initTabs(List<String> tabTitles) {
        commentsTabPanel.clear();
        tabs = new DockLayoutPanel[tabTitles.size()];
        for (int i = 0; i < tabTitles.size(); i++) {
            tabs[i] = new DockLayoutPanel(Style.Unit.PCT);
            tabs[i].addWest(new VerticalPanel(), 50);
            tabs[i].addEast(new VerticalPanel(), 50);
            hackScroll(tabs[i].getWidget(0));
            commentsTabPanel.add(tabs[i], tabTitles.get(i));
        }
    }

    @Override
    public void switchTab(int tab) {
        commentsTabPanel.selectTab(tab);
    }

    @Override
    public void setSelectionHandler(SelectionHandler<Integer> handler) {
        commentsTabPanel.addSelectionHandler(handler);
    }

    @Override
    public void addPost(int tab, Widget widget) {
        ((VerticalPanel)tabs[tab].getWidget(0)).add(widget);
    }

    @Override
    public void addReply(int tab, Widget widget) {
        ((VerticalPanel)tabs[tab].getWidget(1)).add(widget);
    }

    @Override
    public void resetReply(int tab) {
        VerticalPanel replyList = (VerticalPanel) tabs[tab].getWidget(1);
        replyList.clear();
    }

    @Override
    public Widget asWidget() {
        return widget;
    }

    private void hackScroll(Widget w) {
//        w.getElement().getParentElement().getStyle().setOverflow(Style.Overflow.SCROLL);
    }
}
