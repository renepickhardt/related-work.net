package net.relatedwork.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class ItemSuggestion implements IsSerializable, Suggestion {

	private String suggestion;

	public ItemSuggestion() {
		// TODO Auto-generated constructor stub
	}
	
	public ItemSuggestion(String suggestion){
		this.suggestion = suggestion;
	}
	
	@Override
	public String getDisplayString() {
		// TODO Auto-generated method stub
		return suggestion;
	}

	@Override
	public String getReplacementString() {
		// TODO Auto-generated method stub
		return suggestion;
	}
	

}
