package net.relatedwork.server.action;


import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.dao.AuthorAccessHandler;
import net.relatedwork.server.dao.CommentsAccessHelper;
import net.relatedwork.server.neo4jHelper.*;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.DisplayAuthor;
import net.relatedwork.shared.dto.DisplayAuthorResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

public class DisplayAuthorActionHandler implements
		ActionHandler<DisplayAuthor, DisplayAuthorResult> {
	
    @Inject AuthorAccessHandler authorAccessHandler;
    @Inject CommentsAccessHelper commentsAccessHelper;
	
	@Override
	public DisplayAuthorResult execute(DisplayAuthor action,
			ExecutionContext context) throws ActionException {
		
		DisplayAuthorResult result = new DisplayAuthorResult();

		String uri = action.getUri();
		IOHelper.log("Rendering author page with uri '"+ uri +"'");

        Node n = authorAccessHandler.getAuthorNodeFromUri(uri);
        if (n == null) {
            System.out.println("URI INDEX ERROR. uri " + uri + " has more than one associated node.");
            return result;
        }

        Author author = authorAccessHandler.authorFromNode(n);

        if (author == null) {
            System.out.println("Cannot render Author page for node: "+n.toString() +", uri: " + uri);
            return result;
        }
        result.setAuthor(author);

        for (Relationship rel:n.getRelationships(DBRelationshipTypes.CO_AUTHOR_COUNT)){
			Node coAuthor = rel.getEndNode();
			Integer score = (Integer)rel.getProperty(DBRelationshipProperties.CO_AUTHOR_COUNT);
			result.addCoAuthor(authorAccessHandler.authorFromNode(coAuthor,score));
		}

		for (Relationship rel:n.getRelationships(DBRelationshipTypes.CITES_AUTHOR,Direction.OUTGOING)){
			Node citedAuthor = rel.getEndNode();
			Integer score = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
			result.addCitedAuthor(authorAccessHandler.authorFromNode(citedAuthor, score));
		}
		
		for (Relationship rel:n.getRelationships(DBRelationshipTypes.CITES_AUTHOR,Direction.INCOMING)){
			Node citedAuthor = rel.getStartNode();
			Integer score = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
			result.addCitedByAuthor(authorAccessHandler.authorFromNode(citedAuthor, score));
		}
		
		for (Relationship rel:n.getRelationships(DBRelationshipTypes.SIM_AUTHOR, Direction.OUTGOING)){
			Node simAuthor = rel.getEndNode();
			Integer score = (int)((Double)rel.getProperty(DBRelationshipProperties.SIM_AUTHOR_SCORE)*1000.);
			result.addSimilarAuthor(authorAccessHandler.authorFromNode(simAuthor, score));
		}
		
		for (Relationship rel:n.getRelationships(DBRelationshipTypes.WRITTEN_BY)){
			Node paper = rel.getStartNode(); // {paper-node} --[AUTHOROF]--> {author-node}
			result.addWrittenPaper(Neo4jToDTOHelper.paperFromNode(paper));
		}

        result.setCommentList(commentsAccessHelper.getRelatedComments(uri));

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
