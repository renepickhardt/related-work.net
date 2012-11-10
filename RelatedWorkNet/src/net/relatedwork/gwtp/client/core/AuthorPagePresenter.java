package net.relatedwork.gwtp.client.core;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.gwtp.client.place.NameTokens;
import net.relatedwork.gwtp.shared.DisplayAuthor;
import net.relatedwork.gwtp.shared.DisplayAuthorResult;
import net.relatedwork.gwtp.shared.SendTextToServer;
import net.relatedwork.gwtp.shared.SendTextToServerResult;

import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class AuthorPagePresenter extends
		Presenter<AuthorPagePresenter.MyView, AuthorPagePresenter.MyProxy> {

	public interface MyView extends View {
		void setResult(String result);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.author)
	public interface MyProxy extends ProxyPlace<AuthorPagePresenter> {
	}
	private String authorKey;
	private final PlaceManager placeManager;
	private final DispatchAsync dispatcher;

	@Inject
	public AuthorPagePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy, final DispatchAsync dispatcher, final PlaceManager placeManager) {
		super(eventBus, view, proxy);

		this.dispatcher = dispatcher;
		this.placeManager = placeManager;

	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		authorKey = request.getParameter("key", null);
	}

	
	@Override
	protected void onBind() {
		super.onBind();

	}
	
	protected void onReset() {
	super.onReset();
	dispatcher.execute(new DisplayAuthor(authorKey),
			new AsyncCallback<DisplayAuthorResult>() {
				@Override
				public void onFailure(Throwable caught) {
					getView().setResult("ERROR in RPC call");
				}

				@Override
				public void onSuccess(DisplayAuthorResult result) {
					getView().setResult(result.getResult());
					//getView().setServerResponse(result.getResponse());
				}
			});
	}
}
