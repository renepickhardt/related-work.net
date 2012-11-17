package net.relatedwork.gwtp.client.staticpresenter;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;

import net.relatedwork.gwtp.client.MainPresenter;
import net.relatedwork.gwtp.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class ImprintPresenter extends
		Presenter<ImprintPresenter.MyView, ImprintPresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.imprint)
	public interface MyProxy extends ProxyPlace<ImprintPresenter> {
	}

	@Inject
	public ImprintPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
//		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	@Override
	protected void onReset() {
		super.onReset();
		getEventBus().fireEvent(new MyTestEvent("Imprint"));
	}
}
