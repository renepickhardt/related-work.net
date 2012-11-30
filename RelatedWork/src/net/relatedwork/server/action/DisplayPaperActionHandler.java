package net.relatedwork.server.action;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.DisplayAuthorResult;
import net.relatedwork.shared.dto.DisplayPaper;
import net.relatedwork.shared.dto.DisplayPaperResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DisplayPaperActionHandler implements
		ActionHandler<DisplayPaper, DisplayPaperResult> {

	@Inject ServletContext servletContext;
	
	@Inject
	public DisplayPaperActionHandler() {
	}

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
			result.setTitle((String) paperNode.getProperty("title"));
			result.setAbstract((String) paperNode.getProperty("abstract"));
			result.setAuthors((String) paperNode.getProperty("c_authors"));
		} catch (NotFoundException e) {
			result.setTitle("Paper properties not found");
		}

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
