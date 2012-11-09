package net.relatedwork.gwtp.client.core;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AuthorPageView extends ViewImpl implements
		AuthorPagePresenter.MyView {

	private final HTMLPanel widget;

	public interface Binder extends UiBinder<Widget, AuthorPageView> {
	}
	
	@Inject
	public AuthorPageView(final Binder binder) {
		widget = (HTMLPanel) binder.createAndBindUi(this);
		HTML html = new HTML("<h1>More to come soon</h1>");
		widget.add(html);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
}
