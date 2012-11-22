package net.relatedwork.shared;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;

public class DisplayableItemSuggestion implements IsSerializable, Suggestion {

	private String displayString;;
	private String replacementString;

	public DisplayableItemSuggestion() {
		// TODO Auto-generated constructor stub
	}
		
	public DisplayableItemSuggestion(String suggestion, String q, boolean personalized){
		String[] values = suggestion.split("\t");
		String first = q.charAt(0)+"";
		first = first.toUpperCase();
		q = q.replaceFirst(q.charAt(0)+"", first);
		try{
			if(values[1].equals("a")){
				suggestion = values[2];
			} else if(values[1].equals("p")){
				if (values[2].equals("C")){
					suggestion = values[0];
					String tmp = "" + suggestion.charAt(0);
					tmp = tmp.toUpperCase();
					suggestion = suggestion.replaceFirst(suggestion.charAt(0)+"", tmp);
					q = q.replaceFirst(q.charAt(0)+"", tmp);
				}else if (values[2].equals("N")){
					suggestion = values[0];
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		replacementString = suggestion;
		displayString = suggestion.replaceFirst(q, "<b>"+q+"</b>");
		if (personalized){
			displayString = "<span style='color:#0000FF;'>" + displayString + "</span>";
		}
	}
	
	@Override
	public String getDisplayString() {
		// TODO Auto-generated method stub
		return displayString;
	}

	@Override
	public String getReplacementString() {
		// TODO Auto-generated method stub
		return replacementString;
	}
}
