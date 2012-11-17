package net.relatedwork.client;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;

import net.relatedwork.client.layout.BreadcrumbsPresenter;
import net.relatedwork.client.navigation.HistoryTokenChangeEvent;
import net.relatedwork.client.place.NameTokens;

import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Footer = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Breadcrumbs = new Type<RevealContentHandler<?>>();

	
	public interface MyView extends View {
		public HTMLPanel getRwHeader();

		public void setRwHeader(HTMLPanel rwHeader);

		public HTMLPanel getRwContent();

		public void setRwContent(HTMLPanel rwContent);

		public HTMLPanel getRwSidebar();

		public void setRwSidebar(HTMLPanel rwSidebar);

		public HTMLPanel getRwFooter();

		public void setRwFooter(HTMLPanel rwFooter);

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

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Inject FooterPresenter footerPresenter;
	@Inject BreadcrumbsPresenter breadcrumbsPresenter;
	@Inject HomePresenter homePresenter;
	@Override
	protected void onReveal() {
		super.onReveal();
		setInSlot(TYPE_Footer, footerPresenter);
		setInSlot(TYPE_Breadcrumbs, breadcrumbsPresenter);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
//		setInSlot(TYPE_SetMainContent, homePresenter);
		getEventBus().fireEvent(new HistoryTokenChangeEvent(NameTokens.main, "Home"));
	}
}
