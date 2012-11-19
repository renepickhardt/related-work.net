package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.ActionImpl;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;

import net.relatedwork.shared.dto.GlobalSearchResult;
import java.lang.String;

public class GlobalSearch extends UnsecuredActionImpl<GlobalSearchResult> {

	private String query;

	@SuppressWarnings("unused")
	private GlobalSearch() {
		// For serialization only
	}

	public GlobalSearch(String query) {
		this.query = query;
	}

	public String getQuery() {
		return query;
	}
}
