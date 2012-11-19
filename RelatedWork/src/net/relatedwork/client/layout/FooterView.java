package net.relatedwork.client.layout;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class FooterView extends ViewImpl implements FooterPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, FooterView> {
	}

	@Inject
	public FooterView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
