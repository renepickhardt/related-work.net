package net.relatedwork.gwtp.client.core;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class AuthorPageView extends ViewImpl implements
		AuthorPagePresenter.MyView {

	private final HTMLPanel widget;

	private static AuthorPageViewUiBinder uiBinder = GWT.create(AuthorPageViewUiBinder.class);
	interface AuthorPageViewUiBinder extends UiBinder<Widget, AuthorPageView> {}
	
	@Inject
	public AuthorPageView() {
		widget = (HTMLPanel) uiBinder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void setResult(String result){
		widget.clear();
		HTML html = new HTML(result);
		widget.add(html);
	}	
}