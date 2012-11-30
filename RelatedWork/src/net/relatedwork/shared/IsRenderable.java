package net.relatedwork.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.Hyperlink;

public interface IsRenderable extends IsSerializable {
	Hyperlink getAuthorLink();
}
