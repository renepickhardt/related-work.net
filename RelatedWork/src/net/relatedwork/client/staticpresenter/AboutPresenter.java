package net.relatedwork.client.staticpresenter;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;

import net.relatedwork.client.Discussions.CommentPresenter;
import net.relatedwork.client.navigation.HistoryTokenChangeEvent;
import net.relatedwork.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;

public class AboutPresenter extends
		Presenter<AboutPresenter.MyView, AboutPresenter.MyProxy> {

	
	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.about)
	public interface MyProxy extends ProxyPlace<AboutPresenter> {
	}

	@Inject
	public AboutPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	
	@Override
	protected void onReveal() {
		super.onReveal();
		
	}
	
	@Override
	protected void onBind() {
		super.onBind();
	}
	
	@Override
	protected void onReset() {
		super.onReset();
		getEventBus().fireEvent(new HistoryTokenChangeEvent(NameTokens.about, "About"));
	}
}
