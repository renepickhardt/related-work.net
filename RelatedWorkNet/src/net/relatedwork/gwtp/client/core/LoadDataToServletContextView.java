package net.relatedwork.gwtp.client.core;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.inject.Inject;
import com.google.gwt.dev.Link;
import com.google.gwt.dom.builder.shared.HtmlLinkBuilder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class LoadDataToServletContextView extends ViewImpl implements
		LoadDataToServletContextPresenter.MyView {

	private static String html = "<h1>Load Stuff to Data base</h1>\n"
			+ "<table align=\"center\">\n"
			+ "  <tr>\n"
			+ "    <td id=\"loadDatabaseContainer\"></td>\n"
			+ "  </tr>\n"
			+ "</table>\n";	
	
	private final HTMLPanel widget = new HTMLPanel(html);
	
	private final Button loadDatabaseContainer;
	
	
	@Inject
	public LoadDataToServletContextView() {
		loadDatabaseContainer = new Button("loadDatabaseContainer");
		widget.add(loadDatabaseContainer, "loadDatabaseContainer");
	}

	@Override
	public Widget asWidget() {
		return null;
	}
}
