package net.relatedwork.shared;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import net.relatedwork.shared.dto.Author;

import com.gwtplatform.dispatch.shared.Result;

public class DisplayAuthorResult implements Result {

	private ArrayList<Author> similarAuthors = new ArrayList<Author>();
	
	public DisplayAuthorResult() {
	}
	
	public void addSimilarAuthor(Author author){
		similarAuthors.add(author);
	}
	
	public ArrayList<Author> getSimilarAuthors(int k) {
		Collections.sort(similarAuthors, new Comparator<Author>() {
			@Override
			public int compare(Author a1, Author a2) {
				return -a1.getScore().compareTo(a2.getScore());
			}
		});
		return new ArrayList<Author>(similarAuthors.subList(0, Math.min(k, similarAuthors.size())));
	}
}
