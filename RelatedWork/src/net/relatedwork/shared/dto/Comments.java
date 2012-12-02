package net.relatedwork.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Comments implements IsSerializable {

	Author author;
	String comment;
	String date;
	int voting;
	
	public Comments() {
		// TODO Auto-generated constructor stub
	}

	public Comments(Author author, String comment){
		this.author=author;
		this.comment=comment;
	}
	
	/**
	 * @return the author
	 */
	public Author getAuthor() {
		return author;
	}

	/**
	 * @param author the author to set
	 */
	public void setAuthor(Author author) {
		this.author = author;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
	/**
	 * @return the date
	 */
	public String getDate() {
		return date;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.date = date;
	}

	/**
	 * @return the voting
	 */
	public int getVoting() {
		return voting;
	}

	/**
	 * @param voting the voting to set
	 */
	public void setVoting(int voting) {
		this.voting = voting;
	}

	
}
