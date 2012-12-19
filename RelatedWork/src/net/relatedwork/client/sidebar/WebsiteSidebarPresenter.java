package net.relatedwork.client.sidebar;

import java.util.HashMap;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.regexp.shared.RegExp;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBox;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.sidebar.SidebarPresenter;
import net.relatedwork.shared.dto.SetAuthorMetaDataActionHandler;
import net.relatedwork.shared.dto.SetAuthorMetaDataActionHandlerResult;

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
		public TextBox getUrlField();
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
				if (isEnterWebsiteTemplateShown == false) {
					if (url == null) {
						getView().showEnterWebsiteTemplate(true);
						isEnterWebsiteTemplateShown = true;
					} else {
						getView().showEditIcon(true);
					}
				}
			}
		});

		getView().getUrlContainer().addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				if (url == null) {

				} else {
					getView().showEditIcon(false);
				}
			}
		});

		getView().getSendButton().addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				storeUrl();
			}
		});
		
		getView().getUrlField().addKeyUpHandler(new KeyUpHandler() {
			
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER)
					storeUrl();
				if (event.getNativeKeyCode() == KeyCodes.KEY_ESCAPE)
					getView().setWebSite(null);
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

	@Inject DispatchAsync dispatcher;
	
	public void storeUrl() {
		final String url = getView().getUrlField().getText();
		if (isValidUrl(url,true)){
			
			HashMap<String,String> metaData = new HashMap<String,String>();
			Window.alert(uri);
			metaData.put("uri",uri);
			metaData.put("url", url);
			dispatcher.execute(new SetAuthorMetaDataActionHandler(metaData), new AsyncCallback<SetAuthorMetaDataActionHandlerResult>() {
				@Override
				public void onFailure(Throwable caught) {
					Window.alert("could not connect to server. Maybe your internet connection is down?");					
				}

				@Override
				public void onSuccess(
						SetAuthorMetaDataActionHandlerResult result) {
					getView().showEnterWebsiteTemplate(false);
					getView().setWebSite(url);
					isEnterWebsiteTemplateShown = false;					
				}
			});
		}
		else 
			Window.alert("could not store Weblink. Pleas check if the link is really working or if your network connection is alive.");
	}

	
	//from: http://stackoverflow.com/questions/4498225/gwt-java-url-validator
	private RegExp urlValidator;
	private RegExp urlPlusTldValidator;
	private String uri;
	public boolean isValidUrl(String url, boolean topLevelDomainRequired) {
	    if (urlValidator == null || urlPlusTldValidator == null) {
	        urlValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]+)*){1}$");
	        urlPlusTldValidator = RegExp.compile("^((ftp|http|https)://[\\w@.\\-\\_]+\\.[a-zA-Z]{2,}(:\\d{1,5})?(/[\\w#!:.?+=&%@!\\_\\-/]+)*){1}$");
	    }
	    return (topLevelDomainRequired ? urlPlusTldValidator : urlValidator).exec(url) != null;
	}

	public void setUri(String uri) {
		this.uri=uri;		
	}
}
