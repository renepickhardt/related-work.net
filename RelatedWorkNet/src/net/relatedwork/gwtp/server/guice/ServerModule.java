package net.relatedwork.gwtp.server.guice;

import com.gwtplatform.dispatch.server.guice.HandlerModule;
import net.relatedwork.gwtp.shared.SendTextToServer;
import net.relatedwork.gwtp.server.SendTextToServerActionHandler;

public class ServerModule extends HandlerModule {

	@Override
	protected void configureHandlers() {

		bindHandler(SendTextToServer.class, SendTextToServerActionHandler.class);
	}
}
