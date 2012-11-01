package de.renepickhardt.gwt.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.google.gwt.user.client.ui.SuggestOracle;

import de.renepickhardt.gwt.shared.AuthorPageContent;
import de.renepickhardt.gwt.shared.PaperPageContent;

/**
 * The client side stub for the RPC service.
 */
@RemoteServiceRelativePath("greet")
public interface GreetingService extends RemoteService {
	String greetServer(String name) throws IllegalArgumentException;
	SuggestOracle.Response getSuggestions(SuggestOracle.Request req);
	AuthorPageContent displayAuthorPage(String id);
	PaperPageContent displayPaperPage(String id);
}
