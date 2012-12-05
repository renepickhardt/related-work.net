package net.relatedwork.client.layout;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.handler.StartSearchHandler;
import net.relatedwork.client.tools.events.LoginEvent;
import net.relatedwork.client.tools.events.LoginEvent.LoginHandler;
import net.relatedwork.client.tools.login.LoginControlsPresenter;
import net.relatedwork.client.tools.login.LoginPopupPresenter;
import net.relatedwork.client.tools.SearchPresenter;
import net.relatedwork.shared.dto.LoginActionResult;

public class HeaderPresenter extends
		Presenter<HeaderPresenter.MyView, HeaderPresenter.MyProxy> {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Breadcrumbs = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Search = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_LoginControls = new Type<RevealContentHandler<?>>();
	
	public interface MyView extends View {
		public HTMLPanel getRwBreadcrumbs();
		public void setRwBreadcrumbs(HTMLPanel rwBreadcrumbs);
		public HTMLPanel getRwHeaderSearch();
		public void setRwHeaderSearch(HTMLPanel rwHeaderSearch);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<HeaderPresenter> {
	}

	@Inject
	public HeaderPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_Header, this);
	}

	@Inject BreadcrumbsPresenter breadcrumbsPresenter;
	@Inject LoginPopupPresenter loginPopupPresenter;
	@Inject SearchPresenter searchPresenter;
	@Inject LoginControlsPresenter loginControlsPresenter;
	@Inject DispatchAsync dispatcher; 

	protected void onReveal() {
		super.onReveal();
		setInSlot(TYPE_Breadcrumbs, breadcrumbsPresenter);
		setInSlot(TYPE_Search, searchPresenter);
		setInSlot(TYPE_LoginControls,loginControlsPresenter);
	}
	
	
}
