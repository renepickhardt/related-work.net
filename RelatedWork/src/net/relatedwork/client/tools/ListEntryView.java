package net.relatedwork.client.tools;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ListEntryView extends ViewImpl implements
		ListEntryPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwVisibleListEntry;
	@UiField HTMLPanel rwHoverableListEntry;
	@UiField FocusPanel rwListEntry;

	
	public interface Binder extends UiBinder<Widget, ListEntryView> {
	}

	@Inject
	public ListEntryView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HTMLPanel getRwVisibleListEntry() {
		return rwVisibleListEntry;
	}

	public void setRwVisibleListEntry(HTMLPanel rwVisibleListEntry) {
		this.rwVisibleListEntry = rwVisibleListEntry;
	}

	public HTMLPanel getRwHoverableListEntry() {
		return rwHoverableListEntry;
	}

	public void setRwHoverableListEntry(HTMLPanel rwHoverableListEntry) {
		this.rwHoverableListEntry = rwHoverableListEntry;
	}

	public FocusPanel getRwListEntry() {
		return rwListEntry;
	}

	public void setRwListEntry(FocusPanel rwListEntry) {
		this.rwListEntry = rwListEntry;
	}
	
	@Override
	public void setHoverableVisibility(boolean isVisible) {
		getRwHoverableListEntry().setVisible(isVisible);
	}

	
}
