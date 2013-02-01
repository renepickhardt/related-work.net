package net.relatedwork.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;

public interface IsRenderable extends IsSerializable {
	Boolean hasLink();
	String getText();
	String getUri();
	Hyperlink getAuthorLink();
	HTMLPanel getHoverable();
	Boolean passesFilter(String mask);
}
