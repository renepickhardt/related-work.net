package net.relatedwork.client.content;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SearchResultPageView extends ViewImpl implements
		SearchResultPagePresenter.MyView {

	private final Widget widget;
	@UiField HTMLPanel serpContainer;

	public interface Binder extends UiBinder<Widget, SearchResultPageView> {
	}

	@Inject
	public SearchResultPageView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HTMLPanel getSerpContainer() {
		return serpContainer;
	}

	@Override
	public void addResult(Hyperlink result) {
		serpContainer.add(result);
	}

	@Override
	public void addResultElement(HorizontalPanel element) {
		serpContainer.add(element);
	}
}
