package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public class NewCommentAction extends UnsecuredActionImpl<Comments> {

    private Comments comment;

    @SuppressWarnings("unused")
    private NewCommentAction() {
        // For serialization only
    }

    public NewCommentAction(Comments comment) {
        this.comment = comment;
    }

    public Comments getComment() {
        return comment;
    }
}
