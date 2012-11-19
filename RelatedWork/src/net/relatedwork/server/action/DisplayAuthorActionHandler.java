package net.relatedwork.server.action;

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
		String id = action.getUri();
		id = id.replace(' ', '?');
				
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		Index<Node> index = ContextHelper.getSearchIndex(servletContext);
		IndexHits<Node> res = index.query(new QueryContext("title:"+id+"*").sort(s).top(10));
		
		if (res == null)
			return null;
		
		for (Node n : res) {
			if (!n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE))continue;
			Double pr = (Double)n.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
			if (NodeType.isAuthorNode(n)) {
				result.setName((String)n.getProperty(DBNodeProperties.AUTHOR_NAME));
				//TODO: benchmark if one look with conditional statements would be more efficient!
				for (Relationship rel:n.getRelationships(RelationshipTypes.CO_AUTHOR_COUNT)){
					Node coAuthor = rel.getEndNode();
					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CO_AUTHOR_COUNT);
					String name =(String)coAuthor.getProperty("name");
					result.addCoAuthor(new Author(name, name, count));
				}

				for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.OUTGOING)){
					Node citedAuthor = rel.getEndNode();
					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
					String name = (String)citedAuthor.getProperty("name");
					result.addCitedAuthor(new Author(name, name, count));
				}

				for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.INCOMING)){
					Node citedAuthor = rel.getStartNode();
					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
					String name = (String)citedAuthor.getProperty("name");
					result.addCitedByAuthor(new Author(name, name, count));
				}
				
				for (Relationship rel:n.getRelationships(RelationshipTypes.SIM_AUTHOR, Direction.OUTGOING)){
					Node simAuthor = rel.getEndNode();
					Double score = (Double)rel.getProperty(DBRelationshipProperties.SIM_AUTHOR_SCORE);
					String name = (String)simAuthor.getProperty("name");
					result.addSimilarAuthor(new Author(name,name, (int)(score*1000)));
				}
				
				for (Relationship rel:n.getRelationships(RelationshipTypes.AUTHOROF)){
					//Paper p = Neo4jToDTOHelper.generatePaperFromNode(n);
				}
				break;
			}
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
