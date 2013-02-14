package net.relatedwork.client.staticpresenter;

import com.gwtplatform.mvp.client.PopupViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;

public class NotYetImplementedView extends PopupViewImpl implements
		NotYetImplementedPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NotYetImplementedView> {
	}

	@Inject
	public NotYetImplementedView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
