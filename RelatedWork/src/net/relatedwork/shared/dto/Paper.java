package net.relatedwork.shared.dto;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.shared.IsRenderable;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;

/**
 * transferes a paper node to the client 
 * string parsing methods should only be called on the client.
 * 
 * @author rpickhardt
 *
 */

public class Paper implements IsRenderable, IsSerializable {

	private String title;
	private String uri;
	private String source; // Source ID, e.g. arxiv id
	private Integer citationCount;
	
	// for serialization
	Paper(){}
	
	public Paper(String title) {
		this.title = title;
		this.uri = "";
		this.source = "";
		this.citationCount = 0;
	}
	
	public Paper(String title, String uri, String source, Integer citeCnt) {
		this.title = title;
		this.uri = uri;
		this.source = source;
		this.citationCount = citeCnt;
	}

	@Override
	public Hyperlink getAuthorLink() {
		Hyperlink link = new Hyperlink();
		link.setTargetHistoryToken(NameTokens.paper+";q="+uri);
		link.setText(title);
		return link;
	}
	
	public String getText(){
		return this.title;
	}
	
	public HTMLPanel getHoverable(){
		HTMLPanel panel = new HTMLPanel("");
		if (this.uri.equals("")) return panel; 

		HorizontalPanel hp = new HorizontalPanel();
		Label l = new Label(citationCount + " Citations ");
		Anchor a = new Anchor();
		a.setHref(source.replaceAll("abs", "pdf"));
		a.setTarget("_blank");
		a.setText("Download pdf");
		Image star = new Image();
		int rand = (int)(Math.random()*2);
		if (rand == 0){
			star.setUrl("images/follow.svg");			
		}
		else
			star.setUrl("images/unfollow.svg");
		hp.add(l);
		hp.add(a);
		hp.add(star);
		panel.add(hp);
		return panel;
	}
	

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public Integer getCitationCount() {
		return citationCount;
	}

	public void setCitationCount(Integer citationCount) {
		this.citationCount = citationCount;
	}
	
	public Integer getScore(){
		return citationCount;
	}
	
	public Boolean passesFilter(String mask) {
		if (this.title.toLowerCase().contains(mask)) return true;
		return false;
	}

	public Boolean hasLink() {
		if (this.uri.equals("")) return false; 
		return true;
	}
	
}
