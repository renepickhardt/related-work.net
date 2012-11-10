package net.relatedwork.gwtp.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;

import net.relatedwork.gwtp.shared.DisplayAuthor;
import net.relatedwork.gwtp.shared.SendTextToServer;
import net.relatedwork.gwtp.server.SendTextToServerActionHandler;
import net.relatedwork.gwtp.server.LoadNeo4jDataBase;
import net.relatedwork.gwtp.server.LoadNeo4jDataBaseActionHandler;
import net.relatedwork.gwtp.server.DisplayAuthorActionHandler;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {
		bindHandler(SendTextToServer.class, SendTextToServerActionHandler.class);

		bindHandler(LoadNeo4jDataBase.class,
				LoadNeo4jDataBaseActionHandler.class);

		bindHandler(DisplayAuthor.class, DisplayAuthorActionHandler.class);

	}
}
