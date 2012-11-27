package net.relatedwork.client.content;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class PaperView extends ViewImpl implements PaperPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, PaperView> {
	}

	@Inject
	public PaperView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
