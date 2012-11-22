package net.relatedwork.server;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.SuggestTree;
import net.relatedwork.shared.SuggestTree.SuggestionList;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestionResult;

import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class RequestGlobalSearchSuggestionActionHandler
		implements
		ActionHandler<RequestGlobalSearchSuggestion, RequestGlobalSearchSuggestionResult> {

	@Inject ServletContext servletContext;

	@Inject
	public RequestGlobalSearchSuggestionActionHandler() {
	}

	@Override
	public RequestGlobalSearchSuggestionResult execute(
			RequestGlobalSearchSuggestion action, ExecutionContext context)
			throws ActionException {
		SuggestTree<Integer> tree = ContextHelper.getSuggestTree(servletContext);
		SuggestionList list = tree.getBestSuggestions(action.getRequest().getQuery());
		ArrayList<ItemSuggestion> suggestions = new ArrayList<ItemSuggestion>();
		for (int i=0;i<list.length();i++){
			suggestions.add(new ItemSuggestion(list.get(i)));			
		}
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
