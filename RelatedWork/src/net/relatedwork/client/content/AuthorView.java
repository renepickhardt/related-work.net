package net.relatedwork.client.content;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AuthorView extends ViewImpl implements AuthorPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, AuthorView> {
	}

	@Inject
	public AuthorView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
