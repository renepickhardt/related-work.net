package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.shared.dto.DisplayPaperResult;

public class DisplayPaper extends UnsecuredActionImpl<DisplayPaperResult> {

	private String id;
	
	public DisplayPaper() {
	}

	public DisplayPaper(String id) {
		this.id = id;
	}

	public String getPaperId() {
		return id;
	}

	public void setPaperId(String id) {
		this.id = id;
	}
	
}
