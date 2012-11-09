package net.relatedwork.gwtp.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.gwtp.server.LoadNeo4jDataBase;
import net.relatedwork.gwtp.server.LoadNeo4jDataBaseResult;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class LoadNeo4jDataBaseActionHandler implements
		ActionHandler<LoadNeo4jDataBase, LoadNeo4jDataBaseResult> {

	private final ServletContext servletContext;

	private final Provider<HttpServletRequest> requestProvider;

	@Inject
	public LoadNeo4jDataBaseActionHandler(final ServletContext servletContext, final Provider<HttpServletRequest> requestProvider) {
		this.servletContext = servletContext;
		this.requestProvider = requestProvider;
	}

	@Override
	public LoadNeo4jDataBaseResult execute(LoadNeo4jDataBase action,
			ExecutionContext context) throws ActionException {
		EmbeddedGraphDatabase graphDB = new EmbeddedGraphDatabase(action.getPath());
		servletContext.setAttribute("graphDB", graphDB );
		return null;
	}

	@Override
	public void undo(LoadNeo4jDataBase action, LoadNeo4jDataBaseResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<LoadNeo4jDataBase> getActionType() {
		return LoadNeo4jDataBase.class;
	}
}
