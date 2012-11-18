package net.relatedwork.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import net.relatedwork.server.utils.Config;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.server.utils.SuggestTree;

public class ContextHelper {

	private static final String SUGGESTTREE = "suggesttree";
	
	public static SuggestTree<Integer> getSuggestTree(
			ServletContext servletContext) {
		SuggestTree<Integer> tree = (SuggestTree<Integer>) servletContext.getAttribute(SUGGESTTREE);
		if (tree == null){
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
			servletContext.setAttribute(SUGGESTTREE, tree);
		}
		return tree;
	}

}
