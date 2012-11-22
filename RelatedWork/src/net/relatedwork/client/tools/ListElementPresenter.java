package net.relatedwork.client.tools;

import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.tools.ListPresenter;

public class ListElementPresenter extends
		Presenter<ListElementPresenter.MyView, ListElementPresenter.MyProxy> {

	public interface MyView extends View {
		public FocusPanel getRwListElement();
		public void setRwListElement(FocusPanel rwListElement);
	}

	@ProxyCodeSplit
	public interface MyProxy extends Proxy<ListElementPresenter> {
	}

	@Inject
	public ListElementPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy) {
		super(eventBus, view, proxy);
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, ListPresenter.TYPE_ListElement, this);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().getRwListElement().addMouseOverHandler(new MouseOverHandler() {
			
			@Override
			public void onMouseOver(MouseOverEvent event) {
				Window.alert("mouse over!");
				
			}
		});
	}
}
