package net.relatedwork.client.sidebar;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiFactory;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class WebsiteSidebarView extends ViewImpl implements
		WebsiteSidebarPresenter.MyView {

	private final Widget widget;
	
	@UiField FocusPanel urlContainer;
	@UiField HorizontalPanel displayTemplate;
	@UiField Anchor urlLink;
	@UiField Image editIcon;
	
	@UiField HorizontalPanel editTemplate;
	@UiField TextBox urlField;
	@UiField Button sendButton;
	
	public interface Binder extends UiBinder<Widget, WebsiteSidebarView> {
	}

	@Inject
	public WebsiteSidebarView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public FocusPanel getUrlContainer() {
		return urlContainer;
	}
	
	public void setWebSite(String url) {
		if (url==null){
			urlLink.setHref(null);
			urlLink.setText("No website known. Click to set one");
			urlField.setText("http://www.");
		}
		else {
			urlLink.setText(url);
			urlLink.setHref(url);
			urlField.setText(url);
		}
	}
	
	public void showEnterWebsiteTemplate(boolean show){
		displayTemplate.setVisible(!show);
		editTemplate.setVisible(show);
	}

	@Override
	public void showEditIcon(boolean showIcon) {
		editIcon.setVisible(showIcon);
	}
	
	public Button getSendButton() {
		return sendButton;
	}
	
	public Image getEditIcon() {
		return editIcon;
	}
}
