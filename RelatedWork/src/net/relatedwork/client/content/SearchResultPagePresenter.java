package net.relatedwork.client.content;

import java.util.ArrayList;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.client.place.NameTokens;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;
import net.relatedwork.shared.dto.Renderable;

public class SearchResultPagePresenter
		extends
		Presenter<SearchResultPagePresenter.MyView, SearchResultPagePresenter.MyProxy> {

	public interface MyView extends View {
		public HTMLPanel getSerpContainer();
		public void addResult(Hyperlink result);
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.serp)
	public interface MyProxy extends ProxyPlace<SearchResultPagePresenter> {
	}

	@Inject
	public SearchResultPagePresenter(final EventBus eventBus, final MyView view,
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
	 @Override
	protected void onReveal() {
		// TODO Auto-generated method stub
		super.onReveal();
	}

	 @Inject DispatchAsync dispatcher;
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		// TODO Auto-generated method stub
		super.prepareFromRequest(request);
		String query = request.getParameter("q", "Bridgeland");
		dispatcher.execute(new GlobalSearch(query), new AsyncCallback<GlobalSearchResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}	
			@Override
			public void onSuccess(GlobalSearchResult result) {
				setResults(result.getSearchResults());
			}
		});
	}
	public void setResults(ArrayList<Renderable> searchResults) {
		getView().getSerpContainer().clear();
		
		for (Renderable r:searchResults){
			//getView().getSerpContainer().add(r.getLink());
			getView().addResult(r.getLink());
		}
		
	}
	
}
