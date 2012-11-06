package de.renepickhardt.gwt.shared;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Paper implements IsSerializable {
	public Paper() {
		// TODO Auto-generated constructor stub
	}
	public Paper(String title) {
		this.title = title;
	}

	public Double pageRank;
	public Integer citationCount;
	public String title;

	
	public String getHtmlLink(){
		String result = "";
		result = result + "<a href='#paper_" + title + "'>" + title + "</a>";
		return result;
	}
	
	public String renderSRP() {
		return getHtmlLink();
	}
}
