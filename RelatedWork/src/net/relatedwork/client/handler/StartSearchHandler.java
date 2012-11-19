package net.relatedwork.client.handler;

import net.relatedwork.client.header.HeaderPresenter;
import net.relatedwork.client.header.HeaderPresenter.MyView;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

public class StartSearchHandler implements ClickHandler, KeyUpHandler {
	
	private final MyView view;
	private final DispatchAsync dispatcher;
	
	public StartSearchHandler(MyView view, DispatchAsync dispatcher) {
		this.view = view;
		this.dispatcher = dispatcher;
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
		view.getSuggestBox().setText("");
	}

	@Inject PlaceManager placeManager;
	
	private void doSearch() {
		String query = view.getSuggestBox().getText();
//		dispatcher.execute(new GlobalSearch(query), new AsyncCallback<GlobalSearchResult>() {
//			@Override
//			public void onFailure(Throwable caught) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onSuccess(GlobalSearchResult result) {
//				PlaceRequest myRequest = new PlaceRequest(NameTokens.author);
//				// If needed, add URL parameters in this way:
//				myRequest = myRequest.with( "q", "Bridgeland" );
//				placeManager.revealPlace( myRequest ); 
//				
//			}
//		});
		
		PlaceRequest myRequest = new PlaceRequest(NameTokens.author);
		// If needed, add URL parameters in this way:
		myRequest = myRequest.with( "q", "Bridgeland" );
		placeManager.revealPlace( myRequest ); 

//		Window.alert(query);
	}
}
