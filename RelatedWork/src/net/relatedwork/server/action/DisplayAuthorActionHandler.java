package net.relatedwork.server.action;

import javax.servlet.ServletContext;

import sun.security.x509.AuthorityInfoAccessExtension;

import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import net.relatedwork.shared.DisplayAuthor;
import net.relatedwork.shared.DisplayAuthorResult;
import net.relatedwork.shared.dto.Author;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.shared.ActionException;

public class DisplayAuthorActionHandler implements
		ActionHandler<DisplayAuthor, DisplayAuthorResult> {
	
	@Inject
	public DisplayAuthorActionHandler() {
	}

	@Override
	public DisplayAuthorResult execute(DisplayAuthor action,
			ExecutionContext context) throws ActionException {
		DisplayAuthorResult result = new DisplayAuthorResult();
		
		Author a = new Author("least similar", "198h123", 5);
		result.addSimilarAuthor(a);
		a = new Author("most similar author", "9123h123", 10);
		result.addSimilarAuthor(a);
		a = new Author("medium similar author", "8234lj", 7);
		result.addSimilarAuthor(a);

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
