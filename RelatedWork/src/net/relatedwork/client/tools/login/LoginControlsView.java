package net.relatedwork.client.tools.login;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class LoginControlsView extends ViewImpl implements
		LoginControlsPresenter.MyView {

	private final Widget widget;

	@UiField Anchor rwLoginLink;
	@UiField Anchor rwLogoutLink;
	@UiField Label rwLoginStatus;

	
	public Anchor getRwLogoutLink() {
		return rwLogoutLink;
	}

	public Label getLoginStatus(){
		return rwLoginStatus;
	}

	public Anchor getRwLoginLink() {
		return rwLoginLink;
	}

	public void setRwLoginLink(Anchor rwLoginLink) {
		this.rwLoginLink = rwLoginLink;
	}

	
	public interface Binder extends UiBinder<Widget, LoginControlsView> {
	}

	@Inject
	public LoginControlsView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void hideLoginLink() {
		rwLoginLink.setVisible(false);
		rwLogoutLink.setVisible(true);
	}

	public void hideLogoutLink() {
		rwLoginLink.setVisible(true);
		rwLogoutLink.setVisible(false);
	}
	
	
}
