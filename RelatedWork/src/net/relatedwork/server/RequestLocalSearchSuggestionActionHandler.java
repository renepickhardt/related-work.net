package net.relatedwork.server;

import java.util.HashMap;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestion;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestionResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class RequestLocalSearchSuggestionActionHandler
		implements
		ActionHandler<RequestLocalSearchSuggestion, RequestLocalSearchSuggestionResult> {

	@Inject
	public RequestLocalSearchSuggestionActionHandler() {
	}

	@Override
	public RequestLocalSearchSuggestionResult execute(
			RequestLocalSearchSuggestion action, ExecutionContext context)
			throws ActionException {
		
		HashMap<String,Integer> map = new HashMap<String, Integer>();
		map.put("helmut", 2);
		map.put("heinrich", 3);
		map.put("hendrik", 4);
		map.put("herbert", 5);
		map.put("heino", 1);

		RequestLocalSearchSuggestionResult result = new RequestLocalSearchSuggestionResult(map);
		return result;
	}

	@Override
	public void undo(RequestLocalSearchSuggestion action,
			RequestLocalSearchSuggestionResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<RequestLocalSearchSuggestion> getActionType() {
		return RequestLocalSearchSuggestion.class;
	}
}
