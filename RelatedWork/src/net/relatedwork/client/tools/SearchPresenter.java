package net.relatedwork.client.tools;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;

import net.relatedwork.client.content.SearchResultPagePresenter;
import net.relatedwork.client.handler.StartSearchHandler;
import net.relatedwork.client.layout.HeaderPresenter;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;

public class SearchPresenter extends
		Presenter<SearchPresenter.MyView, SearchPresenter.MyProxy> {

	@Inject DispatchAsync dispatcher;
	@Inject PlaceManager placeManager;

	
	public interface MyView extends View {
		public HTMLPanel getSearchContainer();
		public void setSearchContainer(HTMLPanel searchContainer);
		public SuggestBox getSuggestBox();
		public Button getReSearch();
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<SearchPresenter> {
	}

	@Inject
	public SearchPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, HeaderPresenter.TYPE_Search, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getView().getReSearch().addClickHandler(new StartSearchHandler()));
		registerHandler(getView().getSuggestBox().addKeyUpHandler(new StartSearchHandler()));
	}	
	
	public class StartSearchHandler implements ClickHandler, KeyUpHandler {
				
		public StartSearchHandler() {
		}

		@Override
		public void onClick(ClickEvent event) {
			doSearch();
		}

		@Override
		public void onKeyUp(KeyUpEvent event) {
			if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
				doSearch();
			}
			if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE){
				resetSuggestBox();
			}
		}
		
		private void resetSuggestBox() {
			//TODO: discuss! I think this is bad practise rather a method in view saying clearSuggestBox() should be implemented for unit testing
			getView().getSuggestBox().setText("");
		}

		
		private void doSearch() {
			final String query = getView().getSuggestBox().getText();
			PlaceRequest myRequest = new PlaceRequest(NameTokens.serp);
			myRequest = myRequest.with( "q", query );
			placeManager.revealPlace( myRequest );
		}
	}
	
}
