package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.Integer;

public class GlobalSearchResult implements Result {

	private Integer resultSize;

	@SuppressWarnings("unused")
	private GlobalSearchResult() {
		// For serialization only
	}

	public GlobalSearchResult(Integer resultSize) {
		this.resultSize = resultSize;
	}

	public Integer getResultSize() {
		return resultSize;
	}
}
