package net.relatedwork.server.action;

import java.util.ArrayList;
import java.util.NoSuchElementException;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.server.ContextHelper;
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
		
		Index<Node> index = ContextHelper.getPaperIndex(servletContext);

		DisplayPaperResult result = new DisplayPaperResult();
		Node paperNode = null;
		try {
			paperNode = index.get("id", action.getPaperId()).next();
		} catch (NoSuchElementException e) {
			result.setTitle("Paper not found");
			return result;
		} 
		
		// System.out.println(paperNode.getPropertyKeys().toString());

		try {
			result.setTitle((String) paperNode.getProperty("title"));
			result.setAbstract((String) paperNode.getProperty("abstract"));
			result.setAuthors((String) paperNode.getProperty("c_authors"));
		} catch (NullPointerException e) {
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
