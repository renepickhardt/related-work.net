package de.renepickhardt.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.SuggestOracle;

import de.renepickhardt.gwt.shared.AuthorPageContent;
import de.renepickhardt.gwt.shared.ContentContainer;
import de.renepickhardt.gwt.shared.PaperPageContent;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
	void greetServer(String input, AsyncCallback<String> callback)
			throws IllegalArgumentException;
	void getSuggestions(SuggestOracle.Request req, AsyncCallback callback);
	void displayAuthorPage(String id, AsyncCallback<AuthorPageContent> callback);
	void displayPaperPage(String id, AsyncCallback<PaperPageContent> Callback);
	void getMostPopularAuthorsAndPapers(AsyncCallback<ContentContainer> asyncCallback);
}
