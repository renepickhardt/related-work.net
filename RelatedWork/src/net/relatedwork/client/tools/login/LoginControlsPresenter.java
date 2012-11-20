package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.layout.HeaderPresenter;
import net.relatedwork.client.tools.login.LoginEvent.LoginHandler;
import net.relatedwork.client.tools.session.RegisterSesssionAction;
import net.relatedwork.client.tools.session.RegisterSesssionActionResult;

public class LoginControlsPresenter
		extends
		Presenter<LoginControlsPresenter.MyView, LoginControlsPresenter.MyProxy> {

	public interface MyView extends View {
		public Label getLoginStatus();
		public Anchor getRwLoginLink();
		public void setRwLoginLink(Anchor rwLoginLink);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<LoginControlsPresenter> {
	}

	@Inject
	public LoginControlsPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, HeaderPresenter.TYPE_LoginControls, this);
	}

	@Inject LoginPopupPresenter loginPopupPresenter;
	@Inject DispatchAsync dispatcher;

	@Override
	protected void onBind() {
		super.onBind();

		// Bind LoginPopup to ClickEvent 
		registerHandler(getView().getRwLoginLink().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						event.preventDefault(); // do not follow the link!
						addToPopupSlot(loginPopupPresenter);
					}	
				}));

		// listen to LoginEvents
		registerHandler(getEventBus().addHandler(LoginEvent.getType(), loginHandler));

	}

	@Override
	protected void onReveal() {
		super.onReveal();

		// Register session Fails with null exceptions for now reason!!
		RegisterSesssionAction action = new RegisterSesssionAction("agra");
		dispatcher.execute(action, sessionCallback);

	}
	
	private LoginHandler loginHandler = new LoginHandler() {
		@Override
		public void onLogin(LoginEvent event) {
			// TODO Auto-generated method stub
			String username = event.getUserInformation().getUsername();
			getView().getLoginStatus().setText("Logged in as " + username);
		}
	};

	
	/**
	 * Session Management
	 */	
	
	private AsyncCallback<RegisterSesssionActionResult> sessionCallback = new AsyncCallback<RegisterSesssionActionResult>(){

		@Override
		public void onFailure(Throwable caught) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onSuccess(RegisterSesssionActionResult result) {
			Window.alert("Called back!");
			// TODO Auto-generated method stub
		}};	

	
	
}
