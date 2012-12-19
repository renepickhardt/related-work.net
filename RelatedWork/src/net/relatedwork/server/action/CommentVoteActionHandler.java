package net.relatedwork.server.action;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;
import net.relatedwork.server.dao.CommentsAccessHelper;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.CommentVoteAction;
import net.relatedwork.shared.dto.CommentVoteResult;

import static net.relatedwork.shared.dto.CommentVoteResult.VoteResult;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentVoteActionHandler implements ActionHandler<CommentVoteAction, CommentVoteResult> {

    @Inject CommentsAccessHelper commentsAccessHelper;

    @Override
    public CommentVoteResult execute(CommentVoteAction commentVoteAction, ExecutionContext executionContext) throws ActionException {
        String commentUri = commentVoteAction.getCommentUri();
        boolean upVote = commentVoteAction.isUpVote();
        Author author = new Author(); // TODO use logged in user

        IOHelper.log("Vote for comment " + upVote + " " + commentUri);

        int newVote = commentsAccessHelper.voteComment(author, commentUri, upVote);

        // TODO handle cases where user already voted

        return new CommentVoteResult(newVote, VoteResult.SUCCESS);
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
