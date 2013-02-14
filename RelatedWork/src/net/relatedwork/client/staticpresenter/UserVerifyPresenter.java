package net.relatedwork.client.staticpresenter;

import net.relatedwork.client.tools.session.SessionInformationManager;
import org.neo4j.graphdb.Node;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.tools.events.LoginEvent;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;
import net.relatedwork.shared.dto.UserVerifyAction;
import net.relatedwork.shared.dto.UserVerifyActionResult;

public class UserVerifyPresenter extends
		Presenter<UserVerifyPresenter.MyView, UserVerifyPresenter.MyProxy> {

	public interface MyView extends View {
		public void showSuccess(String email);		
		public void showFailed(String email);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.userverify)
	public interface MyProxy extends ProxyPlace<UserVerifyPresenter> {
	}

    @Inject SessionInformationManager sessionInformationManager;

	@Inject
	public UserVerifyPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Inject DispatchAsync dispatcher;
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		String email = request.getParameter("email", "");
		String secret = request.getParameter("secret", "");

		Window.alert("sending request" + secret + " - " + email);
		
		dispatcher.execute(new UserVerifyAction(email, secret, sessionInformationManager.get()), new AsyncCallback<UserVerifyActionResult>() {

			@Override
			public void onFailure(Throwable caught) {
				getView().showFailed(caught.getMessage());
			}

			@Override
			public void onSuccess(UserVerifyActionResult result) {
				getView().showSuccess(
						result.getSession().emailAddress
						);
				
				getEventBus().fireEvent(new LoginEvent(result.getSession()));
			}
			
		});
			
	}


}

