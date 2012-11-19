package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.shared.dto.RequestGlobalSearchSuggestionResult;

import com.google.gwt.user.client.ui.SuggestOracle.Request;

public class RequestGlobalSearchSuggestion extends
		UnsecuredActionImpl<RequestGlobalSearchSuggestionResult> {

	private Request request;

	@SuppressWarnings("unused")
	private RequestGlobalSearchSuggestion() {
		// For serialization only
	}

	public RequestGlobalSearchSuggestion(Request request) {
		this.request = request;
	}

	public Request getRequest() {
		return request;
	}
}
