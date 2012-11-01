package de.renepickhardt.gwt.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.user.client.rpc.IsSerializable;

public class PaperPageContent extends PageContent implements IsSerializable{
	public String title;
	public ArrayList<SimilarPaper> similarPapers;
	public ArrayList<Author> authors;
	
	public PaperPageContent() {
		title = "";
		similarPapers = new ArrayList<SimilarPaper>();
		authors = new ArrayList<Author>();
	}

	public String getHtmlString() {
		String result = "";
		
		int cnt =0;
		
		result = result + "<h1>" + title + "</h1>";		
		result = result + "by";
		
		for (Author author:authors){
			result = result + "<a href='#author_" + author.name+ "'>" + author.name + "</a>, ";
		}
		result = result.substring(0, result.length()-2);
		
		Collections.sort(similarPapers, new Comparator<SimilarPaper>() {
			@Override
			public int compare(SimilarPaper o1, SimilarPaper o2) {
				return -o1.similarityScore.compareTo(o2.similarityScore);
			}
		});
		result = result + "<h2>Following papers are similar</h2>";
		for (SimilarPaper p : similarPapers) {
			if (cnt++ > 10)
				break;
			result = result + "<li><a href='#" + HistoryTokens.PAPER_PAGE+"_" + p.title + "'>"+ p.title + "</a> (" + p.similarityScore
					+ ")<br> authors are:";
		
			for (String author:p.authors){
				result = result + "<a href='#author_" + author+ "'>" + author + "</a>, ";
			}
			result = result.substring(0, result.length()-2);
			result = result + "<br><br></li>";
			
		}
		
		return result;
	}

}
