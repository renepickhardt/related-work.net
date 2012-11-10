package net.relatedwork.gwtp.shared;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.gwtp.shared.DisplayAuthorResult;

import java.lang.String;

public class DisplayAuthor extends UnsecuredActionImpl<DisplayAuthorResult> {

	private String key;

	@SuppressWarnings("unused")
	private DisplayAuthor() {
		// For serialization only
	}

	public DisplayAuthor(String key) {
		this.key = key;
	}

	public String getKey() {
		return key;
	}
}
