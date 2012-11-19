package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.Integer;
import java.util.ArrayList;


public class GlobalSearchResult implements Result {

	private Integer resultSize;

	
	private ArrayList<Renderable> searchResults = new ArrayList<Renderable>();
	
	@SuppressWarnings("unused")
	public GlobalSearchResult() {
		// For serialization only
	}

	public void addPaper(Paper p){
		searchResults.add(p);
	}
	public void addAuthor(Author a){
		searchResults.add(a);
	}
	
	public GlobalSearchResult(Integer resultSize) {
		this.resultSize = resultSize;
	}

	public Integer getResultSize() {
		return resultSize;
	}
	
	public ArrayList<Renderable> getSearchResults() {
		return searchResults;
	}
	
}
