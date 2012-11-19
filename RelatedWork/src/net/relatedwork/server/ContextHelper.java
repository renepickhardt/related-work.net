package net.relatedwork.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.index.Index;
import org.neo4j.index.impl.lucene.LuceneIndex;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.server.utils.SuggestTree;

public class ContextHelper {

	private static final String SUGGEST_TREE = "sugges-ttree";
	private static final String READ_ONLY_NEO4J = "read-only-neo4j";
	private static final String SEARCH_IDX = "search-idx";
	
	
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
			//TODO: don't hardcode path use Config() class!
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
	
	public static EmbeddedReadOnlyGraphDatabase getReadOnlyGraphDatabase(ServletContext servletContext){
		EmbeddedReadOnlyGraphDatabase graphDB = (EmbeddedReadOnlyGraphDatabase)servletContext.getAttribute(READ_ONLY_NEO4J);
		if (graphDB == null){
			graphDB = new EmbeddedReadOnlyGraphDatabase(Config.get().neo4jDbPath);
			servletContext.setAttribute(READ_ONLY_NEO4J, graphDB);
		}
		return graphDB;
	}
	
	public static Index<Node> getSearchIndex(ServletContext servletContext){
		EmbeddedReadOnlyGraphDatabase graphDB = getReadOnlyGraphDatabase(servletContext);
		Index<Node> index = (Index<Node>)servletContext.getAttribute(SEARCH_IDX);
		if (index == null){
			index = graphDB.index().forNodes("prsearch_idx");
			((LuceneIndex<Node>) index).setCacheCapacity("title", 300000);
			servletContext.setAttribute(SEARCH_IDX,index);
		}
		return index;
	}
}