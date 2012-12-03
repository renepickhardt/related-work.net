package net.relatedwork.shared.dto;

import com.gwtplatform.dispatch.shared.Result;
import net.relatedwork.client.tools.session.SessionInformation;

public class NewUserActionResult implements Result {

	private boolean success;
	
	@SuppressWarnings("unused")
	public NewUserActionResult() {
		// For serialization only
	}
	
	public NewUserActionResult(boolean success) {
		this.success = success;		
	}
	

}
