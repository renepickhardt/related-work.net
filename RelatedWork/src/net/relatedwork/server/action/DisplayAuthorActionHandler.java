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
import net.relatedwork.server.neo4jHelper.DBRelationshipProperties;
import net.relatedwork.server.neo4jHelper.RelationshipTypes;
import net.relatedwork.shared.DisplayAuthor;
import net.relatedwork.shared.DisplayAuthorResult;
import net.relatedwork.shared.dto.Author;

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

//		EmbeddedReadOnlyGraphDatabase graphDB = ContextHelper.getReadOnlyGraphDatabase(servletContext);
		
//		id = id.replace(' ', '?');
				
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		Index<Node> index = ContextHelper.getSearchIndex(servletContext);
		IndexHits<Node> res = index.query(new QueryContext("title:Bridgeland*").sort(s).top(10));
		
		if (res == null)
			return null;
		
		for (Node n : res) {
			if (!n.hasProperty("pageRankValue"))continue;
			Double pr = (Double)n.getProperty("pageRankValue");
			if (n.hasProperty("name")) {
//				apc.name = (String) n.getProperty("name");
				
				//TODO: benchmark if one look with conditional statements would be more efficient!
//				for (Relationship rel:n.getRelationships(RelationshipTypes.CO_AUTHOR_COUNT)){
//					Node coAuthor = rel.getEndNode();
//					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CO_AUTHOR_COUNT);
//					apc.coAuthors.add(new Author((String)coAuthor.getProperty("name") + "\t" + count));
//				}
//				
//				for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.OUTGOING)){
//					Node citedAuthor = rel.getEndNode();
//					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
//					apc.citedAuthors.add(new Author((String)citedAuthor.getProperty("name") + "\t" + count));
//				}
//
//				for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.INCOMING)){
//					Node citedAuthor = rel.getStartNode();
//					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
//					apc.citedByAuthors.add(new Author((String)citedAuthor.getProperty("name") + "\t" + count));
//				}
				
				for (Relationship rel:n.getRelationships(RelationshipTypes.SIM_AUTHOR, Direction.OUTGOING)){
					Node simAuthor = rel.getEndNode();
					Double score = (Double)rel.getProperty(DBRelationshipProperties.SIM_AUTHOR_SCORE);
					result.addSimilarAuthor(new Author((String)simAuthor.getProperty("name"),"myURI", (int)(score*1000)));
				}
				
//				for (Relationship rel:n.getRelationships(RelationshipTypes.AUTHOROF)){
//					Node paper = rel.getOtherNode(n);
//					Paper p = new Paper();
//					p.title = (String)paper.getProperty(DBNodeProperties.PAPER_TITLE);
//					p.citationCount = (Integer)paper.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
//					p.pageRank = (Double)paper.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
//					apc.papers.add(p);
//				}
				
//				apcCache.put(id, apc);
				break;
			}
		}
		
//		Author a = new Author("least similar", "198h123", 5);
//		result.addSimilarAuthor(a);
//		a = new Author("most similar author", "9123h123", 10);
//		result.addSimilarAuthor(a);
//		a = new Author("medium similar author", "8234lj", 7);
//		result.addSimilarAuthor(a);
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
