package net.relatedwork.shared;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.shared.DisplayAuthorResult;
import java.lang.String;

public class DisplayAuthor extends UnsecuredActionImpl<DisplayAuthorResult> {

	private String uri;

	@SuppressWarnings("unused")
	private DisplayAuthor() {
		// For serialization only
	}

	public DisplayAuthor(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}
}
