package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Comments;
import net.relatedwork.shared.dto.NewCommentAction;

/**
 * The ActionHandler deals with creation of comments.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class NewCommentActionHandler implements ActionHandler<NewCommentAction, Comments> {

    @Override
    public Comments execute(NewCommentAction newCommentAction, ExecutionContext executionContext) throws ActionException {
        IOHelper.log("Creating new comment");
        Comments comment = newCommentAction.getComment();
        // TODO
        Comments newComment = comment;
        newComment.setComment(comment.getComment() + " (server processed)");
        return newComment;
    }

    @Override
    public Class<NewCommentAction> getActionType() {
        return NewCommentAction.class;
    }

    @Override
    public void undo(NewCommentAction newCommentAction, Comments comments, ExecutionContext executionContext) throws ActionException {
        throw new UnsupportedOperationException();
    }
}
