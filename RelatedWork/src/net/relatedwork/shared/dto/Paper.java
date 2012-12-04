package net.relatedwork.shared.dto;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.server.neo4jHelper.DBNodeProperties;
import net.relatedwork.shared.IsRenderable;


import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
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
	
	public HTMLPanel getHoverable(){
		HTMLPanel panel = new HTMLPanel("");
		Label l = new Label(citationCount+"");
		panel.add(l);
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
		if (this.title.toLowerCase().contains(mask))return true;
		return false;
	}	
	
}
