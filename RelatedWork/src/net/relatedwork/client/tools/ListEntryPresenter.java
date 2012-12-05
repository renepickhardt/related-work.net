package net.relatedwork.client.tools;

import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Author;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class ListEntryPresenter<T extends IsRenderable> extends
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
		
		// change CSS style on mouse over
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
	
	public void setContent(T element){
		
		HTMLPanel visiblePanel = getView().getRwVisibleListEntry();
		visiblePanel.add(element.getAuthorLink());
		
		final HTMLPanel hoverPanel = getView().getRwHoverableListEntry();
		hoverPanel.setVisible(false);
		hoverPanel.add(element.getHoverable());
		
		
		final FocusPanel fp = getView().getRwListEntry();

		// Switch hover panel on mouse over
		fp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				hoverPanel.setVisible(true);
				fp.setStyleName("rwListEntry-hover");
			}
		});
		
		fp.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				hoverPanel.setVisible(false);
				fp.setStyleName("rwListEntry");
			}
		});
	}
	
}
