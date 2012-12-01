package net.relatedwork.server.action;

import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import sun.security.x509.AuthorityInfoAccessExtension;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipProperties;
import net.relatedwork.server.neo4jHelper.Neo4jToDTOHelper;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.DisplayAuthor;
import net.relatedwork.shared.dto.DisplayAuthorResult;
import net.relatedwork.shared.dto.Paper;

import com.google.gwt.dev.util.DefaultTextOutput;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DisplayAuthorActionHandler implements
		ActionHandler<DisplayAuthor, DisplayAuthorResult> {
	
	@Inject ServletContext servletContext;
	
	@Inject
	public DisplayAuthorActionHandler() {
	}

	@Override
	public DisplayAuthorResult execute(DisplayAuthor action,
			ExecutionContext context) throws ActionException {
		
		DisplayAuthorResult result = new DisplayAuthorResult();
		Index<Node> uriIndex = ContextHelper.getUriIndex(servletContext);
		
		String uri = action.getUri();
		IOHelper.log("Rendering author page with uri '"+ uri +"'");
		
		// TODO: This still gives
		// Service exception while executing net.relatedwork.shared.dto.DisplayAuthor: Service exception executing action "DisplayAuthor", java.lang.NullPointerException
		// when URI not found e.g. "Filippenko, A. V."
		Node n = null;
		try{
			n = uriIndex.get(DBNodeProperties.URI, uri).getSingle();
		} catch (Exception e) {
			System.out.println("URI INDEX ERROR. uri " + uri + " has more than one associated node.");
		}
		
		if (n == null ||  !NodeType.isAuthorNode(n) || !n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE) ){
			System.out.println("Cannot render Author page for node: "+n.toString() +", uri: " + uri);
			return result;
		}
		
		// Add data to result. Requires URIs to be set.
		result.setName((String)n.getProperty(DBNodeProperties.AUTHOR_NAME));
		
		for (Relationship rel:n.getRelationships(RelationshipTypes.CO_AUTHOR_COUNT)){
			Node coAuthor = rel.getEndNode();
			Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CO_AUTHOR_COUNT);
			result.addCoAuthor(Neo4jToDTOHelper.authorFromNode(coAuthor,count));
		}

		for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.OUTGOING)){
			Node citedAuthor = rel.getEndNode();
			result.addCitedAuthor(Neo4jToDTOHelper.authorFromNode(citedAuthor));
		}
		
		for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.INCOMING)){
			Node citedAuthor = rel.getStartNode();
			result.addCitedByAuthor(Neo4jToDTOHelper.authorFromNode(citedAuthor));
		}
		
		for (Relationship rel:n.getRelationships(RelationshipTypes.SIM_AUTHOR, Direction.OUTGOING)){
			Node simAuthor = rel.getEndNode();
			Integer score = (int)((Double)rel.getProperty(DBRelationshipProperties.SIM_AUTHOR_SCORE)*1000.);
			result.addSimilarAuthor(Neo4jToDTOHelper.authorFromNode(simAuthor, score));
		}
		
		for (Relationship rel:n.getRelationships(RelationshipTypes.AUTHOROF)){
			Node paper = rel.getStartNode(); // {paper-node} --[AUTHOROF]--> {author-node}
			result.addWrittenPaper(Neo4jToDTOHelper.paperFromNode(paper));
		}

		return result;
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
