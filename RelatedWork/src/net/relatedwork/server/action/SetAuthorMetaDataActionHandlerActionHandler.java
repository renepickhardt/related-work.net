package net.relatedwork.server.action;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;

import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.shared.dto.SetAuthorMetaDataActionHandler;
import net.relatedwork.shared.dto.SetAuthorMetaDataActionHandlerResult;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class SetAuthorMetaDataActionHandlerActionHandler
		implements
		ActionHandler<SetAuthorMetaDataActionHandler, SetAuthorMetaDataActionHandlerResult> {

	@Inject ServletContext servletContext;

	
	@Inject
	public SetAuthorMetaDataActionHandlerActionHandler() {
	}

	@Override
	public SetAuthorMetaDataActionHandlerResult execute(
			SetAuthorMetaDataActionHandler action, ExecutionContext context)
			throws ActionException {
		
		Node author = null;
		String uri = action.getMetaData().get("uri"); 
		try{
			author = ContextHelper.getUriIndex(servletContext).get(DBNodeProperties.URI, uri).getSingle();
			System.out.println("get author with uri: " + uri);
		} catch (Exception e) {
			System.out.println("URI INDEX ERROR. uri " + uri + " has more than one associated node.");
			return null;
		}
		Transaction tx = ContextHelper.getGraphDatabase(servletContext).beginTx();
		try {
			for (String key:action.getMetaData().keySet()){
				if (key.equals("uri"))continue;
				author.setProperty("metadata:"+key, action.getMetaData().get(key));
			}
			tx.success();
		}finally{
			tx.finish();
		}
		
		return new SetAuthorMetaDataActionHandlerResult(true);
	}

	@Override
	public void undo(SetAuthorMetaDataActionHandler action,
			SetAuthorMetaDataActionHandlerResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<SetAuthorMetaDataActionHandler> getActionType() {
		return SetAuthorMetaDataActionHandler.class;
	}
}
