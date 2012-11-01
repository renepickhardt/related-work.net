package de.renepickhardt.gwt.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class ItemSuggestion implements IsSerializable, Suggestion {

	private String s;
	
	public ItemSuggestion() {
		s = null;
	}
	
	public ItemSuggestion(String string) {
		s = string;
	}

	@Override
	public String getDisplayString() {
		return s;
	}

	@Override
	public String getReplacementString() {
		String tmp = s.replaceFirst("<b>", "");
		tmp = tmp.replaceFirst("</b>", "");
		return tmp;
	}

}
