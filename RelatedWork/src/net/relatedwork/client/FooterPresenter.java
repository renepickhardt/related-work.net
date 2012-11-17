package net.relatedwork.client;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;

public class FooterPresenter extends
		Presenter<FooterPresenter.MyView, FooterPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<FooterPresenter> {
	}

	@Inject
	public FooterPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		//setInSlot(MainPresenter.TYPE_Footer, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
}
