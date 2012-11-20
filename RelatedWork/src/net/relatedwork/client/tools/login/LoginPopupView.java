package net.relatedwork.client.tools.login;

import com.gwtplatform.mvp.client.PopupViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class LoginPopupView extends PopupViewImpl implements
		LoginPopupPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, LoginPopupView> {
	}

	@UiField TextBox rwLoginUsername;
	@UiField TextBox rwLoginPassword;
	@UiField Button rwLoginButton;
	@UiField Button rwLoginNewUserButton;
	

	@Inject
	public LoginPopupView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public Button getRwNewUserButton() {
		return rwLoginNewUserButton;
	}

	public void setRwNewUserButton(Button rwNewUserButton) {
		this.rwLoginNewUserButton = rwNewUserButton;
	}

	public TextBox getRwLoginUsername() {
		return rwLoginUsername;
	}

	public void setRwLoginUsername(TextBox rwLoginUsername) {
		this.rwLoginUsername = rwLoginUsername;
	}

	public TextBox getRwLoginPassword() {
		return rwLoginPassword;
	}

	public void setRwLoginPassword(TextBox rwLoginPassword) {
		this.rwLoginPassword = rwLoginPassword;
	}

	public Button getRwLoginButton() {
		return rwLoginButton;
	}

	public void setRwLoginButton(Button rwLoginButton) {
		this.rwLoginButton = rwLoginButton;
	}
	
	
}
