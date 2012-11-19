package net.relatedwork.client;

import java.util.ArrayList;

import org.mortbay.jetty.servlet.Dispatcher;

import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.RequestGlobalSearchSuggestionResult;

import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainView extends ViewImpl implements MainPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwHeader;
	@UiField HTMLPanel rwContent;
	@UiField HTMLPanel rwSidebar;
	@UiField HTMLPanel rwFooter;
	@UiField HTMLPanel rwBreadcrumbs;
	@UiField HTMLPanel rwHeaderSearch;
	@UiField HTMLPanel rwDiscussions;
	
	private final SuggestBox suggestBox;
	private final Button reSearch;
	
	public HTMLPanel getRwHeader() {
		return rwHeader;
	}

	public void setRwHeader(HTMLPanel rwHeader) {
		this.rwHeader = rwHeader;
	}

	public HTMLPanel getRwContent() {
		return rwContent;
	}

	public void setRwContent(HTMLPanel rwContent) {
		this.rwContent = rwContent;
	}

	public HTMLPanel getRwSidebar() {
		return rwSidebar;
	}

	public void setRwSidebar(HTMLPanel rwSidebar) {
		this.rwSidebar = rwSidebar;
	}

	public HTMLPanel getRwFooter() {
		return rwFooter;
	}

	public void setRwFooter(HTMLPanel rwFooter) {
		this.rwFooter = rwFooter;
	}

	public interface Binder extends UiBinder<Widget, MainView> {
	}

	@Inject DispatchAsync dispatcher;
	
	@Inject
	public MainView(final Binder binder) {		
		widget = binder.createAndBindUi(this);
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

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPresenter.TYPE_Footer){
			setFooter(content);			
		}
		else if (slot == MainPresenter.TYPE_SetMainContent) {
			setMainContent(content);
		} 
		else if (slot == MainPresenter.TYPE_Discussion) {
			setDiscussions(content);
		} 
		else if (slot == MainPresenter.TYPE_Breadcrumbs) {
			setBreadcrumbs(content);
		} 
		else {
			super.setInSlot(slot, content);
		}
	}

	private void setDiscussions(Widget content) {
		rwDiscussions.clear();
		if (content != null) {
			rwDiscussions.add(content);
		}		
	}

	private void setBreadcrumbs(Widget content) {
		rwBreadcrumbs.clear();
		if (content != null) {
			rwBreadcrumbs.add(content);
		}
	}

	private void setFooter(Widget content) {
		rwFooter.clear();
		if (content != null) {
			rwFooter.add(content);
		}
	}

	private void setMainContent(Widget content) {
		rwContent.clear();
		if (content != null) {
			rwContent.add(content);
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