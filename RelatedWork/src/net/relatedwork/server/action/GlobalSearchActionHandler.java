package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.shared.dto.GlobalSearch;
import net.relatedwork.shared.dto.GlobalSearchResult;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class GlobalSearchActionHandler implements
		ActionHandler<GlobalSearch, GlobalSearchResult> {

	@Inject
	public GlobalSearchActionHandler() {
	}

	@Override
	public GlobalSearchResult execute(GlobalSearch action, ExecutionContext context)
			throws ActionException {
		return null;
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
