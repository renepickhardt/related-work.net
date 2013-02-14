package net.relatedwork.client.tools.login;

import com.gwtplatform.mvp.client.PopupViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.web.bindery.event.shared.EventBus;
import com.google.inject.Inject;

public class NewUserView extends PopupViewImpl implements
		NewUserPresenter.MyView {

	private final Widget widget;

	public interface Binder extends UiBinder<Widget, NewUserView> {
	}

	@Inject
	public NewUserView(final EventBus eventBus, final Binder binder) {
		super(eventBus);
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@UiField TextBox rwNewUsername;
	@UiField TextBox rwNewEmail;
	@UiField TextBox rwNewPassword;
	@UiField TextBox rwNewPasswordRepeat;
	@UiField Button  rwNewUserSubmit;
	public TextBox getRwNewUsername() {
		return rwNewUsername;
	}

	public void clearFields() {
		rwNewUsername.setText("");
		rwNewEmail.setText("");
		rwNewPassword.setText("");
		rwNewPasswordRepeat.setText("");
	}
	
	@Override
	public void mockPassword() {
		rwNewPasswordRepeat.setText("Not equal to password!");		
	}
	
	
	public void setRwNewUsername(TextBox rwNewUsername) {
		this.rwNewUsername = rwNewUsername;
	}

	public TextBox getRwNewEmail() {
		return rwNewEmail;
	}

	public void setRwNewEmail(TextBox rwNewEmail) {
		this.rwNewEmail = rwNewEmail;
	}

	public TextBox getRwNewPassword() {
		return rwNewPassword;
	}

	public void setRwNewPassword(TextBox rwNewPassword) {
		this.rwNewPassword = rwNewPassword;
	}

	public TextBox getRwNewPasswordRepeat() {
		return rwNewPasswordRepeat;
	}

	public void setRwNewPasswordRepeat(TextBox rwNewPasswordRepeat) {
		this.rwNewPasswordRepeat = rwNewPasswordRepeat;
	}

	public Button getRwNewUserSubmit() {
		return rwNewUserSubmit;
	}

	public void setRwNewUserSubmit(Button rwNewUserSubmit) {
		this.rwNewUserSubmit = rwNewUserSubmit;
	}
	
	
	
}
