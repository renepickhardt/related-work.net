package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class CommentVoteAction extends UnsecuredActionImpl<CommentVoteResult> {
    private String user;  // TODO proper User type
    private String commentUri;
    private boolean upVote;

    @SuppressWarnings("unused")
    private CommentVoteAction() {

    }

    public CommentVoteAction(String user, String commentUri, boolean upVote) {
        this.user = user;
        this.commentUri = commentUri;
        this.upVote = upVote;
    }

    public String getUser() {
        return user;
    }

    public String getCommentUri() {
        return commentUri;
    }

    public boolean isUpVote() {
        return upVote;
    }
}
