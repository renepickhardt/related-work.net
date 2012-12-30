package net.relatedwork.client;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;
import net.relatedwork.client.content.HomePresenter;
import net.relatedwork.client.layout.BreadcrumbsPresenter;
import net.relatedwork.client.layout.FooterPresenter;
import net.relatedwork.client.layout.HeaderPresenter;
import net.relatedwork.client.navigation.HistoryTokenChangeEvent;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.tools.events.LoadingOverlayEvent;
import net.relatedwork.client.tools.events.LoadingOverlayEvent.LoadingOverlayHandler;
import net.relatedwork.client.tools.events.LoginEvent;
import net.relatedwork.client.tools.session.SessionInformation;
import net.relatedwork.client.tools.session.SessionInformationManager;


public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> {
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Footer = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Header = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Sidebar = new Type<RevealContentHandler<?>>();

	
	public interface MyView extends View {
		public HTMLPanel getRwHeader();
		public void setRwHeader(HTMLPanel rwHeader);
		public HTMLPanel getRwContent();
		public void setRwContent(HTMLPanel rwContent);
		public HTMLPanel getRwSidebar();
		public void setRwSidebar(HTMLPanel rwSidebar);
		public HTMLPanel getRwFooter();
		public void setRwFooter(HTMLPanel rwFooter);
		
		public void showLoadingOverlay(String message);	
		public void hideLoadingOverlay();
	}
	
	@ProxyCodeSplit
	@NameToken(NameTokens.main)
	public interface MyProxy extends ProxyPlace<MainPresenter> {
	}

	@Inject
	public MainPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Inject FooterPresenter footerPresenter;
	@Inject HomePresenter homePresenter;
	@Inject HeaderPresenter headerPresenter;
	@Inject BreadcrumbsPresenter breadcrumbsPresenter;
    @Inject SessionInformationManager sessionInformationManager;

	@Inject DispatchAsync dispatchAsync;
	
	@Override
	protected void onBind() {
		super.onBind();
		registerHandler(getEventBus().addHandler(LoadingOverlayEvent.getType(), overlayHandler));
	}
	

	@Override
	protected void onReveal() {
		super.onReveal();
		setInSlot(TYPE_Footer, footerPresenter);
		setInSlot(TYPE_Header, headerPresenter);

        SessionInformation sessionInformation = sessionInformationManager.get();
		// Register Session
		sessionInformation.continueSession();

		// fire Login/Logout depending on wether we continue a user session
		if (sessionInformation.isLoggedIn()) {
			getEventBus().fireEvent(new LoginEvent(sessionInformation));
		}

		// Remark: RPC calls have to be in onReveal!
		// Does not work at onBind, onReset! -> null object exception
	}
	
	@Override
	protected void onReset() {
		super.onReset();
//		setInSlot(TYPE_SetMainContent, homePresenter);
		getEventBus().fireEvent(new HistoryTokenChangeEvent(NameTokens.main, "Home"));
	}
	

	/**
	 * Loading overlay 
	 */

	private static Integer overlayCount = 0; 
	
	LoadingOverlayHandler overlayHandler = new LoadingOverlayHandler() {
		public void onLoadingOverlay(LoadingOverlayEvent event) {
//			Window.alert("Handling Overlay. Count " + overlayCount.toString() );
			
			if (event.getShow() == true) {
				// request to show overlay
				overlayCount++;
				
				getView().showLoadingOverlay("wating for " + overlayCount.toString() + " requests.");
												
			} 
			else {
				// request to hide
				overlayCount--;
				
				if (overlayCount > 0){
					getView().showLoadingOverlay("wating for " + overlayCount.toString() + " requests.");
				} else {
					getView().hideLoadingOverlay();
					overlayCount = 0;
				}
				
			}
			
		};
	};
}

