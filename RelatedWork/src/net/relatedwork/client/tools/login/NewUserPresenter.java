package net.relatedwork.client.tools.login;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

public class NewUserPresenter extends PresenterWidget<NewUserPresenter.MyView> {

	public interface MyView extends PopupView {
		public void setRwNewUsername(TextBox rwNewUsername);
		public TextBox getRwNewEmail();
		public void setRwNewEmail(TextBox rwNewEmail);
		public TextBox getRwNewPassword();
		public void setRwNewPassword(TextBox rwNewPassword);
		public TextBox getRwNewPasswordRepeat();
		public void setRwNewPasswordRepeat(TextBox rwNewPasswordRepeat);
		public Button getRwNewUserSubmit();
		public void setRwNewUserSubmit(Button rwNewUserSubmit);
	}

	@Inject
	public NewUserPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
