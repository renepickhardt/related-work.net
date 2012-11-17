package net.relatedwork.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import net.relatedwork.shared.RequestGlobalSearchSuggestion;
import net.relatedwork.server.RequestGlobalSearchSuggestionActionHandler;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(RequestGlobalSearchSuggestion.class,
				RequestGlobalSearchSuggestionActionHandler.class);
	}
}
