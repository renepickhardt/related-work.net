package net.relatedwork.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import net.relatedwork.server.RequestGlobalSearchSuggestionActionHandler;
import net.relatedwork.server.action.*;
import net.relatedwork.shared.dto.*;
import net.relatedwork.server.RequestLocalSearchSuggestionActionHandler;

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

        bindHandler(NewCommentAction.class, NewCommentActionHandler.class);

        bindHandler(CommentVoteAction.class, CommentVoteActionHandler.class);
	}
}
