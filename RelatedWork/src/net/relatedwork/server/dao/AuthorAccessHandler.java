package net.relatedwork.server.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.relatedwork.server.ContextHelper;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.shared.dto.Author;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;

import javax.servlet.ServletContext;
import java.util.NoSuchElementException;

/**
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
@Singleton
public class AuthorAccessHandler {
    private Index<Node> uriIndex;

    @Inject
    public AuthorAccessHandler(ServletContext servletContext) {
        uriIndex = ContextHelper.getUriIndex(servletContext);
    }

    /**
     * Return the author node. If not found or multiple nodes have the uri, return null.
     */
    public Node getAuthorNodeFromUri(String uri) {
        // TODO: This still gives
        // Service exception while executing net.relatedwork.shared.dto.DisplayAuthor: Service exception executing action "DisplayAuthor", java.lang.NullPointerException
        // when URI not found e.g. "Filippenko, A. V."
        Node n = null;
        try{
            n = uriIndex.get(DBNodeProperties.URI, uri).getSingle();
        } catch (NoSuchElementException e) {
            // more than one
        }
        return n;
    }

    /**
     * Construct the Author object from node.
     */
    public Author authorFromNode(Node n) {
        Author author = authorFromNode(n, (Double)null);
        if (author == null) return null;
        Double pageRank = (Double) n.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
        author.setScore(pageRank);
        return author;
    }

    public Author authorFromNode(Node n, Double score) {
        if (n == null ||  !NodeType.isAuthorNode(n) || !n.hasProperty(DBNodeProperties.PAGE_RANK_VALUE) ){
            return null;
        }

        Author author = new Author();
        author.setDisplayName((String) n.getProperty(DBNodeProperties.AUTHOR_NAME));
        author.setUri((String)n.getProperty(DBNodeProperties.URI));
        author.setScore(score);
        return author;
    }

    public Author authorFromNode(Node n, Integer score) {
        return authorFromNode(n, Double.valueOf(score));
    }
}
