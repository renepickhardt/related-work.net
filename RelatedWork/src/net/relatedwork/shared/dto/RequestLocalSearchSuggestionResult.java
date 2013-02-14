package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import java.util.HashMap;

public class RequestLocalSearchSuggestionResult implements Result {

	private HashMap<String, Integer> localAuthorSuggestions;
	private HashMap<String, Integer> localPaperSuggestions;

	@SuppressWarnings("unused")
	private RequestLocalSearchSuggestionResult() {
		// For serialization only
	}

	public RequestLocalSearchSuggestionResult(HashMap<String, Integer> localAuthorSuggestions, HashMap<String, Integer> localPaperSuggestions) {
		this.localAuthorSuggestions = localAuthorSuggestions;
		this.localPaperSuggestions = localPaperSuggestions;
	}

	public HashMap<String, Integer> getLocalAuthorSuggestions() {
		return localAuthorSuggestions;
	}

	public HashMap<String, Integer> getLocalPaperSuggestions() {
		return localPaperSuggestions;
	}
}
