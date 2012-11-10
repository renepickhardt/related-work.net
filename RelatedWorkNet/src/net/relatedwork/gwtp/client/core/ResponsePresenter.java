package net.relatedwork.gwtp.client.core;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.gwtp.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import net.relatedwork.gwtp.shared.SendTextToServer;
import net.relatedwork.gwtp.shared.SendTextToServerResult;

public class ResponsePresenter extends
		Presenter<ResponsePresenter.MyView, ResponsePresenter.MyProxy> {

	public interface MyView extends View {

		HasClickHandlers getCloseClickHandlers();

		void setServerResponse(String serverResponse);

		void setTextToServer(String textToServer);
	}

	@ProxyStandard
	@NameToken(NameTokens.response)
	public interface MyProxy extends ProxyPlace<ResponsePresenter> {
	}

	public static final String textToServerParam = "textToServer";

	private final DispatchAsync dispatcher;

	private final PlaceManager placeManager;

	private String textToServer;
	private String method;

	@Inject
	public ResponsePresenter(final EventBus eventBus, final MyView view,
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
		textToServer = request.getParameter(textToServerParam, null);
		method = request.getParameter("method", "not set (impossible)");
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().getCloseClickHandlers().addClickHandler(
				new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						placeManager.revealPlace(new PlaceRequest(NameTokens.main));
					}
				}));
	}

	@Override
	protected void onReset() {
		super.onReset();
		getView().setTextToServer(textToServer);
		getView().setServerResponse("Waiting for response...");
		dispatcher.execute(new SendTextToServer(textToServer,method),
				new AsyncCallback<SendTextToServerResult>() {
					@Override
					public void onFailure(Throwable caught) {
						getView().setServerResponse(
								"An error occured: " + caught.getMessage());
					}
	
					@Override
					public void onSuccess(SendTextToServerResult result) {
						getView().setServerResponse(result.getResponse());
					}
				});
	}
}
