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
	
	public boolean prepareForDisplay(){
		String[] values = suggestion.split("\t");
		if (values.length!=3)return false;
		if(values[1].equals("a")){
			suggestion = values[2];
		} else if(values[1].equals("p")){
			if (values[2].equals("C")){
				suggestion = values[0];
				String tmp = "" + suggestion.charAt(0);
				tmp = tmp.toUpperCase();
				suggestion = suggestion.replaceFirst(suggestion.charAt(0)+"", tmp);
			}else if (values[2].equals("N")){
				suggestion = values[0];
			}
			else 
				return false;
		}
		else
			return false;
		
		return true;
	}
}
