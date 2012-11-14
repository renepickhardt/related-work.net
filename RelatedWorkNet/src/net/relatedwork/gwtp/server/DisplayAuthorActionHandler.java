package net.relatedwork.gwtp.server;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.gwtp.client.place.NameTokens;
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
		
		Index<Node> searchIndex = Neo4jHelper.getSearchIndex(servletContext);		
		String query = action.getKey().replace(' ', '?');
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		IndexHits<Node> lookUpResult = searchIndex.query(new QueryContext("title:"
				+ query+"*").sort(s).top(10));
		
		
		if (lookUpResult == null)
			return new DisplayAuthorResult("no results on search index: could not find Author");
		
		DisplayAuthorResult displayAuthorResult = null;
		for (Node n : lookUpResult) {		
			if (n.hasProperty("name")){//TODO: need to change !
				for (Relationship rel:n.getRelationships()){
					Node otherNode = rel.getOtherNode(n);
					for (String key:otherNode.getPropertyKeys()){
						result = result + "<a href='"+NameTokens.author +"#" + otherNode.getProperty(key)+"'>" + otherNode.getProperty(key) + "</a><br>";
					}
					result = result + "<br><br>";
				}
				servletContext.setAttribute(action.getKey(), result);
				displayAuthorResult = new DisplayAuthorResult(result);
				break;					
			}
		}
		if (displayAuthorResult==null){
			displayAuthorResult = new DisplayAuthorResult("could not find Author");
		}
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
