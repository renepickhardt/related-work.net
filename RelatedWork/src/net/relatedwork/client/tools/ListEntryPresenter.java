package net.relatedwork.client.tools;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ListEntryPresenter extends
		PresenterWidget<ListEntryPresenter.MyView> {

	public interface MyView extends View {
		public HTMLPanel getRwVisibleListEntry();
		public void setRwVisibleListEntry(HTMLPanel rwVisibleListEntry);
		public HTMLPanel getRwHoverableListEntry();
		public void setRwHoverableListEntry(HTMLPanel rwHoverableListEntry);
		public FocusPanel getRwListEntry();
		public void setRwListEntry(FocusPanel rwListEntry);
		public void setHoverableVisibility(boolean isVisible);
	}

	@Inject
	public ListEntryPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		
		final FocusPanel fp = getView().getRwListEntry();
		
		fp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				getView().setHoverableVisibility(true);
				fp.setStyleName("rwListEntry-hover");
			}
		});
		fp.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				getView().setHoverableVisibility(false);
				fp.setStyleName("rwListEntry");
			}
		});

		
		
	}
}
