package net.relatedwork.client.staticpresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ImprintView extends ViewImpl implements ImprintPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, ImprintView> {
	}

	@Inject
	public ImprintView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
