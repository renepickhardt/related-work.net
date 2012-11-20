package net.relatedwork.shared.dto;


import com.gwtplatform.dispatch.shared.Result;
import java.lang.Integer;
import java.util.ArrayList;

import net.relatedwork.shared.IsRenderable;


public class GlobalSearchResult implements Result {

	private Integer resultSize;

	
	private ArrayList<IsRenderable> searchResults = new ArrayList<IsRenderable>();
	
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
	
	public ArrayList<IsRenderable> getSearchResults() {
		return searchResults;
	}
	
}
