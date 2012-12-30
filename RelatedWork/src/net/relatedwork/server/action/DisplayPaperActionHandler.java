package net.relatedwork.server.action;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.dao.AuthorAccessHandler;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.DBRelationshipTypes;
import net.relatedwork.server.neo4jHelper.Neo4jToDTOHelper;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.DisplayPaper;
import net.relatedwork.shared.dto.DisplayPaperResult;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;

import javax.servlet.ServletContext;

public class DisplayPaperActionHandler implements
		ActionHandler<DisplayPaper, DisplayPaperResult> {

	@Inject ServletContext servletContext;
    @Inject AuthorAccessHandler authorAccessHandler;

	@Override
	public DisplayPaperResult execute(DisplayPaper action, ExecutionContext context)
			throws ActionException {
		
		DisplayPaperResult result = new DisplayPaperResult();
		Index<Node> uriIndex = ContextHelper.getUriIndex(servletContext);
		
		String uri = action.getPaperUri();
		IOHelper.log("Rendering paper page with uri '"+ uri +"'");
		
		Node paperNode = null;
		try{
			paperNode = uriIndex.get(DBNodeProperties.URI, uri).getSingle();
		} catch (Exception e) {
			System.out.println("URI INDEX ERROR. uri " + uri + " has more than one associated node.");
		}
		
		if (paperNode == null ||  !NodeType.isPaperNode(paperNode) || !paperNode.hasProperty(DBNodeProperties.PAGE_RANK_VALUE) ){
			System.out.println("Cannot render Paper page for node: "+paperNode.toString() +", uri: " + uri);
			result.setTitle("Paper Not Found!");
			return result;
		}

		try {
			result.setTitle((String) paperNode.getProperty(DBNodeProperties.PAPER_TITLE));
			result.setAbstract((String) paperNode.getProperty(DBNodeProperties.PAPER_ABSTRACT));
			
			// Author list
			for (Relationship rel: paperNode.getRelationships(DBRelationshipTypes.WRITTEN_BY)){
				Node authorNode = rel.getEndNode();
				result.addAuthor(authorAccessHandler.authorFromNode(authorNode));
			}
			
			// Reference list
			for (Relationship rel: paperNode.getRelationships(DBRelationshipTypes.CITES, Direction.OUTGOING)){
				Node citationTargetNode = rel.getEndNode();
				result.addCitedPaper(Neo4jToDTOHelper.paperFromNode(citationTargetNode));
			};

//			for (String citeStirng: (ArrayList<String>)paperNode.getProperty(DBNodeProperties.PAPER_UNMATCHED_CITATIONS)){
//				result.addCitedPaper(new Paper(citeStirng));
//			}

			// Citation List
			for (Relationship rel: paperNode.getRelationships(DBRelationshipTypes.CITES, Direction.INCOMING)){
				Node citationSourceNode = rel.getStartNode();
				result.addCitedByPaper(Neo4jToDTOHelper.paperFromNode(citationSourceNode));
			};

			// CoCitations
			for (Relationship rel: paperNode.getRelationships(DBRelationshipTypes.CO_CITATION_SCORE, Direction.INCOMING)){
				Node coCitationSourceNode = rel.getStartNode();
				result.addCoCitedWithPaper(Neo4jToDTOHelper.paperFromNode(coCitationSourceNode));
			};

			for (Relationship rel: paperNode.getRelationships(DBRelationshipTypes.CO_CITATION_SCORE, Direction.OUTGOING)){
				Node coCitationTargetNode = rel.getEndNode();
				result.addCoCitedFromPaper(Neo4jToDTOHelper.paperFromNode(coCitationTargetNode));
			};

		} catch (NotFoundException e) {
			result.setTitle("Paper properties not found: " + e.getMessage());
		}

		IOHelper.log("Return paper object");
		return result;
	}

	@Override
	public void undo(DisplayPaper action, DisplayPaperResult result,
			ExecutionContext context) throws ActionException {
	}

	@Override
	public Class<DisplayPaper> getActionType() {
		return DisplayPaper.class;
	}
}
