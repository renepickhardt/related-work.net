package de.renepickhardt.gwt.server;

import java.util.ArrayList;
import java.util.HashMap;

import org.apache.lucene.search.Sort;
import org.apache.lucene.search.SortField;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.index.lucene.QueryContext;
import org.neo4j.kernel.EmbeddedGraphDatabase;

import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

import de.renepickhardt.gwt.client.GreetingService;
import de.renepickhardt.gwt.server.SuggestTree.SuggestionList;
import de.renepickhardt.gwt.server.neo4jHelper.DBNodeProperties;
import de.renepickhardt.gwt.server.neo4jHelper.DBRelationshipProperties;
import de.renepickhardt.gwt.server.neo4jHelper.RelationshipTypes;
import de.renepickhardt.gwt.shared.Author;
import de.renepickhardt.gwt.shared.AuthorPageContent;
import de.renepickhardt.gwt.shared.ContentContainer;
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
		
		id = id.replace(' ', '?');
		
		HashMap<String, AuthorPageContent> apcCache = ContextListener.getAuthorPageCach(getServletContext());
		
		if (apcCache.containsKey(id)){
			return apcCache.get(id);
		}
		
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		Index<Node> index = ContextListener.getSearchIndex(getServletContext());
		IndexHits<Node> res = index.query(new QueryContext("title:"
				+ id+"*").sort(s).top(10));
		
		
		if (res == null)
			return null;
		
		
		for (Node n : res) {
			if (!n.hasProperty("pageRankValue"))continue;
			Double pr = (Double)n.getProperty("pageRankValue");
			if (n.hasProperty("name")) {
				apc.name = (String) n.getProperty("name");
				
				//TODO: benchmark if one look with conditional statements would be more efficient!

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
				
				for (Relationship rel:n.getRelationships(RelationshipTypes.SIM_AUTHOR, Direction.OUTGOING)){
					Node simAuthor = rel.getEndNode();
					Double score = (Double)rel.getProperty(DBRelationshipProperties.SIM_AUTHOR_SCORE);
					apc.simAuthors.add(new Author((String)simAuthor.getProperty("name") + "\t" + score));
				}
				
				for (Relationship rel:n.getRelationships(RelationshipTypes.AUTHOROF)){
					Node paper = rel.getOtherNode(n);
					Paper p = new Paper();
					p.title = (String)paper.getProperty(DBNodeProperties.PAPER_TITLE);
					p.citationCount = (Integer)paper.getProperty(DBNodeProperties.PAPER_CITATION_COUNT);
					p.pageRank = (Double)paper.getProperty(DBNodeProperties.PAGE_RANK_VALUE);
					apc.papers.add(p);
				}
				
				apcCache.put(id, apc);
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
	
	public ContentContainer getMostPopularAuthorsAndPapers(){
		ContentContainer cc = new ContentContainer();

		Index<Node> index = ContextListener.getSearchIndex(getServletContext());
		Sort s = new Sort();
		s.setSort(new SortField("pr", SortField.DOUBLE, true));

		IndexHits<Node> res = index.query(new QueryContext("title:*").sort(s).top(100));

		if (res == null)
			return null;
			
		ArrayList<Author> authors = new ArrayList<Author>();
		ArrayList<Paper> papers = new ArrayList<Paper>();
		for (Node n : res) {
			if (!n.hasProperty("pageRankValue"))continue;
			Double pr = (Double)n.getProperty("pageRankValue");
			if (n.hasProperty("name")) {
				authors.add(new Author((String) n.getProperty("name")));
			}
			if (n.hasProperty("title")){
				papers.add(new Paper((String)n.getProperty("title")));
			}
		}
		cc.setAuthors(authors);
		cc.setPapers(papers);
		
		return cc;
	}
}
