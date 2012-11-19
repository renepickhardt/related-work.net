package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

public class LoginPopupPresenter extends
		PresenterWidget<LoginPopupPresenter.MyView> {

	public interface MyView extends PopupView {
		public TextBox getRwLoginUsername();
		public void setRwLoginUsername(TextBox rwLoginUsername);
		public TextBox getRwLoginPassword();
		public void setRwLoginPassword(TextBox rwLoginPassword);
		public Button getRwLoginButton();
		public void setRwLoginButton(Button rwLoginButton);
	}

	private EventBus eventBus;
	
	@Inject
	public LoginPopupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
		this.eventBus = eventBus;
	}

	@Inject DispatchAsync dispatchAsync;
	
	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().getRwLoginButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				LoginAction action = new LoginAction(
						getView().getRwLoginUsername().getText(), 
						getView().getRwLoginPassword().getText()
						);
				
				dispatchAsync.execute(action, getLoginCallback);
			}
		}));
	}

	private AsyncCallback<UserInformation> getLoginCallback = new AsyncCallback<UserInformation>() {

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(UserInformation result) {
			LoginEvent loginEvent = new LoginEvent(result);
			LoginPopupPresenter.this.eventBus.fireEvent(loginEvent);
			getView().hide();
		}

	};

}
