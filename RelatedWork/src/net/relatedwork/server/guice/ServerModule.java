package net.relatedwork.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import net.relatedwork.server.RequestGlobalSearchSuggestionActionHandler;
import net.relatedwork.server.action.DisplayAuthorActionHandler;
import net.relatedwork.shared.dto.DisplayAuthor;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.LoginAction;
import net.relatedwork.shared.dto.NewUserAction;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.dto.UserVerifyAction;
import net.relatedwork.server.action.GlobalSearchActionHandler;
import net.relatedwork.server.action.LoginActionActionHandler;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestion;
import net.relatedwork.server.RequestLocalSearchSuggestionActionHandler;
import net.relatedwork.server.action.NewUserActionActionHandler;
import net.relatedwork.shared.dto.DisplayPaper;
import net.relatedwork.server.action.DisplayPaperActionHandler;
import net.relatedwork.server.action.UserVerifyActionActionHandler;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(RequestGlobalSearchSuggestion.class,
				RequestGlobalSearchSuggestionActionHandler.class);

		bindHandler(DisplayAuthor.class, DisplayAuthorActionHandler.class);

		bindHandler(GlobalSearch.class, GlobalSearchActionHandler.class);

		bindHandler(LoginAction.class, LoginActionActionHandler.class);

		bindHandler(RequestLocalSearchSuggestion.class,
				RequestLocalSearchSuggestionActionHandler.class);
		


		bindHandler(NewUserAction.class, NewUserActionActionHandler.class);

		bindHandler(DisplayPaper.class, DisplayPaperActionHandler.class);

		bindHandler(UserVerifyAction.class, UserVerifyActionActionHandler.class);
	}
}
