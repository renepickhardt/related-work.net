package net.relatedwork.client.layout;

import java.util.ArrayList;

import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestionResult;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwBreadcrumbs;
	@UiField HTMLPanel rwHeaderSearch;
	@UiField Anchor rwLoginLink;
	@UiField Label rwLoginStatus;
	
	public Label getLoginStatus(){
		return rwLoginStatus;
	}
	
	
	// Search button and and box
	private final Button reSearch;
	private final SuggestBox suggestBox;

	// Geters and Setters
	public Anchor getRwLoginLink() {
		return rwLoginLink;
	}

	public void setRwLoginLink(Anchor rwLoginLink) {
		this.rwLoginLink = rwLoginLink;
	}

	public HTMLPanel getRwBreadcrumbs() {
		return rwBreadcrumbs;
	}

	public void setRwBreadcrumbs(HTMLPanel rwBreadcrumbs) {
		this.rwBreadcrumbs = rwBreadcrumbs;
	}

	public HTMLPanel getRwHeaderSearch() {
		return rwHeaderSearch;
	}

	public void setRwHeaderSearch(HTMLPanel rwHeaderSearch) {
		this.rwHeaderSearch = rwHeaderSearch;
	}

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}
	
	
	// Auto Complete Box 
	@Inject DispatchAsync dispatcher;
	
	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);
//		SuggestOracle oracle = new SuggestOracle(){
//			@Override
//			public void requestSuggestions(final Request request,final Callback callback) {
//				dispatcher.execute(new RequestGlobalSearchSuggestion(request), new AsyncCallback<RequestGlobalSearchSuggestionResult>(){
//					@Override
//					public void onFailure(Throwable caught) {
//
//					}
//					@Override
//					public void onSuccess(
//							RequestGlobalSearchSuggestionResult result) {
//						ArrayList<ItemSuggestion> list = new ArrayList<ItemSuggestion>();
//						for (Suggestion sug:result.getResponse().getSuggestions()){
//							if (((ItemSuggestion)sug).prepareForDisplay()==false)continue;
//							list.add((ItemSuggestion)sug);
//						}
//						Response r = new Response();
//						r.setSuggestions(list);
//						callback.onSuggestionsReady(request, r);
//					}});
//			}};
		
		SuggestOracle oracle = getSuggestOracle();
		suggestBox = new SuggestBox(oracle);
		reSearch = new Button();
		reSearch.setText("(re)search");	
		rwHeaderSearch.add(suggestBox);
		rwHeaderSearch.add(reSearch);		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}


	public void setInSlot(Object slot, Widget content) {
		if (slot == HeaderPresenter.TYPE_Breadcrumbs){
			setBreadcrumbs(content);
		}
	}

	private void setBreadcrumbs(Widget content) {
		rwBreadcrumbs.clear();
		if (content != null) {
			rwBreadcrumbs.add(content);
		}
	}
	
	
		
	/**
	 * this function and logic should not be in the view. but i cant move it to the presenter
	 * reasons:
	 * 1.) suggestbox cant exchange it's oracle so I cant set it from the presenter
	 * 2.) cant to rendering in the view from the presenter
	 * 3.) can request data from the presenter in the view (the presenter provides data to the view via getters and setters)
	 * 4.) see at 1.) there are not getters and setters for suggest oracles in the suggest box
	 * 
	 * TODO: figure out how to solve it
	 * @return
	 */
	private SuggestOracle getSuggestOracle() {
		return new SuggestOracle(){
			
			private ItemSuggestion sug = new ItemSuggestion("requesting Suggestions...");
			
			@Override
			public void requestSuggestions(final Request request,
					final Callback callback) {
				//TODO: getLocallyCached personal suggestions
				Response r = new Response();
				final ArrayList<ItemSuggestion> local = new ArrayList<ItemSuggestion>();
				local.add(sug); 
				r.setSuggestions(local);
				callback.onSuggestionsReady(request, r);
				
				//callback.onSuggestionsReady(request, response
				//make rpc request!
				dispatcher.execute(
						new RequestGlobalSearchSuggestion(request),
						new AsyncCallback<RequestGlobalSearchSuggestionResult>() {
							@Override
							public void onFailure(Throwable caught) {
							}
							@Override
							public void onSuccess(
									RequestGlobalSearchSuggestionResult result) {
								ArrayList<ItemSuggestion> list = new ArrayList<ItemSuggestion>();
//								for (ItemSuggestion s:local){
//									list.add(s);
//								}
								for (Suggestion sug : result.getResponse()
										.getSuggestions()) {
									if (((ItemSuggestion) sug).prepareForDisplay() == false)
										continue;
									list.add((ItemSuggestion) sug);
								}
								Response r = new Response();
								r.setSuggestions(list);
								//TODO: Merge with local suggestions!
								callback.onSuggestionsReady(request, r);
							}
						});		
			}
		};
	}

	public SuggestBox getSuggestBox() {
		return suggestBox;
	}

	public Button getReSearch() {
		return reSearch;
	}
	
}
