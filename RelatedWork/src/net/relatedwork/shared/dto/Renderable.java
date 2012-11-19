package net.relatedwork.shared.dto;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Hyperlink;

import net.relatedwork.shared.IsRenderable;

public abstract class Renderable implements IsRenderable, IsSerializable{
	public String displayName;
	public String uri;

	public Renderable() {
	}

	@Override
	abstract public Hyperlink getLink(); 
}