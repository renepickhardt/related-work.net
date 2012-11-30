package net.relatedwork.client.tools;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class TestView extends ViewImpl implements TestPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, TestView> {
	}

	@Inject
	public TestView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
