package de.renepickhardt.gwt.server;

import java.util.Comparator;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import de.renepickhardt.gwt.server.utils.Config;
import de.renepickhardt.gwt.shared.ItemSuggestion;

public class ContextListener implements ServletContextListener {

	public static EmbeddedGraphDatabase getGraphDB(ServletContext context) {
		return (EmbeddedGraphDatabase) context.getAttribute("neo4j");
	}

	public static SuggestTree<Double> getSuggestTree(ServletContext context) {
		return (SuggestTree<Double>) context.getAttribute("tree");
	}

	public static Index<Node> getSearchIndex(ServletContext context) {
		return (Index<Node>) context.getAttribute("searchIndex");
	}

	public static Index<Node> getAuthorSearchIndex(ServletContext context) {
		return (Index<Node>) context.getAttribute("authorSearchIndex");
	}

	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		getGraphDB(arg0.getServletContext()).shutdown();
	}

	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		EmbeddedGraphDatabase graphDB = new EmbeddedGraphDatabase(Config.get().neo4jDbPath);
		ServletContext context = arg0.getServletContext();
		context.setAttribute("neo4j", graphDB);

		boolean getSearchIndex = true;
		boolean getAuthorSearchIndex = false;
		boolean getAutoSuggestions = true;
		if (getSearchIndex) {
			Index<Node> test = graphDB.index().forNodes("prsearch_idx");
			((LuceneIndex<Node>) test).setCacheCapacity("title", 300000);
			context.setAttribute("searchIndex", test);
		}

		if (getAuthorSearchIndex){
			Index<Node> test = graphDB.index().forNodes("author_idx");
			((LuceneIndex<Node>) test).setCacheCapacity("name", 300000);
			context.setAttribute("authorSearchIndex", test);
		}
		
		if (getAutoSuggestions){
			SuggestTree<Double> tree = buildSuggestTree(graphDB);
			context.setAttribute("tree", tree);
		}
	}

	private SuggestTree<Double> buildSuggestTree(EmbeddedGraphDatabase graphDB) {
		Comparator<Double> c = new Comparator<Double>() {
			@Override
			public int compare(Double o1, Double o2) {
				return -o1.compareTo(o2);
			}
		};
		SuggestTree<Double> tree = new SuggestTree<Double>(6, c);

		HashMap<String, Double> map = new HashMap<String, Double>();

		for (Node n : graphDB.getAllNodes()) {
			if (n.hasProperty("name")) {
				String name = (String) n.getProperty("name");
				if (n.hasProperty("pageRankValue")) {
					map.put(name, (Double) n.getProperty("pageRankValue"));
				}
			}
		}
		tree.build(map);

		return tree;
	}

}
