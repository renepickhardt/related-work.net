package net.relatedwork.client.staticpresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class DataView extends ViewImpl implements DataPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, DataView> {
	}

	@Inject
	public DataView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
