package net.relatedwork.client.header;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;

public class HeaderPresenter extends
		Presenter<HeaderPresenter.MyView, HeaderPresenter.MyProxy> {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_Breadcrumbs = new Type<RevealContentHandler<?>>();
	
	public interface MyView extends View {
		public void setRwBreadcrumbs(HTMLPanel rwBreadcrumbs);	
		public void setRwHeaderSearch(HTMLPanel rwHeaderSearch);
		public HTMLPanel getRwHeaderSearch();
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
		RevealContentEvent.fire(this, MainPresenter.TYPE_Breadcrumbs, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
