package net.relatedwork.server;

import java.util.HashMap;

import javax.servlet.ServletContext;

import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.server.neo4jHelper.Neo4jToDTOHelper;
import net.relatedwork.server.neo4jHelper.NodeType;
import net.relatedwork.server.utils.IOHelper;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestion;
import net.relatedwork.shared.dto.RequestLocalSearchSuggestionResult;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.kernel.EmbeddedReadOnlyGraphDatabase;

import com.google.inject.Inject;
import com.gwtplatform.dispatch.server.ExecutionContext;
import com.gwtplatform.dispatch.server.actionhandler.ActionHandler;
import com.gwtplatform.dispatch.shared.ActionException;

public class RequestLocalSearchSuggestionActionHandler
		implements
		ActionHandler<RequestLocalSearchSuggestion, RequestLocalSearchSuggestionResult> {

	@Inject ServletContext servletContext;

	private HashMap<String,Integer> map;

	@Inject
	public RequestLocalSearchSuggestionActionHandler() {
	}

	@Override
	public RequestLocalSearchSuggestionResult execute(
			RequestLocalSearchSuggestion action, ExecutionContext context)
			throws ActionException {
		
		map = new HashMap<String, Integer>();
		if (map==null)return null;
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		Index<Node> index = ContextHelper.getSearchIndex(servletContext);
		IndexHits<Node> res = index.query(new QueryContext("title:Hartmann,?Hei*").sort(s).top(2));
		
		if (res == null)
			return null;
		
		for (Node n : res) {
			if (NodeType.isAuthorNode(n)){
					for (Relationship rel:n.getRelationships(Direction.BOTH)){
						try {
							Node tmp = rel.getOtherNode(n);
							Integer pr = (int)(10000.*(Double)tmp.getProperty(DBNodeProperties.PAGE_RANK_VALUE));
							if (NodeType.isAuthorNode(tmp)){
								updateMap((String)tmp.getProperty(DBNodeProperties.AUTHOR_NAME), pr);
								for (Relationship rel1:tmp.getRelationships(Direction.BOTH)){
									try{
										Node tmp1 = rel1.getOtherNode(tmp);
										Integer pr1 = (int)(10000.*(Double)tmp1.getProperty(DBNodeProperties.PAGE_RANK_VALUE));
										if (NodeType.isAuthorNode(tmp1)){
											updateMap((String)tmp1.getProperty(DBNodeProperties.AUTHOR_NAME), pr1);
										}
										if (NodeType.isPaperNode(tmp1)){
											updateMap((String)tmp1.getProperty(DBNodeProperties.PAPER_TITLE), pr1);
										}
									}catch (Exception e) {
										e.printStackTrace();
										IOHelper.log(map.size() + " elements in personalized search index for " + (String)n.getProperty(DBNodeProperties.AUTHOR_NAME));
										continue;
									}
	
								}
							}
							if (NodeType.isPaperNode(tmp)){
								updateMap((String)tmp.getProperty(DBNodeProperties.PAPER_TITLE), pr);
							}
						}catch (Exception e) {
							e.printStackTrace();
							IOHelper.log(map.size() + " elements in personalized search index for " + (String)n.getProperty(DBNodeProperties.AUTHOR_NAME));
							continue;
						}	
					}
				IOHelper.log(map.size() + " elements in personalized search index for " + (String)n.getProperty(DBNodeProperties.AUTHOR_NAME));
				break;
			}
		}
//		map.put("helmut", 2);
//		map.put("heinrich", 3);
//		map.put("hendrik", 4);
//		map.put("herbert", 5);
//		map.put("heino", 1);
		RequestLocalSearchSuggestionResult result = new RequestLocalSearchSuggestionResult(map);
		return result;
	}

	@Override
	public void undo(RequestLocalSearchSuggestion action,
			RequestLocalSearchSuggestionResult result, ExecutionContext context)
			throws ActionException {
	}

	@Override
	public Class<RequestLocalSearchSuggestion> getActionType() {
		return RequestLocalSearchSuggestion.class;
	}
	
	//Something like this should be included to global helper class i need this all the time
	private void updateMap(String key, Integer value){
		if (map.containsKey(key)){
			Integer tmp = map.get(key);
			map.put(key, tmp + value);
		}
		else 
			map.put(key, value);
	}
}
