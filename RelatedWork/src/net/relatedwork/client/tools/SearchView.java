package net.relatedwork.client.tools;

import java.util.ArrayList;

import net.relatedwork.shared.DisplayableItemSuggestion;
import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.SuggestProtocol;
import net.relatedwork.shared.SuggestTree;
import net.relatedwork.shared.SuggestTree.SuggestionList;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestionResult;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestion;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestionResult;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.storage.client.Storage;
import com.google.gwt.storage.client.StorageMap;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.inject.Inject;
import com.google.web.bindery.requestfactory.shared.RequestFactory;

//TODO: nice data structure but it forgets the ranking values. After runtime tests are alright extend data structure to remember doubles within suggestionlist
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;


public class SearchView extends ViewImpl implements SearchPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel searchContainer;
	// Auto Complete Box 
	@Inject DispatchAsync dispatcher;
	
	// Search button and and box
	private final Button reSearch;
	private final SuggestBox suggestBox;
	
	public interface Binder extends UiBinder<Widget, SearchView> {
	}

	@Inject
	public SearchView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		
		SuggestOracle oracle = getSuggestOracle();
		suggestBox = new SuggestBox(oracle);
		reSearch = new Button();
		reSearch.setText("(re)search");	
		searchContainer.add(suggestBox);
		searchContainer.add(reSearch);		

	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HTMLPanel getSearchContainer() {
		return searchContainer;
	}
	
	public void setSearchContainer(HTMLPanel searchContainer) {
		this.searchContainer = searchContainer;
	}

//	private buildLocalSuggestTree(){
//		
//	}
	
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
			boolean flag=false;			
			SuggestTree<Integer> tree;
			
			@Override
			public void requestSuggestions(final Request request,
					final Callback callback) {
				final Response r = new Response();
				final ArrayList<DisplayableItemSuggestion> local = new ArrayList<DisplayableItemSuggestion>();

				request.setQuery(request.getQuery().toLowerCase());
				
				if (flag==false){
					  tree = new SuggestTree<Integer>(3,new Comparator<Integer>(){
							@Override
							public int compare(Integer o1, Integer o2) {
								return -o1.compareTo(o2);
							}});
					  Storage locStore = Storage.getLocalStorageIfSupported();
					  String tmp = locStore.getItem("suggestMap");
					  if (false){/*tmp!=null){
						  tree.build(SuggestProtocol.deSerializeHashMap(tmp));
						  flag = true;*/
					  }
					  else {
						dispatcher.execute(new RequestLocalSearchSuggestion(), new AsyncCallback<RequestLocalSearchSuggestionResult>() {

							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}

							@Override
							public void onSuccess(RequestLocalSearchSuggestionResult result) {
								// TODO Auto-generated method stub
								HashMap<String,Integer> map = new HashMap<String, Integer>();
								HashMap<String, Integer> tmp;
								tmp = result.getLocalAuthorSuggestions();
								for (String key:tmp.keySet()){
									ArrayList<String> out = SuggestProtocol.getSuggestTreeAuthor(key);
									for (String s:out){
										map.put(s, tmp.get(key));
									}
								}
								tmp = result.getLocalPaperSuggestions();
								for (String key:tmp.keySet()){
									ArrayList<String> out = SuggestProtocol.getSuggestTreePaper(key);
									for (String s:out){
										map.put(s, tmp.get(key));
									}
								}

								//TODO: invoking webstore seems not to work!
								Storage s = Storage.getLocalStorageIfSupported();
								s.setItem("suggestMap", SuggestProtocol.serializeMap(map));
								
								
								tree.build(map);
								flag = true;
							}
						}); 									
					  }
				}
				
				SuggestionList list = tree.getBestSuggestions(request.getQuery());						
				for (int i=0;i< list.length();i++){
					local.add(new DisplayableItemSuggestion(list.get(i), request.getQuery(),true));
				}
				r.setSuggestions(local);
				callback.onSuggestionsReady(request, r);

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
								for (Suggestion sug : result.getResponse()
										.getSuggestions()) {
									local.add(new DisplayableItemSuggestion(sug.getDisplayString(),request.getQuery(),false));
								}
								r.setSuggestions(local);
								callback.onSuggestionsReady(request, r);
							}
						});		
			}

			@Override
			public boolean isDisplayStringHTML() {
				// TODO Auto-generated method stub
				return true;
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
