package net.relatedwork.gwtp.shared;

import com.gwtplatform.dispatch.shared.Result;

public class DisplayAuthorResult implements Result {

	private String result;
	
	public DisplayAuthorResult() {
	}
	
	public DisplayAuthorResult(String thatResult){
		this.result = thatResult;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}
	
	
}
