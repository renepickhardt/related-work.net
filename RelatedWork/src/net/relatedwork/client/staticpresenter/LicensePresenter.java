package net.relatedwork.client.staticpresenter;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;

import net.relatedwork.client.navigation.HistoryTokenChangeEvent;
import net.relatedwork.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;

public class LicensePresenter extends
		Presenter<LicensePresenter.MyView, LicensePresenter.MyProxy> {

	public interface MyView extends View {
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.license)
	public interface MyProxy extends ProxyPlace<LicensePresenter> {
	}

	@Inject
	public LicensePresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}
	
	// A Java method using JSNI from
	// http://googlewebtoolkit.blogspot.co.uk/2008/07/getting-to-really-know-gwt-part-1-jsni.html
	native String sayHelloInJava(String name) /*-{
	  $wnd.sayHello(name); // $wnd is a JSNI synonym for 'window'
	  return "Test JSNI return string";
	}-*/;
	
	@Override
	protected void onReset() {
		super.onReset();
		String js_return = sayHelloInJava("License View");
		getEventBus().fireEvent(new HistoryTokenChangeEvent(NameTokens.license, "License" + js_return));
	}
	
}
