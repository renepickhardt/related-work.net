package net.relatedwork.client;

import java.util.ArrayList;

import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestionResult;

import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;

public class MainView extends ViewImpl implements MainPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwHeader;
	@UiField HTMLPanel rwContent;
	@UiField HTMLPanel rwSidebar;
	@UiField HTMLPanel rwFooter;
	@UiField HTMLPanel rwOverlay;
	
	@UiField HeadingElement rwLoadingMessage; 
		
	public interface Binder extends UiBinder<Widget, MainView> {
	}
	
	@Inject
	public MainView(final Binder binder) {
		widget = binder.createAndBindUi(this);
		HTMLPanel p = new HTMLPanel("");
		FormPanel fp = new FormPanel();
		FileUpload fu = new FileUpload();
		Button b = new Button();
		b.setText("Upload a Paper");
		p.add(fu);
		p.add(b);
		fp.add(p);
		rwSidebar.add(fp);
	}
	
	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public HTMLPanel getRwHeader() {
		return rwHeader;
	}

	public void setRwHeader(HTMLPanel rwHeader) {
		this.rwHeader = rwHeader;
	}

	public HTMLPanel getRwContent() {
		return rwContent;
	}

	public void setRwContent(HTMLPanel rwContent) {
		this.rwContent = rwContent;
	}

	public HTMLPanel getRwSidebar() {
		return rwSidebar;
	}

	public void setRwSidebar(HTMLPanel rwSidebar) {
		this.rwSidebar = rwSidebar;
	}

	public HTMLPanel getRwFooter() {
		return rwFooter;
	}

	public void setRwFooter(HTMLPanel rwFooter) {
		this.rwFooter = rwFooter;
	}

	
	public void showLoadingOverlay(String message) {
		this.rwOverlay.setStyleName("rwOverlay");
		this.rwLoadingMessage.setInnerHTML("Loading <br>" + message);
	}
	
	public void hideLoadingOverlay() {
		this.rwOverlay.setStyleName("rwOverlayHidden");
	}
	
	
	// Nested presenter setters

	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == MainPresenter.TYPE_Footer){
			setFooter(content);
		}
		else if (slot == MainPresenter.TYPE_SetMainContent) {
			setMainContent(content);
		}
		else if (slot == MainPresenter.TYPE_Header) {
			setHeader(content);
		} 
		else {
			super.setInSlot(slot, content);
		}
	}
	
	
	private void setHeader(Widget content) {
		rwHeader.clear();
		if (content != null) {
			rwHeader.add(content);
		}
	}

	private void setFooter(Widget content) {
		rwFooter.clear();
		if (content != null) {
			rwFooter.add(content);
		}
	}

	private void setMainContent(Widget content) {
		rwContent.clear();
		if (content != null) {
			rwContent.add(content);
		}
	}	
}
