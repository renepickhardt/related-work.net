package net.relatedwork.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.dispatch.shared.Result;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.IsRenderable;

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
	String displayName;
	String uri;
	private Double score;


	public Author(String displayName, String uri, Double score){
		this.displayName = displayName;
		this.uri = uri;
		this.score = score;
	}
	
	public Hyperlink getAuthorLink(){
		Hyperlink link = new Hyperlink();
		link.setTargetHistoryToken(NameTokens.author+";q="+uri);
		link.setText(displayName);
		return link;
	}

	public String getText(){
		return this.displayName;
	}
	
	public HTMLPanel getHoverable(){
		HTMLPanel panel = new HTMLPanel("");
		Label l = new Label(score+"");
		panel.add(l);
		return panel;
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

	public Double getScore() {
		return score;
	}

	public void setScore(Double score) {
		this.score = score;
	}

	@Override
	public Boolean passesFilter(String mask) {
		if (this.displayName.toLowerCase().contains(mask))return true;
		return false;
	}
	
	public Boolean hasLink(){
		return true;
	}
}
