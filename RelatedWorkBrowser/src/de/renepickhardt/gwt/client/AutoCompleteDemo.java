package de.renepickhardt.gwt.client;

import java.util.ArrayList;
import java.util.HashMap;


import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.TabPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

import de.renepickhardt.gwt.client.staticcontent.AuthorPage;
import de.renepickhardt.gwt.server.utils.Config;
import de.renepickhardt.gwt.shared.Author;
import de.renepickhardt.gwt.shared.AuthorPageContent;
import de.renepickhardt.gwt.shared.ContentContainer;
import de.renepickhardt.gwt.shared.FieldVerifier;
import de.renepickhardt.gwt.shared.HistoryTokens;
import de.renepickhardt.gwt.shared.Paper;
import de.renepickhardt.gwt.shared.PaperPageContent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class AutoCompleteDemo implements EntryPoint {
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";

	private final HashMap<String, AuthorPageContent> authorPageCach = new HashMap<String, AuthorPageContent>();

	/**
	 * Create a remote service proxy to talk to the server-side Greeting
	 * service.
	 */
	private final GreetingServiceAsync greetingService = GWT
			.create(GreetingService.class);

	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		handleCookies();
		final Button sendButton = new Button("Send");
		ItemSuggestOracle oracle = new ItemSuggestOracle();
		final SuggestBox nameField = new SuggestBox(oracle);
		nameField.setText("GWT User");
		final Label errorLabel = new Label();

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(nameField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(errorLabel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a
			 * response.
			 */
			private void sendNameToServer() {
				// First, we validate the input.
				errorLabel.setText("");
				String textToServer = nameField.getText();
				if (!FieldVerifier.isValidName(textToServer)) {
					errorLabel.setText("Please enter at least four characters");
					return;
				}

				// Then, we send the input to the server.
				sendButton.setEnabled(false);
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				greetingService.greetServer(textToServer,
						new AsyncCallback<String>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(SERVER_ERROR);
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(String result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML(result);
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);

		final TabPanel tabPanel = new TabPanel();

		Hyperlink link = new Hyperlink();
		link.setTargetHistoryToken("author_Hartmann, Heinrich");
		AuthorPage myConstants = (AuthorPage) GWT.create(AuthorPage.class);
		link.setText("Seite von Hartmann"+myConstants.citedAuthors() + myConstants.similarAuthors());

		HTML h = new HTML("<h1>Let's look at conferences</h1>");
		h.setTitle(HistoryTokens.CONFERENCE_PAGE);
		tabPanel.add(link.asWidget(), "Conferences");
		h = new HTML("<h1>Let's look at jornals</h1>");
		h.setTitle(HistoryTokens.JOURNAL_PAGE);
		tabPanel.add(h, "Journals");

		greetingService
				.getMostPopularAuthorsAndPapers(new AsyncCallback<ContentContainer>() {

					@Override
					public void onFailure(Throwable caught) {

					}

					@Override
					public void onSuccess(ContentContainer result) {
						ArrayList<Author> authors = result.getAuthors();
						String htmlString = "<h1>Most popular authors</h1>";
						for (Author a : authors) {
							htmlString = htmlString + a.renderSRP() + "<br>";
						}
						HTML h = new HTML(htmlString);
						h.setTitle(HistoryTokens.AUTHOR_PAGE);
						tabPanel.add(h, "Authors");

						ArrayList<Paper> papers = result.getPapers();
						htmlString = "<h1>Most popular papers</h1>";

						for (Paper p : papers) {
							htmlString = htmlString + p.renderSRP() + "<br>";
						}
						h = new HTML(htmlString);
						h.setTitle(HistoryTokens.PAPER_PAGE);
						tabPanel.add(h, "Papers");

					}

				});

		tabPanel.addSelectionHandler(new SelectionHandler<Integer>() {
			public void onSelection(SelectionEvent<Integer> event) {
				String tmp = tabPanel.getWidget(event.getSelectedItem())
						.getTitle();
				History.newItem(tmp);
			}
		});

		History.addValueChangeHandler(new ValueChangeHandler<String>() {
			public void onValueChange(ValueChangeEvent<String> event) {
				final String historyToken = event.getValue();

				// Parse the history token
				try {
					final String[] values = historyToken.split("_");
					if (historyToken.startsWith(HistoryTokens.CONFERENCE_PAGE))
						tabPanel.selectTab(0);
					if (historyToken.startsWith(HistoryTokens.JOURNAL_PAGE))
						tabPanel.selectTab(1);
					if (historyToken.startsWith(HistoryTokens.AUTHOR_PAGE)) {
						if (values.length > 1) {
							if (authorPageCach.containsKey(historyToken)) {
								// TODO: runime for cache? or let via server
								// push notifie clients if a cached site is
								// updated?
								AuthorPageContent apc = authorPageCach
										.get(historyToken);
								tabPanel.remove(2);
								HTML html = new HTML(apc.getHtmlString());
								html.setTitle(HistoryTokens.AUTHOR_PAGE + "_"
										+ values[1]);
								tabPanel.insert(html,
										HistoryTokens.AUTHOR_PAGE, 2);
								tabPanel.selectTab(2);
							} else {
								greetingService.displayAuthorPage(values[1],
										new AsyncCallback<AuthorPageContent>() {

											@Override
											public void onFailure(
													Throwable caught) {
												// TODO Auto-generated method
												// stub

											}

											@Override
											public void onSuccess(
													AuthorPageContent res) {

												tabPanel.remove(2);
												HTML html = new HTML(res
														.getHtmlString());
												html.setTitle(HistoryTokens.AUTHOR_PAGE
														+ "_" + values[1]);
												tabPanel.insert(
														html,
														HistoryTokens.AUTHOR_PAGE,
														2);
												tabPanel.selectTab(2);
												authorPageCach.put(
														historyToken, res);

											}
										});
							}
						}
					}
					if (historyToken.startsWith(HistoryTokens.PAPER_PAGE)) {
						if (values.length > 1) {
							greetingService.displayPaperPage(values[1],
									new AsyncCallback<PaperPageContent>() {
										public void onFailure(Throwable caught) {
											// TODO Auto-generated method
											// stub

										}

										public void onSuccess(
												PaperPageContent res) {
											tabPanel.remove(3);
											HTML html = new HTML(res
													.getHtmlString());
											html.setTitle(HistoryTokens.PAPER_PAGE
													+ "_" + values[1]);
											tabPanel.insert(html,
													HistoryTokens.PAPER_PAGE, 3);
											tabPanel.selectTab(3);
										}
									});
						} else {
							tabPanel.selectTab(3);
						}
					}
				} catch (IndexOutOfBoundsException e) {
					tabPanel.selectTab(0);
				}
			}
		});

		tabPanel.selectTab(0);
		RootPanel.get("content").add(tabPanel);

	}

	private void handleCookies() {
		if (Cookies.getCookie("rw.session")==null){
			//http://stackoverflow.com/questions/1382088/session-id-cookie-in-gwt-rpc
		}
	}
}
