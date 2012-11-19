package net.relatedwork.shared.dto;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.IsRenderable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Hyperlink;
import com.gwtplatform.dispatch.shared.Result;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;

/**
 * this class is a dto for authors that shall be displayed it 
 * is supposed to be as light weight as possible
 * 
 * but we always need an display name and uri and and Score
 * 
 * TODO: the score could be put in a class ScoredAuthor<T>
 * 
 * @author rpickhardt
 *
 */

public class Author implements Result, IsSerializable, IsRenderable{
	public Author(){		
	}

	
	private String displayName;
	private String uri;
	private Integer score;	

	public Author(String displayName, String uri, Integer score){
		this.displayName = displayName;
		this.uri = uri;
		this.score = score;
	}
	
	public Hyperlink getLink(){
		Hyperlink link = new Hyperlink();
		link.setTargetHistoryToken(NameTokens.author+";q="+uri);
		link.setText(displayName);
		return link;
	}
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public Integer getScore() {
		return score;
	}

	public void setScore(Integer score) {
		this.score = score;
	}	
}
