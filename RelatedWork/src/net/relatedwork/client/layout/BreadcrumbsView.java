package net.relatedwork.client.layout;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class BreadcrumbsView extends ViewImpl implements
		BreadcrumbsPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel breadcrumbContainer;
	
	public HTMLPanel getBreadcrumbContainer() {
		return breadcrumbContainer;
	}

	public void setBreadcrumbContainer(HTMLPanel breadcrumbContainer) {
		this.breadcrumbContainer = breadcrumbContainer;
	}

	public interface Binder extends UiBinder<Widget, BreadcrumbsView> {
	}

	@Inject
	public BreadcrumbsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
