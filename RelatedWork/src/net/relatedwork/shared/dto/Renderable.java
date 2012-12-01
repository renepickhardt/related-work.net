package net.relatedwork.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;

import net.relatedwork.client.place.NameTokens;
import net.relatedwork.shared.IsRenderable;

public class Renderable implements IsRenderable, IsSerializable{
	public String displayName;
	public String uri;

	public Renderable() {
	}

	@Override
	public Hyperlink getAuthorLink(){
		return new Hyperlink(displayName, NameTokens.author);
	}
	
	@Override
	public HTMLPanel getHoverable() {
		// TODO Auto-generated method stub
		return null;
	}
}