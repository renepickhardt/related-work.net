package net.relatedwork.shared.dto;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.IsRenderable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Hyperlink;

/**
 * transferes a paper node to the client 
 * string parsing methods should only be called on the client.
 * 
 * @author rpickhardt
 *
 */

public class Paper extends Renderable implements IsRenderable, IsSerializable {

	private String title;
	private String uri;
	private String source;
	private String date;
	private Integer citationCount;
	
	Paper(){}
	
	public Paper(String title, String uri, String source, String date,
			Integer citeCnt) {
		this.title = title;
		this.uri = uri;
		this.source = source;
		this.date = date;
		this.citationCount = citeCnt;
	}

	@Override
	public Hyperlink getLink() {
		Hyperlink link = new Hyperlink();
		link.setTargetHistoryToken(NameTokens.author+";q="+uri);
		link.setText(title + "Don't click me yet");
		return link;
	}

}
