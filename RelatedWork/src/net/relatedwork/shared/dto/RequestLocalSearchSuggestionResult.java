package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import java.util.HashMap;

public class RequestLocalSearchSuggestionResult implements Result {

	private HashMap<String, Integer> localSuggestions;

	@SuppressWarnings("unused")
	private RequestLocalSearchSuggestionResult() {
		// For serialization only
	}

	public RequestLocalSearchSuggestionResult(HashMap<String, Integer> localSuggestions) {
		this.localSuggestions = localSuggestions;
	}

	public HashMap<String, Integer> getLocalSuggestions() {
		return localSuggestions;
	}
}
