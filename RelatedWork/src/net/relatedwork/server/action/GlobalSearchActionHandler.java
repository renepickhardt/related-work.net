package net.relatedwork.server.action;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.dao.AuthorAccessHandler;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.Neo4jToDTOHelper;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;
import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;

import javax.servlet.ServletContext;

public class GlobalSearchActionHandler implements
		ActionHandler<GlobalSearch, GlobalSearchResult> {

	@Inject ServletContext servletContext;
    @Inject AuthorAccessHandler authorAccessHandler;

	@Override
	public GlobalSearchResult execute(GlobalSearch action, ExecutionContext context)
			throws ActionException {
		//TODO: log user ID / session and search query / time stamp - later log what the user clicks in search results

		Index<Node> index = ContextHelper.getSearchIndex(servletContext);
		GlobalSearchResult result = new GlobalSearchResult();
		
		String queryRaw = action.getQuery();
		String query = ContextHelper.prepareQueryString(queryRaw);
		
		IOHelper.log("Send query:" + queryRaw);
		IOHelper.log("Processed query:" + query);

		// setup sort field
		Sort s = new Sort();
		s.setSort(new SortField("score", SortField.DOUBLE, true));

		// run query
		IndexHits<Node> queryReults = index.query(new QueryContext(query).
				defaultOperator(Operator.AND).
				sort(s).
				top(20));

		IOHelper.log("Query returned " + queryReults.size() + " results.");

		for (Node n : queryReults) {
			if (!n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE)) continue;
			
			// Add query Results to result object
			if (NodeType.isAuthorNode(n)) {
				result.addAuthor(authorAccessHandler.authorFromNode(n));
			}

			if (NodeType.isPaperNode(n)){
				result.addPaper(Neo4jToDTOHelper.paperFromNode(n));
			}
		}

		return result;
	}

	@Override
	public void undo(GlobalSearch action, GlobalSearchResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<GlobalSearch> getActionType() {
		return GlobalSearch.class;
	}
}
