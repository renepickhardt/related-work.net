package net.relatedwork.server.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import javax.servlet.ServletContext;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
@Singleton
public class UserAccessHandler {
    private Index<Node> uriIndex;

    @Inject
    public UserAccessHandler(ServletContext servletContext) {
        uriIndex = ContextHelper.getUriIndex(servletContext);
    }

    public Node getUserNodeFromEmail(String email) {
        String userUri = "rw:user:" + email;
        return uriIndex.get(DBNodeProperties.URI, userUri).getSingle();
    }

    public void indexUserNode(Node userNode, String email) {
        uriIndex.add(userNode, DBNodeProperties.URI, "rw:user:" + email);
    }

}
