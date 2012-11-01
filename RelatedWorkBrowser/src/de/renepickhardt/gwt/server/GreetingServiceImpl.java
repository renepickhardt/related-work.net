package de.renepickhardt.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.renepickhardt.gwt.client.GreetingService;
import de.renepickhardt.gwt.server.SuggestTree.SuggestionList;
import de.renepickhardt.gwt.server.neo4jHelper.DBRelationshipProperties;
import de.renepickhardt.gwt.server.neo4jHelper.RelationshipTypes;
import de.renepickhardt.gwt.shared.Author;
import de.renepickhardt.gwt.shared.AuthorPageContent;
import de.renepickhardt.gwt.shared.ItemSuggestion;
import de.renepickhardt.gwt.shared.Paper;
import de.renepickhardt.gwt.shared.PaperPageContent;
import de.renepickhardt.gwt.shared.SimilarPaper;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
		GreetingService {

	public String greetServer(String input) throws IllegalArgumentException {
		EmbeddedGraphDatabase graphDB = ContextListener.getGraphDB(getServletContext());
		
		Index<Node> test = ContextListener.getSearchIndex(getServletContext());
		
		input = input.replace(' ', '?');
		
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));
		long start = System.currentTimeMillis();

		IndexHits<Node> res = test.query(new QueryContext("title:"
				+ input+"*").sort(s).top(10));
		
		
		if (res == null)
			return "no results found";
		
		String result ="";
		
		for (Node n : res) {
			String toDisplay = "";
			if (!n.hasProperty("pageRankValue"))continue;
			Double pr = (Double)n.getProperty("pageRankValue");
			if (n.hasProperty("name")) {
				toDisplay = (String)n.getProperty("name");
			}
			if (n.hasProperty("title")) {
				toDisplay = (String)n.getProperty("title");
			}
			result =  result + toDisplay + "  (" + pr + ")<br>"; 
		}
		long end = System.currentTimeMillis();
		
		result = result + "<br><br>search for: " + input
				+ "\nindexlookup: " + (end - start)
				+ "ms \t number of elements: " + res.size() + "\n";		
		
		input = escapeHtml(input);

		return "Hello, " + input + "!<br><br>Have a look at your search results:<br><br>" + result;
	}

	/**
	 * Escape an html string. Escaping data received from the client helps to
	 * prevent cross-site script vulnerabilities.
	 * 
	 * @param html the html string to escape
	 * @return the escaped string
	 */
	private String escapeHtml(String html) {
		if (html == null) {
			return null;
		}
		return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;")
				.replaceAll(">", "&gt;");
	}

	@Override
	public Response getSuggestions(Request req) {
		Response response = new Response();
		ArrayList<ItemSuggestion> suggestions = new ArrayList<ItemSuggestion>();

		SuggestTree<Double> index = ContextListener.getSuggestTree(getServletContext());
		
		SuggestionList list = index.getBestSuggestions(req.getQuery());
		
		for (int i = 0;i<list.length();i++){
			suggestions.add(new ItemSuggestion(list.get(i)));
		}
		
		response.setSuggestions(suggestions);
		return response;
	}
	
	public AuthorPageContent displayAuthorPage(String id){
		AuthorPageContent apc = new AuthorPageContent();
		
		Index<Node> index = ContextListener.getSearchIndex(getServletContext());
		id = id.replace(' ', '?');
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		IndexHits<Node> res = index.query(new QueryContext("title:"
				+ id+"*").sort(s).top(10));
		
		
		if (res == null)
			return null;
		
		
		for (Node n : res) {
			if (!n.hasProperty("pageRankValue"))continue;
			Double pr = (Double)n.getProperty("pageRankValue");
			if (n.hasProperty("name")) {
				apc.name = (String) n.getProperty("name");		

				HashMap<Node, Double> simAuthors = new HashMap<Node, Double>();
				for (Relationship rel:n.getRelationships(RelationshipTypes.AUTHOROF)){
					Node paper = rel.getOtherNode(n);
					if (paper.hasProperty("title")){// i found a paper
						Paper p = new Paper((String)paper.getProperty("title"));
						p.pageRank = (Double)paper.getProperty("pageRankValue");
						p.citationCount = (Integer)paper.getProperty("c_citation_count");
						apc.papers.add(p);
						for (Relationship simPaperRel:paper.getRelationships(DynamicRelationshipType.withName("RW:DM:CO_CITATION_SCORE"), Direction.OUTGOING)){
							Node simPaper = simPaperRel.getEndNode();
							if (simPaperRel.hasProperty("rw:coCitationScore")){
								Double score = (Double)simPaperRel.getProperty("rw:coCitationScore");
								for (Relationship authorRel:simPaper.getRelationships(RelationshipTypes.AUTHOROF)){
									Node simAuthor = authorRel.getOtherNode(simPaper);
									if (simAuthor.getId()==n.getId())continue;
									if (simAuthors.containsKey(simAuthor))
										simAuthors.put(simAuthor, simAuthors.get(simAuthor) + score);
									else
										simAuthors.put(simAuthor, score);
								}
							}
						}
					}
				}
				
				
				
				for (Relationship rel:n.getRelationships(RelationshipTypes.CO_AUTHOR_COUNT)){
					Node coAuthor = rel.getEndNode();
					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CO_AUTHOR_COUNT);
					apc.coAuthors.add(new Author((String)coAuthor.getProperty("name") + "\t" + count));
				}

				
				for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.OUTGOING)){
					Node citedAuthor = rel.getEndNode();
					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
					apc.citedAuthors.add(new Author((String)citedAuthor.getProperty("name") + "\t" + count));
				}

				for (Relationship rel:n.getRelationships(RelationshipTypes.CITES_AUTHOR,Direction.INCOMING)){
					Node citedAuthor = rel.getStartNode();
					Integer count = (Integer)rel.getProperty(DBRelationshipProperties.CITATION_COUNT);
					apc.citedByAuthors.add(new Author((String)citedAuthor.getProperty("name") + "\t" + count));
				}
				
				for (Node simAuthor: simAuthors.keySet()){
					apc.simAuthors.add(new Author((String)simAuthor.getProperty("name") + "\t" + simAuthors.get(simAuthor)));
				}
				
				/*
				HashMap<Node, Integer> coAuthors = new HashMap<Node, Integer>();
				HashMap<Node, Integer> citedAuthors = new HashMap<Node, Integer>();

				for (Relationship rel:n.getRelationships(DynamicRelationshipType.withName("author"))){
					Node paper = rel.getOtherNode(n);
					if (paper.hasProperty("title")){// i found a paper
						Paper p = new Paper((String)paper.getProperty("title"));
						p.pageRank = (Double)paper.getProperty("pageRankValue");
						p.citationCount = (Integer)paper.getProperty("c_citation_count");
						apc.papers.add(p);

						//find cited authors
						for (Relationship citedPaperRel: paper.getRelationships(DynamicRelationshipType.withName("ref"), Direction.OUTGOING)){
							Node citedPaper = citedPaperRel.getOtherNode(paper);
							if (citedPaper.hasProperty("title")){
								for (Relationship citedAuthorRel: citedPaper.getRelationships(DynamicRelationshipType.withName("author"),Direction.OUTGOING)){
									Node citedAuthor = citedAuthorRel.getOtherNode(citedPaper);
									if (citedAuthor.hasProperty("name")){
										if (citedAuthors.containsKey(citedAuthor))
											citedAuthors.put(citedAuthor, citedAuthors.get(citedAuthor) + 1);
										else
											citedAuthors.put(citedAuthor, 1);
									}
								}
							}
						}
						
						//find co authors
						for (Relationship coAuthorRel: paper.getRelationships(DynamicRelationshipType.withName("author"))){
							Node coAuthor = coAuthorRel.getOtherNode(paper);
							if (coAuthors.containsKey(coAuthor))
								coAuthors.put(coAuthor, coAuthors.get(coAuthor) + 1);
							else
								coAuthors.put(coAuthor, 1);
						}
					}
				}
				
				for (Node coAuthor: coAuthors.keySet()){
					apc.coAuthors.add(new Author((String)coAuthor.getProperty("name") + "\t" + coAuthors.get(coAuthor)));
				}
				
				for (Node citedAuthor: citedAuthors.keySet()){
					apc.citedAuthors.add(new Author((String)citedAuthor.getProperty("name") + "\t" + citedAuthors.get(citedAuthor)));
				}
				
								*/
				break;
			}
		}
		return apc;
	}
	
	public PaperPageContent displayPaperPage(String id){
		Index<Node> index = ContextListener.getSearchIndex(getServletContext());
		id = id.replace(' ', '?');
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		IndexHits<Node> res = index.query(new QueryContext("title:"
				+ id+"*").sort(s).top(10));
		
		
		if (res == null)
			return null;

		PaperPageContent ppc = new PaperPageContent();
		
		for (Node n : res) {
			if (!n.hasProperty("pageRankValue"))continue;
			Double pr = (Double)n.getProperty("pageRankValue");
			if (n.hasProperty("title")) {
				ppc.title = (String) n.getProperty("title");

				
				for (Relationship rel:n.getRelationships(DynamicRelationshipType.withName("author"))){
					Author a = new Author();
					a.name = (String)rel.getOtherNode(n).getProperty("name");
					ppc.authors.add(a);
				}

				
				for (Relationship rel: n.getRelationships(DynamicRelationshipType.withName("RW:DM:CO_CITATION_SCORE"),Direction.OUTGOING)){
					Node paper = rel.getEndNode();
					SimilarPaper simPaper = new SimilarPaper();
					simPaper.similarityScore = (Double)rel.getProperty("rw:coCitationScore");
					simPaper.title = (String)paper.getProperty("title");
					for (Relationship pAuthorRel:paper.getRelationships(DynamicRelationshipType.withName("author"))){
						String name = (String)pAuthorRel.getOtherNode(paper).getProperty("name");
						simPaper.authors.add(name);
					}
					ppc.similarPapers.add(simPaper);
				}
				
				break;
			}
		}
		
		return ppc;
	}
}