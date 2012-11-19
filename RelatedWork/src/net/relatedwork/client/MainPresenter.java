package net.relatedwork.client;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;

import net.relatedwork.client.Discussions.CommentPresenter;
import net.relatedwork.client.header.HeaderPresenter;
import net.relatedwork.client.handler.StartSearchHandler;
import net.relatedwork.client.layout.BreadcrumbsPresenter;
import net.relatedwork.client.login.LoginPopupPresenter;
import net.relatedwork.client.navigation.HistoryTokenChangeEvent;
import net.relatedwork.client.place.NameTokens;

import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class MainPresenter extends
		Presenter<MainPresenter.MyView, MainPresenter.MyProxy> {
	
	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_SetMainContent = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Discussion = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Footer = new Type<RevealContentHandler<?>>();

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Header = new Type<RevealContentHandler<?>>();

	
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
	@Inject HomePresenter homePresenter;
	@Inject CommentPresenter commentPresenter;
	@Inject HeaderPresenter headerPresenter;

	@Override
	protected void onReveal() {
		super.onReveal();
		setInSlot(TYPE_Footer, footerPresenter);
		setInSlot(TYPE_Discussion, commentPresenter);
		setInSlot(TYPE_Header, headerPresenter);
	}
	
	@Override
	protected void onReset() {
		super.onReset();
//		setInSlot(TYPE_SetMainContent, homePresenter);
		getEventBus().fireEvent(new HistoryTokenChangeEvent(NameTokens.main, "Home"));
	}
}
