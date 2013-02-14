package net.relatedwork.client.sidebar;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.discussion.DiscussionsReloadedEvent;
import net.relatedwork.client.tools.events.SidebarReloadedEvent;
import net.relatedwork.shared.dto.AuthorSidebar;

public class SidebarPresenter extends
		Presenter<SidebarPresenter.MyView, SidebarPresenter.MyProxy> {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Widgets = new Type<RevealContentHandler<?>>();

	public String uri;
	
	public interface MyView extends View {
		public HTMLPanel getSidebarWidgets();
		public void setSidebarWidgets(HTMLPanel sidebarWidgets);
		public void clearWidgets();
		public void append(String test);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<SidebarPresenter> {
	}

	@Inject
	public SidebarPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_Sidebar, this);
	}

	@Override
	protected void onBind() {
		registerHandler(getEventBus().addHandler(SidebarReloadedEvent.getType(), new SidebarReloadedEvent.SidebarReloadedHandler() {
			@Override
			public void onSidebarReloaded(SidebarReloadedEvent event) {
				getView().clearWidgets();
				uri = event.getUri();
				setAuthorSidebar(event.getAuthorSidebar());
			}
		}));
		
		super.onBind();
	}


	
	@Inject WebsiteSidebarPresenter websiteSidebarPresenter;
	@Inject WebsiteSidebarPresenter twitterSidebarPresenter;
	protected void setAuthorSidebar(AuthorSidebar authorSidebar) {
		if (authorSidebar!=null){
			websiteSidebarPresenter.setUri(uri);
			websiteSidebarPresenter.setWebSite(authorSidebar.getWebsite());
			setInSlot(TYPE_Widgets, websiteSidebarPresenter);
		}
	}
	
	
}
