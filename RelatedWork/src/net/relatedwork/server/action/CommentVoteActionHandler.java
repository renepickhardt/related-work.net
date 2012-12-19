package net.relatedwork.server.action;

import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.CommentVoteAction;
import net.relatedwork.shared.dto.CommentVoteResult;

import static net.relatedwork.shared.dto.CommentVoteResult.VoteResult;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentVoteActionHandler implements ActionHandler<CommentVoteAction, CommentVoteResult> {
    private static int votes = 0;
    @Override
    public CommentVoteResult execute(CommentVoteAction commentVoteAction, ExecutionContext executionContext) throws ActionException {
        String commentUri = commentVoteAction.getCommentUri();
        boolean upVote = commentVoteAction.isUpVote();

        IOHelper.log("Vote for comment " + upVote + " " + commentUri);
        // TODO

        CommentVoteResult result = new CommentVoteResult(++votes, VoteResult.SUCCESS);
        return result;
    }

    @Override
    public Class<CommentVoteAction> getActionType() {
        return CommentVoteAction.class;
    }

    @Override
    public void undo(CommentVoteAction commentVoteAction, CommentVoteResult commentVoteResult, ExecutionContext executionContext) throws ActionException {
        throw new UnsupportedOperationException();
    }
}
