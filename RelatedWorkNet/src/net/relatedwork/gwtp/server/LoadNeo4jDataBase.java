package net.relatedwork.gwtp.server;

import com.gwtplatform.dispatch.shared.ActionImpl;
import net.relatedwork.gwtp.server.LoadNeo4jDataBaseResult;
import java.lang.String;

public class LoadNeo4jDataBase extends ActionImpl<LoadNeo4jDataBaseResult> {

	private String path;

	@SuppressWarnings("unused")
	private LoadNeo4jDataBase() {
		// For serialization only
	}

	public LoadNeo4jDataBase(String path) {
		this.path = path;
	}

	public String getPath() {
		return path;
	}
}
