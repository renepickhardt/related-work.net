package net.relatedwork.client.sidebar;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.sidebar.SidebarPresenter;

public class WebsiteSidebarPresenter
		extends
		Presenter<WebsiteSidebarPresenter.MyView, WebsiteSidebarPresenter.MyProxy> {

	public interface MyView extends View {
		public void setWebSite(String url);
		public FocusPanel getUrlContainer();
		public void showEnterWebsiteTemplate(boolean show);
		public void showEditIcon(boolean showIcon);
		public Image getEditIcon();
		public Button getSendButton();
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<WebsiteSidebarPresenter> {
	}

	protected boolean isEnterWebsiteTemplateShown;
	protected String url;

	@Inject
	public WebsiteSidebarPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, SidebarPresenter.TYPE_Widgets, this);
	}

	@Override
	protected void onBind() {
		url = null;
		isEnterWebsiteTemplateShown = false;
		getView().getUrlContainer().addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				if (isEnterWebsiteTemplateShown==false){
					if (url == null){
						getView().showEnterWebsiteTemplate(true);
						isEnterWebsiteTemplateShown = true;
					}
					else {
						getView().showEditIcon(true);
					}
				}
			}
		});
		
		getView().getUrlContainer().addMouseOutHandler(new MouseOutHandler() {
			
			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (url == null){
					
				}
				else {
					getView().showEditIcon(false);
				}
			}
		});

		getView().getSendButton().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				Window.alert("initiate rpc call to save new url here");
				getView().showEnterWebsiteTemplate(false);
				isEnterWebsiteTemplateShown = false;
			}
		});
		
		getView().getEditIcon().addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				getView().showEnterWebsiteTemplate(true);
				isEnterWebsiteTemplateShown = true;				
			}
		});
		
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		isEnterWebsiteTemplateShown = false;
		super.onReset();
	}

	public void setWebSite(String url) {
		this.url = url;
		getView().setWebSite(url);
	}
}
