package net.relatedwork.shared.dto;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Follow (probably star bookmark)
 * Send Privae message (contact author)
 * reputation scores (citation, H, pagerank, related work internal scores)
 * Website
 * twitter handle
 * Attanded universities
 * PhD advisor
 * students
 * tagging box and tags (discuss if we want a tagsonomy)
 * IDEA: similar authors as Tagcloud
 * 
 * @author rpickhardt
 */

public class AuthorSidebar implements IsSerializable {
	
	public AuthorSidebar() {
		// TODO Auto-generated constructor stub
	}
	
	private String website;
	private String twitterHandle;
	private Author advisor;
	private ArrayList<Author> students;
	
	public String getWebsite() {
		return website;
	}
	public void setWebsite(String website) {
		this.website = website;
	}
	public String getTwitterHandle() {
		return twitterHandle;
	}
	public void setTwitterHandle(String twitterHandle) {
		this.twitterHandle = twitterHandle;
	}
	public Author getAdvisor() {
		return advisor;
	}
	public void setAdvisor(Author advisor) {
		this.advisor = advisor;
	}
	public ArrayList<Author> getStudents() {
		return students;
	}
	public void setStudents(ArrayList<Author> students) {
		this.students = students;
	}
	
	public void addStudent(Author student){
		students.add(student);
	}
}
