package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import java.util.HashMap;

public class SetAuthorMetaDataActionHandler extends
		UnsecuredActionImpl<SetAuthorMetaDataActionHandlerResult> {

	private HashMap<String, String> metaData;

	@SuppressWarnings("unused")
	private SetAuthorMetaDataActionHandler() {
		// For serialization only
	}

	public SetAuthorMetaDataActionHandler(HashMap<String, String> metaData) {
		this.metaData = metaData;
	}

	public HashMap<String, String> getMetaData() {
		return metaData;
	}
}
