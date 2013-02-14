package net.relatedwork.shared.dto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.gwtplatform.dispatch.shared.Result;

public class DisplayPaperResult implements Result {

	private String title;
	private String abstractString; // abstract is not vaild name

	private ArrayList<Author> authorList = new ArrayList<Author>();
	private ArrayList<Paper>  citedByPapers = new ArrayList<Paper>();
	private ArrayList<Paper>  citesPapers = new ArrayList<Paper>();

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

	public String getAuthorString() {
		String authors = "";
		for (Author author: authorList){
			authors += author.getDisplayName() + " ";
		}
		return authors;
	}

	public void addAuthor(Author author){
		this.authorList.add(author);
	}
	public void addCitedPaper(Paper paper){
		this.citesPapers.add(paper);
	}
	public void addCitedByPaper(Paper paper){
		this.citedByPapers.add(paper);
	}

	public void addCoCitedWithPaper(Paper paperFromNode) {
		// TODO Auto-generated method stub
		
	}

	public void addCoCitedFromPaper(Paper paperFromNode) {
		// TODO Auto-generated method stub
		
	}

	public ArrayList<Author> getAuthorList() {
		return authorList;
	}

	public ArrayList<Paper> getCitedByPapers(int k) {
		return getSortedTopKPapers(citedByPapers, k);
	}

	public ArrayList<Paper> getCitesPapers(int k) {
		return getSortedTopKPapers(citesPapers, k);
	}
	
	private ArrayList<Paper> getSortedTopKPapers(ArrayList<Paper> list, int k){
		Collections.sort(list, new Comparator<Paper>() {
			@Override
			public int compare(Paper p1, Paper p2) {
				return -p1.getScore().compareTo(p2.getScore());
			}
		});
		return new ArrayList<Paper>(list.subList(0, Math.min(k, list.size())));		
	}
	
	
}
