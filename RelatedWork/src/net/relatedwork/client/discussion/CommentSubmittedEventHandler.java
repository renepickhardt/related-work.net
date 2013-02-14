package net.relatedwork.client.discussion;

import net.relatedwork.shared.dto.Comments;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
public interface CommentSubmittedEventHandler {
    void success(Comments newComment);
}
