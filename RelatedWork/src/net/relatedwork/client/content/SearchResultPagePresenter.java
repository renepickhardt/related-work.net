package net.relatedwork.client.content;

import java.util.ArrayList;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.tools.events.LoadingOverlayEvent;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;
import net.relatedwork.shared.dto.Paper;
import net.relatedwork.shared.dto.Renderable;

public class SearchResultPagePresenter
		extends
		Presenter<SearchResultPagePresenter.MyView, SearchResultPagePresenter.MyProxy> {

	public interface MyView extends View {
		public HTMLPanel getSerpContainer();
		public void addResult(Hyperlink result);
		public void addResultElement(HorizontalPanel element);
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
		super.prepareFromRequest(request);
		
		getView().getSerpContainer().clear();
		
		String query = request.getParameter("q", "Bridgeland");
		
		// Log search query
		MainPresenter.getSessionInformation().logSearch(query);

		// show Loading Overlay
		getEventBus().fireEvent(new LoadingOverlayEvent(true));
		
		dispatcher.execute(new GlobalSearch(query), new AsyncCallback<GlobalSearchResult>() {
			@Override
			public void onSuccess(GlobalSearchResult result) {
				getEventBus().fireEvent(new LoadingOverlayEvent(false));
				setResults(result.getSearchResults());
			}

			@Override
			public void onFailure(Throwable caught) {
				getEventBus().fireEvent(new LoadingOverlayEvent(false));				
			}	
		});
	}
	
	public void setResults(ArrayList<IsRenderable> searchResults) {
		
		//getView().getSerpContainer().clear();
		
		for (IsRenderable r:searchResults){
			//getView().getSerpContainer().add(r.getLink());
			HorizontalPanel panel = new HorizontalPanel();
			Label authorLabel = new Label("Author: ");
			Label paperLabel = new Label("Paper: ");
			if (r instanceof Paper){
				Paper p = (Paper)r;
				panel.add(paperLabel);
				panel.add(p.getAuthorLink());
				getView().addResultElement(panel);
			}else if (r instanceof Author){
				Author a = (Author)r;
				panel.add(authorLabel);
				panel.add(a.getAuthorLink());
				getView().addResultElement(panel);
			}
		}	
	}
}
