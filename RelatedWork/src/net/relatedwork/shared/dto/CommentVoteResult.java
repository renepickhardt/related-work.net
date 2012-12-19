package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentVoteResult implements Result {
    public enum VoteResult {
        SUCCESS,
        ALREADY_VOTED,
    }
    private int votes;
    private VoteResult result;

    @SuppressWarnings("unused")
    private CommentVoteResult() {

    }

    public CommentVoteResult(int votes, VoteResult result) {
        this.votes = votes;
        this.result = result;
    }

    public int getVotes() {
        return votes;
    }

    public VoteResult getResult() {
        return result;
    }
}
