package net.relatedwork.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import net.relatedwork.shared.RequestGlobalSearchSuggestion;
import net.relatedwork.server.RequestGlobalSearchSuggestionActionHandler;
import net.relatedwork.shared.DisplayAuthor;
import net.relatedwork.server.action.DisplayAuthorActionHandler;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.server.action.GlobalSearchActionHandler;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(RequestGlobalSearchSuggestion.class,
				RequestGlobalSearchSuggestionActionHandler.class);

		bindHandler(DisplayAuthor.class, DisplayAuthorActionHandler.class);

		bindHandler(GlobalSearch.class, GlobalSearchActionHandler.class);
	}
}
