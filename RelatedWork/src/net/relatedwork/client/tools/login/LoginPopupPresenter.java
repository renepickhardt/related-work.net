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
		public void clearFields();
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

		// Click on LoginButton:
		registerHandler(getView().getRwLoginButton().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						dispatchAsync.execute(
								new LoginAction(
								// username
										getView().getRwLoginUsername().getText(),
										// hash password for security reasons
										Integer.toString(getView().getRwLoginPassword().getText().hashCode()),
										// session info
										MainPresenter.getSessionInformation()
										),
								new AsyncCallback<LoginActionResult>() {
									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
									}

									@Override
									public void onSuccess(
											LoginActionResult result) {
										// LoginScucessfull
										// fire LoginEvent
										LoginPopupPresenter.this.eventBus.fireEvent(new LoginEvent(result.getSession()));

										// Clear username/pw from login popup
										getView().clearFields();
										
										// hide Popups
										getView().hide();
									}});
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

}
