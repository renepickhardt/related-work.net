package net.relatedwork.server.action;

import javax.servlet.ServletContext;

import org.apache.lucene.queryParser.QueryParser.Operator;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;

import sun.security.x509.DistributionPointName;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipProperties;
import net.relatedwork.server.neo4jHelper.Neo4jToDTOHelper;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.DisplayAuthorResult;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;
import net.relatedwork.shared.dto.Paper;

import com.google.gwt.uibinder.elementparsers.DialogBoxParser;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GlobalSearchActionHandler implements
		ActionHandler<GlobalSearch, GlobalSearchResult> {

	@Inject ServletContext servletContext;
	
	@Inject
	public GlobalSearchActionHandler() {
	}

	public static String prepareQuery(String queryString){
		queryString = queryString.replaceAll("\\W+", " ");
		queryString = queryString.trim();
		queryString = queryString.replace(',',' ');
		queryString = queryString.toLowerCase();
		// replace intermediate whitespaces
		queryString = queryString.replaceAll("\\s+", "* AND ");
		queryString = queryString + "*";
		
		return queryString;
	}
	
	@Override
	public GlobalSearchResult execute(GlobalSearch action, ExecutionContext context)
			throws ActionException {
		
		GlobalSearchResult result = new GlobalSearchResult();
		String query = action.getQuery();

		System.out.println("Send Query:" + query);
		//TODO: log user ID / session and search query / time stamp - later log what the user clicks in search results
		// query = query.replace(' ', '?');
				
		query = prepareQuery(query);
		System.out.println("Send Query:" + "title:"+query);

		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		Index<Node> index = ContextHelper.getSearchIndex(servletContext);
		IndexHits<Node> res = index.query(new QueryContext("title:"+query).defaultOperator(Operator.AND).sort(s).top(20));
		//IndexHits<Node> res = index.query(new QueryContext("title:"+query).defaultOperator(Operator.AND).sort(s).top(20));
		//IndexHits<Node> res = index.query("title",query);

		System.out.println("Query returned "+res.size()+" results");

		for (Node n : res) {
			if (!n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE))continue;
			Double pr = (Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
			if (NodeType.isAuthorNode(n)) {
//				IOHelper.log("add author node: ");
				String name = (String)n.getProperty(DBNodeProperties.AUTHOR_NAME);
				Author a = new Author(name, name, (int)(pr*1000.));
				result.addAuthor(a);
			}
			if (NodeType.isPaperNode(n)){
//				IOHelper.log("add paper node:");
				Paper p = Neo4jToDTOHelper.generatePaperFromNode(n);
				result.addPaper(p);
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
