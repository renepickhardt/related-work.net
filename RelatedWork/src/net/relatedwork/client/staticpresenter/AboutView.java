package net.relatedwork.client.staticpresenter;

import net.relatedwork.client.MainPresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AboutView extends ViewImpl implements AboutPresenter.MyView {

	private final Widget widget;
	
	public interface Binder extends UiBinder<Widget, AboutView> {
	}

	@Inject
	public AboutView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
