package net.relatedwork.gwtp.shared;

import com.google.gwt.core.client.GWT;
import com.gwtplatform.dispatch.shared.UnsecuredActionImpl;
import net.relatedwork.gwtp.shared.SendTextToServerResult;

public class SendTextToServer extends
		UnsecuredActionImpl<SendTextToServerResult> {

	private String textToServer;
	private String method; 

	@SuppressWarnings("unused")
	private SendTextToServer() {
		// For serialization only
	}

	public SendTextToServer(String textToServer, String method) {
		this.textToServer = textToServer;
		this.method = method;
	}

	public String getTextToServer() {
		return textToServer;
	}
	public String getMethodPara(){
		return method;
	}
}
