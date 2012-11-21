package net.relatedwork.client.tools.login;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.tools.events.ClearPopupsEvent;
import net.relatedwork.client.tools.events.ClearPopupsEvent.ClearPopupsHandler;

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
		public Button getRwNewUserButton();
		public void setRwNewUserButton(Button rwLoginButton);
	}

	private EventBus eventBus;
	
	@Inject
	public LoginPopupPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
		this.eventBus = eventBus;
	}

	@Inject DispatchAsync dispatchAsync;
	@Inject NewUserPresenter newUserPresenter;
	
	@Override
	protected void onBind() {
		super.onBind();

		// Click on Login Button
		registerHandler(getView().getRwLoginButton().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				LoginAction action = new LoginAction(
						//username
						getView().getRwLoginUsername().getText(), 
						// hash password for security reasons
						Integer.toString(getView().getRwLoginPassword().getText().hashCode()),
						// session info
						MainPresenter.getSessionInformation()
						);
				
				dispatchAsync.execute(action, getLoginCallback);
			}
		}));
		
		// Click on NewUser Button
		registerHandler(getView().getRwNewUserButton().addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				addToPopupSlot(newUserPresenter);
			}
		}));
		
		// Hide on ClearPopups Event
		registerHandler(getEventBus().addHandler(ClearPopupsEvent.getType(), new ClearPopupsHandler() {
			@Override
			public void onclearP(ClearPopupsEvent event) {
				// TODO Auto-generated method stub
				getView().hide();
			}
		}
		));

	}

	private AsyncCallback<LoginActionResult> getLoginCallback = new AsyncCallback<LoginActionResult>() {
		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(LoginActionResult result) {
			// LoginScucessfull:
			// Update sessionInformation
			MainPresenter.setSessionInformation(result.getSession());
			// fire LoginEvent
			LoginPopupPresenter.this.eventBus.fireEvent(new LoginEvent());
			// hide Popup window.
			getView().hide();
		}

	};

}
