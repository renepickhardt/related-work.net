package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.shared.dto.DisplayPaperResult;

public class DisplayPaper extends UnsecuredActionImpl<DisplayPaperResult> {

	private String uri;
	
	public DisplayPaper() {
	}

	public DisplayPaper(String uri) {
		this.uri = uri;
	}

	public String getPaperUri() {
		return uri;
	}

	public void setPaperUri(String uri) {
		this.uri = uri;
	}
	
}
