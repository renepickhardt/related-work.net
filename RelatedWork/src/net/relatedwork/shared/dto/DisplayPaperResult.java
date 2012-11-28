package net.relatedwork.shared.dto;

import java.util.ArrayList;

import com.gwtplatform.dispatch.shared.Result;

public class DisplayPaperResult implements Result {

	private String title;
	private String abstractString; // abstract is not vaild name
	private String authors;
	
	

	public DisplayPaperResult() {
	}

	public DisplayPaperResult(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	public String getAbstract() {
		return abstractString;
	}

	public void setAbstract(String abstractString) {
		this.abstractString = abstractString;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}
	
}
