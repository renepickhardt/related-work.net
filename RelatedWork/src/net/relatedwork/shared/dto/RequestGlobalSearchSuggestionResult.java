package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import com.google.gwt.user.client.ui.SuggestOracle.Response;

public class RequestGlobalSearchSuggestionResult implements Result {

	private Response response;

	@SuppressWarnings("unused")
	private RequestGlobalSearchSuggestionResult() {
		// For serialization only
	}

	public RequestGlobalSearchSuggestionResult(Response response) {
		this.response = response;
	}

	public Response getResponse() {
		return response;
	}
}
