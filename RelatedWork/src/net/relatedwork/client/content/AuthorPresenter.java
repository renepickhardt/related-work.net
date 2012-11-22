package net.relatedwork.client.content;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.annotations.NameToken;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.tools.ListPresenter;

import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.DisplayAuthor;
import net.relatedwork.shared.dto.DisplayAuthorResult;

public class AuthorPresenter extends
		Presenter<AuthorPresenter.MyView, AuthorPresenter.MyProxy> {

	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_SimilarAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_CoAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_CitedAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_CitedByAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_Papers = new Type<RevealContentHandler<?>>();
	
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
	
	@Inject ListPresenter<Author> similarAuthorsListPresenter;
	@Inject ListPresenter<Author> coAuthorsListPresenter;
	@Inject ListPresenter<Author> citedAuthorsListPresenter;
	@Inject ListPresenter<Author> citedByAuthorsListPresenter;
	//TODO: change to Paper template (need to create Paper implements IsRenderable first)
	@Inject ListPresenter<Author> paperListPresenter;
	
	@Override
	protected void onReveal() {
		// TODO Auto-generated method stub
		super.onReveal();

		setInSlot(TYPE_SimilarAuthors, similarAuthorsListPresenter);
		setInSlot(TYPE_CitedAuthors, citedAuthorsListPresenter);
		setInSlot(TYPE_CoAuthors, coAuthorsListPresenter);
		setInSlot(TYPE_CitedByAuthors, citedByAuthorsListPresenter);
		setInSlot(TYPE_Papers, paperListPresenter);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		// TODO Auto-generated method stub
		super.prepareFromRequest(request);
		dispatcher.execute(new DisplayAuthor(request.getParameter("q", "Bridgeland")), new AsyncCallback<DisplayAuthorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				
			}
			@Override
			public void onSuccess(DisplayAuthorResult result) {
				similarAuthorsListPresenter.setTitle(result.getName() + " has similar authors");
				similarAuthorsListPresenter.setList(result.getSimilarAuthors(25),5);
				
				citedAuthorsListPresenter.setTitle(result.getName() + " cites:");
				citedAuthorsListPresenter.setList(result.getCitedAuthors(25),5);
				
				citedByAuthorsListPresenter.setTitle(result.getName() + " is cited by:");
				citedByAuthorsListPresenter.setList(result.getCitedByAuthors(25),5);
				
				coAuthorsListPresenter.setTitle("important coauthors of " + result.getName());
				coAuthorsListPresenter.setList(result.getCoAuthors(25),5);
				
				paperListPresenter.setTitle("Bridgeland composed papers");
				paperListPresenter.setList(result.getSimilarAuthors(5),5);
			}
		});
		
	}
}
