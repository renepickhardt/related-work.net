package net.relatedwork.client.tools;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ListElementView extends ViewImpl implements
		ListElementPresenter.MyView {

	private final Widget widget;
	
	@UiField FocusPanel rwListElement;

	public FocusPanel getRwListElement() {
		return rwListElement;
	}

	public void setRwListElement(FocusPanel rwListElement) {
		this.rwListElement = rwListElement;
	}

	public interface Binder extends UiBinder<Widget, ListElementView> {
	}

	@Inject
	public ListElementView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
