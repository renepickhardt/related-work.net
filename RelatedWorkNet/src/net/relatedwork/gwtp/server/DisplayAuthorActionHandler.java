package net.relatedwork.gwtp.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.gwtp.shared.DisplayAuthor;
import net.relatedwork.gwtp.shared.DisplayAuthorResult;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DisplayAuthorActionHandler implements
		ActionHandler<DisplayAuthor, DisplayAuthorResult> {

	private final ServletContext servletContext;
	private final Provider<HttpServletRequest> requestProvider;

	@Inject
	public DisplayAuthorActionHandler(final ServletContext servletContext, final Provider<HttpServletRequest> requestProvider) {
		this.servletContext = servletContext;
		this.requestProvider = requestProvider;
	}

	@Override
	public DisplayAuthorResult execute(DisplayAuthor action,
			ExecutionContext context) throws ActionException {

		String result;
		if ((result = (String) servletContext.getAttribute(action.getKey()))!=null){
			return new DisplayAuthorResult(result);
		}
		result = "";
		EmbeddedReadOnlyGraphDatabase graphDB = Neo4jHelper.getReadOnlyGraphDatabase(servletContext);
		for (Node n:graphDB.getAllNodes()){
			if (n.hasProperty("name")){//TODO: need to change !
				for (Relationship rel:n.getRelationships()){
					Node otherNode = rel.getOtherNode(n);
					for (String key:otherNode.getPropertyKeys()){
						result = result + otherNode.getProperty(key) + "<br>";
					}
					result = result + "<br><br>";
				}
				servletContext.setAttribute(action.getKey(), result);
				break;
			}
		}
		
		DisplayAuthorResult displayAuthorResult = new DisplayAuthorResult(result);
		
		return displayAuthorResult;
	}

	@Override
	public void undo(DisplayAuthor action, DisplayAuthorResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<DisplayAuthor> getActionType() {
		return DisplayAuthor.class;
	}
}
