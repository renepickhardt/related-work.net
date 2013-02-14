package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import java.lang.Boolean;

public class SetAuthorMetaDataActionHandlerResult implements Result {

	private Boolean success;

	@SuppressWarnings("unused")
	private SetAuthorMetaDataActionHandlerResult() {
		// For serialization only
	}

	public SetAuthorMetaDataActionHandlerResult(Boolean success) {
		this.success = success;
	}

	public Boolean getSuccess() {
		return success;
	}
}
