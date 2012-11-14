package net.relatedwork.gwtp.client.core;


import net.relatedwork.gwtp.client.GlobalSearchSuggestOracle;
import net.relatedwork.gwtp.client.place.NameTokens;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HasValue;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainPageView extends ViewImpl implements MainPagePresenter.MyView {

	public interface Binder extends UiBinder<Widget, MainPageView> {
	}

	private static Binder uiBinder = GWT.create(Binder.class);
	
	private static String html = "<h1>Web Application Starter Project</h1>\n"
			+ "<table align=\"center\">\n"
			+ "  <tr>\n"
			+ "    <td colspan=\"2\" style=\"font-weight:bold;\">Please enter your name:</td>\n"
			+ "  </tr>\n"
			+ "  <tr>\n"
			+ "    <td id=\"nameFieldContainer\"></td>\n"
			+ "    <td id=\"sendButtonContainer\"></td>\n"
			+ "  </tr>\n"
			+ "  <tr>\n"
			+ "    <td colspan=\"2\" style=\"color:red;\" id=\"errorLabelContainer\"></td>\n"
			+ "  </tr>\n" + "</table>\n";
	private final HTMLPanel panel;// = new HTMLPanel(html);
	@UiField HTMLPanel rwContent;
	@UiField HTMLPanel searchBoxContainer;
	
	private final Label errorLabel;
	private final SuggestBox searchBox;
	private final TextBox nameField;
	private final Button sendButton;

	private GlobalSearchSuggestOracle oracle;

	@Inject
	public MainPageView(/*final Binder binder*/) {
		panel = (HTMLPanel)Composite.asWidgetOrNull(uiBinder.createAndBindUi(this));
		//rwContent = (HTMLPanel) uiBinder.createAndBindUi(this);
		panel.add(new HTML(html));// ,"rwhead");
		sendButton = new Button("Send");
		nameField = new TextBox();
		nameField.setText("GWT User");
		errorLabel = new Label();
		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		searchBox = setSuggestBox(oracle);
		searchBoxContainer.add(searchBox);
		
		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		panel.add(nameField, "nameFieldContainer");
		panel.add(sendButton, "sendButtonContainer");
		panel.add(errorLabel, "errorLabelContainer");

		Hyperlink l = new Hyperlink();
		l.setTargetHistoryToken(NameTokens.getAuthor() + ";key=Hartmann, Hei");
		l.setText("Display Author Page");
		panel.add(l);
	}

	public SuggestBox setSuggestBox(GlobalSearchSuggestOracle oracle){
		SuggestBox searchBox = new SuggestBox(oracle);
		return searchBox;
	}

	@Override
	public Widget asWidget() {
		return panel;
	}

	@Override
	public HasValue<String> getNameValue() {
		return nameField;
	}
	
	@Override
	public SuggestBox getSuggestBox() {
		return searchBox;
	}	

	@Override
	public HasClickHandlers getSendClickHandlers() {
		return sendButton;
	}

	@Override
	public void resetAndFocus() {
		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();
	}

	@Override
	public void setError(String errorText) {
		errorLabel.setText(errorText);
	}

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPagePresenter.TYPE_SetMainContent) {
			setMainContent(content);
		} else {
			super.setInSlot(slot, content);
		}
	}

	private void setMainContent(Widget content) {
		rwContent.clear();
		if (content != null) {
			rwContent.add(content);
		}
	}

	@Override
	public void setOracle(GlobalSearchSuggestOracle oracle) {
		this.oracle = oracle;
	}
}
