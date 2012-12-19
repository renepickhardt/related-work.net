package net.relatedwork.server.action;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.dao.CommentsAccessHelper;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Comments;
import net.relatedwork.shared.dto.NewCommentAction;
import org.neo4j.graphdb.Node;

/**
 * The ActionHandler deals with creation of comments.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class NewCommentActionHandler implements ActionHandler<NewCommentAction, Comments> {

    @Inject CommentsAccessHelper commentsAccessHelper;

    @Override
    public Comments execute(NewCommentAction newCommentAction, ExecutionContext executionContext) throws ActionException {
        Comments comment = newCommentAction.getComment();
        String targetUri = comment.getTargetUri();

        IOHelper.log(
                String.format("Creating new comment. Target[%s] Comment[%s]", targetUri, comment.getComment()));

        Node targetNode = commentsAccessHelper.getTargetNode(targetUri, comment.isReply());
        commentsAccessHelper.addComment(comment, targetNode);
        return comment;
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
