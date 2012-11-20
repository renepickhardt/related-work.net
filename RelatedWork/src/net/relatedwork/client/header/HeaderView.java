package net.relatedwork.client.header;

import java.util.ArrayList;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.RequestGlobalSearchSuggestionResult;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;
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

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwBreadcrumbs;
	@UiField HTMLPanel rwHeaderSearch;
	
	// Search button and and box
	private final Button reSearch;
	private final SuggestBox suggestBox;

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
		SuggestOracle oracle = new SuggestOracle(){
			@Override
			public void requestSuggestions(final Request request,final Callback callback) {
				dispatcher.execute(new RequestGlobalSearchSuggestion(request), new AsyncCallback<RequestGlobalSearchSuggestionResult>(){
					@Override
					public void onFailure(Throwable caught) {

					}
					@Override
					public void onSuccess(
							RequestGlobalSearchSuggestionResult result) {
						ArrayList<ItemSuggestion> list = new ArrayList<ItemSuggestion>();
						for (Suggestion sug:result.getResponse().getSuggestions()){
							if (((ItemSuggestion)sug).prepareForDisplay()==false)continue;
							list.add((ItemSuggestion)sug);
						}
						Response r = new Response();
						r.setSuggestions(list);
						callback.onSuggestionsReady(request, r);
					}});
			}};
		
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
		
}
