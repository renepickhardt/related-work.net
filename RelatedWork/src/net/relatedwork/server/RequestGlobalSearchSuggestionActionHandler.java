package net.relatedwork.server;

import java.util.ArrayList;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.RequestGlobalSearchSuggestionResult;

import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class RequestGlobalSearchSuggestionActionHandler
		implements
		ActionHandler<RequestGlobalSearchSuggestion, RequestGlobalSearchSuggestionResult> {

	@Inject
	public RequestGlobalSearchSuggestionActionHandler() {
	}

	@Override
	public RequestGlobalSearchSuggestionResult execute(
			RequestGlobalSearchSuggestion action, ExecutionContext context)
			throws ActionException {
		String q = action.getRequest().getQuery();
		ArrayList<ItemSuggestion> suggestions = new ArrayList<ItemSuggestion>();
		suggestions.add(new ItemSuggestion(q+"jas"));
		suggestions.add(new ItemSuggestion(q+"as"));
		suggestions.add(new ItemSuggestion(q+"bas"));
		Response resp = new Response();
		resp.setSuggestions(suggestions);
		return new RequestGlobalSearchSuggestionResult(resp);
	}

	@Override
	public void undo(RequestGlobalSearchSuggestion action,
			RequestGlobalSearchSuggestionResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<RequestGlobalSearchSuggestion> getActionType() {
		return RequestGlobalSearchSuggestion.class;
	}
}
