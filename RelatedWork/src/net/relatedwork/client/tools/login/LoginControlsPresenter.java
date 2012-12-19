package net.relatedwork.client.tools.login;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.layout.HeaderPresenter;
import net.relatedwork.client.tools.events.LoginEvent;
import net.relatedwork.client.tools.events.LogoutEvent;
import net.relatedwork.client.tools.events.LoginEvent.LoginHandler;
import net.relatedwork.client.tools.events.LogoutEvent.LogoutHandler;
import net.relatedwork.client.tools.session.SessionInformation;

public class LoginControlsPresenter
		extends
		Presenter<LoginControlsPresenter.MyView, LoginControlsPresenter.MyProxy> {

	public interface MyView extends View {
		public Label getLoginStatus();
		public Anchor getRwLoginLink();
		public Anchor getRwLogoutLink();		
		public void setRwLoginLink(Anchor rwLoginLink);
		
		public void hideLoginLink();
		public void hideLogoutLink();
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
	protected void onReveal() {
		super.onReveal();
		updateLabel();
		// Logged out per default
		getView().hideLogoutLink();
		
	}
	
	@Override
	protected void onBind() {
		super.onBind();

		// Bind LoginPopup to LoginLink
		registerHandler(getView().getRwLoginLink().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						event.preventDefault(); // do not follow the link!
						addToPopupSlot(loginPopupPresenter);
					}	
				}));

		// Bind Logout Event
		registerHandler(getView().getRwLogoutLink().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						event.preventDefault(); // do not follow the link!
						getEventBus().fireEvent(new LogoutEvent());
//						Window.alert("Logout");
					}	
				}));

		
		// On LoginEvents update Label
		registerHandler(getEventBus().addHandler(LoginEvent.getType(), new LoginHandler() {
			
			@Override
			public void onLogin(LoginEvent event) {
				// Hide Login button and updatet Label
				getView().hideLoginLink();
				updateLabel();
			}
		}));
		
		// On LoginEvents update Label
		registerHandler(getEventBus().addHandler(LogoutEvent.getType(), new LogoutHandler(){
			@Override
			public void onLogout(LogoutEvent event) {
				getView().hideLogoutLink();
				updateLabel();
			}
		}));

	}

	
	private void updateLabel() {
		SessionInformation session = MainPresenter.getSessionInformation();
//		Window.alert("UpdatingLabel " + session.getSessionId());
		getView().getLoginStatus().setText(
				"Username:" + session.getUsername() + " -- " +
				"SessionId:" + session.getSessionId() );
	}
	
}
