package net.relatedwork.client.content;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.client.place.NameTokens;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.DisplayAuthor;
import net.relatedwork.shared.DisplayAuthorResult;
import net.relatedwork.shared.dto.Author;

public class AuthorPresenter extends
		Presenter<AuthorPresenter.MyView, AuthorPresenter.MyProxy> {

	public interface MyView extends View {
		public HTMLPanel getSimilarAuthors();
	}

	@ProxyCodeSplit
	@NameToken(NameTokens.author)
	public interface MyProxy extends ProxyPlace<AuthorPresenter> {
	}

	private DispatchAsync dispatcher;
	
	@Inject
	public AuthorPresenter(final EventBus eventBus, final MyView view,
			final MyProxy proxy,final DispatchAsync dispatcher) {
		super(eventBus, view, proxy);
		this.dispatcher=dispatcher;
	}

	@Override
	protected void revealInParent() {
		RevealContentEvent.fire(this, MainPresenter.TYPE_SetMainContent, this);
	}
	@Override
	protected void onBind() {
		super.onBind();
	}
	@Override
	protected void onReveal() {
		// TODO Auto-generated method stub
		super.onReveal();
		dispatcher.execute(new DisplayAuthor("Bridgeland, Tom"), new AsyncCallback<DisplayAuthorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(DisplayAuthorResult result) {
				getView().getSimilarAuthors().clear();
				for (Author similarAuthor:result.getSimilarAuthors(2)){
					getView().getSimilarAuthors().add(similarAuthor.getLink());
				}
			}
		});
	}
}
