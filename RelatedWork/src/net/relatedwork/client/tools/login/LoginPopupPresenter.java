package net.relatedwork.client.tools.login;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PopupView;
import com.gwtplatform.mvp.client.PresenterWidget;
import net.relatedwork.client.tools.events.ClearPopupsEvent;
import net.relatedwork.client.tools.events.ClearPopupsEvent.ClearPopupsHandler;
import net.relatedwork.client.tools.events.LoginEvent;
import net.relatedwork.client.tools.session.SessionInformationManager;
import net.relatedwork.shared.dto.LoginAction;
import net.relatedwork.shared.dto.LoginActionResult;

public class LoginPopupPresenter extends
		PresenterWidget<LoginPopupPresenter.MyView> {

	public interface MyView extends PopupView {
		public TextBox getRwLoginEmail();
		public void setRwLoginEmail(TextBox rwLoginUsername);
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
    @Inject SessionInformationManager sessionInformationManager;


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
										// email
										getView().getRwLoginEmail().getText(),
										// hash password for security reasons
										Integer.toString(getView().getRwLoginPassword().getText().hashCode()),
										// session info
										sessionInformationManager.get()
										),
								new AsyncCallback<LoginActionResult>() {
									@Override
									public void onFailure(Throwable caught) {
										// TODO Auto-generated method stub
									}

									@Override
									public void onSuccess(LoginActionResult result) {
										// LoginScucessfull
										// fire LoginEvent
										getEventBus().fireEvent(new LoginEvent(result.getSession()));

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
