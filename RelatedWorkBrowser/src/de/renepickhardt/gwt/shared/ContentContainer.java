package de.renepickhardt.gwt.shared;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ContentContainer extends PageContent implements IsSerializable{
	private ArrayList<Author> authors;
	private ArrayList<Paper> papers;
	
	public ContentContainer() {
		authors = new ArrayList<Author>();
		papers = new ArrayList<Paper>();
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(ArrayList<Author> authors) {
		this.authors = authors;
	}

	public ArrayList<Paper> getPapers() {
		return papers;
	}

	public void setPapers(ArrayList<Paper> papers) {
		this.papers = papers;
	}
	
	

}
