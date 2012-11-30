package net.relatedwork.client.content;

import java.io.IOException;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.client.place.NameTokens;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.dto.DisplayPaper;
import net.relatedwork.shared.dto.DisplayPaperResult;

public class PaperPresenter extends
		Presenter<PaperPresenter.MyView, PaperPresenter.MyProxy> {

	public interface MyView extends View {
		public HeadingElement getRwTitle();
		public void setRwTitle(HeadingElement rwTitle);
		public HeadingElement getRwAuthors();
		public void setRwAuthors(HeadingElement rwAuthors);
		public ParagraphElement getRwAbstract();
		public void setRwAbstract(ParagraphElement rwAbstract);	
		
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.paper)
	public interface MyProxy extends ProxyPlace<PaperPresenter> {
	}

	@Inject
	public PaperPresenter(final EventBus eventBus, final MyView view,
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

	@Inject DispatchAsync dispatcher;
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		
		// Get paper_id form query string
		String paper_id = request.getParameter("q", "None");

		// Get paper data from server	
		dispatcher.execute(new DisplayPaper(paper_id), new AsyncCallback<DisplayPaperResult>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess(DisplayPaperResult result) {
				getView().getRwTitle().setInnerHTML(result.getTitle());
				getView().getRwAuthors().setInnerHTML(result.getAuthors());
				getView().getRwAbstract().setInnerHTML(result.getAbstract());
			}
		});
		
	}

}
