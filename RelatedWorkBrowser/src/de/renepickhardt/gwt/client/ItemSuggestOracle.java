package de.renepickhardt.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;

import de.renepickhardt.gwt.shared.ItemSuggestion;

public class ItemSuggestOracle extends SuggestOracle {
	
	public final HashMap<String, Response> cache;
	
	public ItemSuggestOracle() {
		cache = new HashMap<String, Response>();
	}
	
	@Override
	public boolean isDisplayStringHTML() {
		return true;
	}
	
	@Override
	public void requestSuggestions(Request request, Callback callback) {
		
		if (cache.containsKey(request.getQuery())){
			callback.onSuggestionsReady(request, cache.get(request.getQuery()));
		}
		else {
			GreetingServiceAsync instance = (GreetingServiceAsync)GWT.create(GreetingService.class);
			instance.getSuggestions(request, new ItemSuggestCallback(request, callback));
		}
	}
	
	public class ItemSuggestCallback implements AsyncCallback {

		private Request request;
		private Callback callback;
		
		public ItemSuggestCallback(Request request, Callback callback) {
			this.request = request;
			this.callback = callback;
		}

		@Override
		public void onFailure(Throwable caught) {
			callback.onSuggestionsReady(request, new Response());

		}

		@Override
		public void onSuccess(Object result) {
			Response resp = (Response)result;
			
			ArrayList<ItemSuggestion> niceResult = new ArrayList<ItemSuggestion>();
			for (Suggestion sug:resp.getSuggestions()){
				String string = ((ItemSuggestion)sug).getDisplayString();
				string = string.replaceFirst(request.getQuery(), "<b>" +request.getQuery()+ "</b>");
				niceResult.add(new ItemSuggestion(string));
			}
			
			resp.setSuggestions(niceResult);
			
			cache.put(request.getQuery(), resp);
			callback.onSuggestionsReady(request, resp);
		}
	};

}
