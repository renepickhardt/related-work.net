package de.renepickhardt.gwt.shared;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Hyperlink;

/**
 * PageContent are containers for different entities that are transmitted via
 * RPC calls They really have getters and setters and will provide data for the
 * frontend this makes sure that the front end is independent from the data base
 * scheme
 * 
 * @author rpickhardt
 * 
 */
public class AuthorPageContent extends PageContent implements IsSerializable {
	public String name;
	public ArrayList<Paper> papers;
	public ArrayList<Author> coAuthors;
	public ArrayList<Author> citedAuthors;
	public ArrayList<Author> citedByAuthors;
	public ArrayList<Author> simAuthors;

	public AuthorPageContent() {
		papers = new ArrayList<Paper>();
		coAuthors = new ArrayList<Author>();
		citedAuthors = new ArrayList<Author>();
		citedByAuthors = new ArrayList<Author>();
		simAuthors = new ArrayList<Author>();

		name = "";
	}

	public String getHtmlString() {
		String result = "";

		result = result + "<h1>" + name + "</h1>";

		/**
		 *  similar authors block
		 */
		result = result
				+ "<div style='float:left; width:450px'><h2>Similar Authors:</h2>";
		result = result + "<ul>";

		Collections.sort(simAuthors, new Comparator<Author>() {
			@Override
			public int compare(Author o1, Author o2) {
				Double i1 = Double.parseDouble(o1.name.split("\t")[1]);
				Double i2 = Double.parseDouble(o2.name.split("\t")[1]);
				return -i1.compareTo(i2);
			}
		});

		int cnt = 0;

		for (Author a : simAuthors) {
			if (cnt++ > 10)
				break;
			result = result + "<li><a href='#author_" + a.name.split("\t")[0]
					+ "'>" + a.name + "</a></li>";
		}
		result = result + "</ul></div>";

		
		/**
		 *  important coAuthors block
		 */

		
		result = result
				+ "<div style='float:left; width:450px'><h2>Important coauthors:</h2>";
		result = result + "<ul>";

		Collections.sort(coAuthors, new Comparator<Author>() {
			@Override
			public int compare(Author o1, Author o2) {
				Integer i1 = Integer.parseInt(o1.name.split("\t")[1]);
				Integer i2 = Integer.parseInt(o2.name.split("\t")[1]);
				return -i1.compareTo(i2);
			}
		});

		cnt = 0;

		for (Author a : coAuthors) {
			if (cnt++ > 10)
				break;
			result = result + "<li><a href='#author_" + a.name.split("\t")[0]
					+ "'>" + a.name + "</a></li>";
		}
		result = result + "</ul></div>";

		/**
		 *  authors that receive a lot of attation from current author block
		 */
		
		result = result
				+ "<div style='float:left; width:450px'><h2>"+name+"pays a lot of attantion to:</h2>";

		result = result + "<ul>";

		Collections.sort(citedAuthors, new Comparator<Author>() {
			@Override
			public int compare(Author o1, Author o2) {
				Integer i1 = Integer.parseInt(o1.name.split("\t")[1]);
				Integer i2 = Integer.parseInt(o2.name.split("\t")[1]);
				return -i1.compareTo(i2);
			}
		});

		cnt = 0;

		for (Author a : citedAuthors) {
			if (cnt++ > 10)
				break;
			result = result + "<li><a href='#author_" + a.name.split("\t")[0]
					+ "'>" + a.name + "</a></li>";
		}
		result = result + "</ul></div>";

		/**
		 *  authors that pay a lot of attantion to current author block
		 */

		
		result = result
				+ "<div style='float:left; width:450px'><h2>"+name+" is often cited by:</h2>";

		result = result + "<ul>";

		Collections.sort(citedByAuthors, new Comparator<Author>() {
			@Override
			public int compare(Author o1, Author o2) {
				Integer i1 = Integer.parseInt(o1.name.split("\t")[1]);
				Integer i2 = Integer.parseInt(o2.name.split("\t")[1]);
				return -i1.compareTo(i2);
			}
		});

		cnt = 0;

		for (Author a : citedByAuthors) {
			if (cnt++ > 10)
				break;
			result = result + "<li><a href='#author_" + a.name.split("\t")[0]
					+ "'>" + a.name + "</a></li>";
		}
		result = result + "</ul></div>";

		result = result + "<br style='float:block;'>";
		result = result
				+ "<div style='float:left; width:450px'><h2>Important papers from author by PageRank:</h2>";

		result = result + "<ul>";

		Collections.sort(papers, new Comparator<Paper>() {

			@Override
			public int compare(Paper o1, Paper o2) {
				return -o1.pageRank.compareTo(o2.pageRank);
			}
		});

		cnt = 0;
		for (Paper p : papers) {
			if (cnt++ > 10)
				break;
			result = result + "<li><a href='#" + HistoryTokens.PAPER_PAGE + "_"
					+ p.title + "'>" + p.title + "</a><br> (" + p.pageRank
					+ ")(<b>" + p.citationCount + "</b>)</li>";
		}
		result = result + "</ul></div>";

		result = result
				+ "<div style='float:left; width:450px'><h2>Important papers from author by Citation Count:</h2>";

		result = result + "<ul>";

		Collections.sort(papers, new Comparator<Paper>() {

			@Override
			public int compare(Paper o1, Paper o2) {
				return -o1.citationCount.compareTo(o2.citationCount);
			}
		});

		cnt = 0;
		for (Paper p : papers) {
			if (cnt++ > 10)
				break;
			result = result + "<li>" + p.title + "<br> (<b>" + p.pageRank
					+ "</b>)(" + p.citationCount + ")</li>";
		}
		result = result + "</ul></div>";

		return result;
	}
}
