package net.relatedwork.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.helpers.collection.MapUtil;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.kernel.EmbeddedGraphDatabase;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;
import org.neo4j.kernel.impl.core.NodeImpl;

import net.relatedwork.server.executables.CustomTokenAnalyzer;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.userHelper.UserInformation;
import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.SuggestTree;

public class ContextHelper {

	private static final String SUGGEST_TREE = "sugges-ttree";
	private static final String READ_ONLY_NEO4J = "read-only-neo4j";
	
	private static final String SEARCH_IDX_NEO4J = DBNodeProperties.SEARCH_INDEX_NAME;
	private static final String SEARCH_IDX_GWT = "searchIdx";
	
	private static final String PAPER_IDX_NEO4J = "source_idx";
	private static final String PAPER_IDX_GWT = "paper-idx";
	
	private static final String URI_IDX = DBNodeProperties.URI_INDEX_NAME;

	// Get NEO4J DB
	public static EmbeddedGraphDatabase getGraphDatabase(ServletContext servletContext){
		EmbeddedGraphDatabase graphDB = (EmbeddedGraphDatabase)servletContext.getAttribute(READ_ONLY_NEO4J);
		if (graphDB == null){
			graphDB = new EmbeddedGraphDatabase(Config.get().neo4jDbPath);
			servletContext.setAttribute(READ_ONLY_NEO4J, graphDB);
		}
		return graphDB;
	}

	
	// Auto Completion
	public static SuggestTree<Integer> getSuggestTree(
			ServletContext servletContext) {
		SuggestTree<Integer> tree = (SuggestTree<Integer>) servletContext.getAttribute(SUGGEST_TREE);
		if (tree == null){
			IOHelper.log("build new suggesttree from disk");
			tree = new SuggestTree<Integer>(5,new Comparator<Integer>(){
				@Override
				public int compare(Integer o1, Integer o2) {
					return -o1.compareTo(o2);
				}});
			
			HashMap<String, Integer> map = new HashMap<String, Integer>();
			BufferedReader reader = IOHelper.openReadFile(Config.get().autoCompleteFile);
			String line = "";
			try {
				while ((line=reader.readLine())!=null){
					String[] values = line.split("\t\t");
					if (values.length!=2)continue;
					map.put(values[1], Integer.parseInt(values[0]));
				}
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tree.build(map);
			servletContext.setAttribute(SUGGEST_TREE, tree);
		}
		return tree;
	}

	// Search index
	public static Index<Node> getSearchIndex(ServletContext servletContext){
		EmbeddedGraphDatabase graphDB = getGraphDatabase(servletContext);
		Index<Node> index = (Index<Node>)servletContext.getAttribute(SEARCH_IDX_GWT);
		if (index == null){
			System.out.println("Adding search index - " + SEARCH_IDX_GWT + "- to servletContext.");
			index = graphDB.index().forNodes(SEARCH_IDX_NEO4J,
					MapUtil.stringMap("analyzer", CustomTokenAnalyzer.class.getName())
					);
			((LuceneIndex<Node>) index).setCacheCapacity("key", 300000);
			servletContext.setAttribute(SEARCH_IDX_GWT,index);
		}
		return index;
	}

	// URI index
	public static Index<Node> getUriIndex(ServletContext servletContext){
		EmbeddedGraphDatabase graphDB = getGraphDatabase(servletContext);
		Index<Node> index = (Index<Node>)servletContext.getAttribute(URI_IDX);
		if (index == null){
			System.out.println("Adding uri index - " + URI_IDX + " - to servletContext");
			index = graphDB.index().forNodes(URI_IDX);
			((LuceneIndex<Node>) index).setCacheCapacity("uri", 300000);
			servletContext.setAttribute(URI_IDX,index);
		}
		return index;
	}
	
	// arxiv-id index
	public static Index<Node> getPaperIndex(ServletContext servletContext){
		EmbeddedGraphDatabase graphDB = getGraphDatabase(servletContext);
		Index<Node> index = (Index<Node>)servletContext.getAttribute(PAPER_IDX_GWT);
		if (index == null){
			System.out.println("Initializing paper index - " + PAPER_IDX_GWT);
			index = graphDB.index().forNodes(PAPER_IDX_NEO4J);
			((LuceneIndex<Node>) index).setCacheCapacity("title", 300000);
			servletContext.setAttribute(PAPER_IDX_GWT,index);
		}
		return index;
	}


	public static Node getNodeByUri(String uri, ServletContext servletContext) {
		return getUriIndex(servletContext).get("url", uri).getSingle();
	}


	public static Node getUserNodeFromEamil(String email, ServletContext servletContext) {
		return getNodeByUri("rw:user:" + email, servletContext);
	}
	
}