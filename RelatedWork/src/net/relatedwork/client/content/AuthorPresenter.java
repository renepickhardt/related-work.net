package net.relatedwork.client.content;

import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyCodeSplit;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.ProxyPlace;
import com.gwtplatform.mvp.client.proxy.RevealContentEvent;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.discussion.CommentsPanelPresenter;
import net.relatedwork.client.discussion.DiscussionsReloadedEvent;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.tools.ListPresenter;
import net.relatedwork.client.tools.events.LoadingOverlayEvent;
import net.relatedwork.client.tools.session.SessionInformationManager;
import net.relatedwork.shared.dto.Author;
import net.relatedwork.shared.dto.DisplayAuthor;
import net.relatedwork.shared.dto.DisplayAuthorResult;
import net.relatedwork.shared.dto.Paper;

public class AuthorPresenter extends
		Presenter<AuthorPresenter.MyView, AuthorPresenter.MyProxy> {

	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_SimilarAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_CoAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_CitedAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_CitedByAuthors = new Type<RevealContentHandler<?>>();
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_Papers = new Type<RevealContentHandler<?>>();
	
	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_Discussion = new Type<RevealContentHandler<?>>();

	
	public interface MyView extends View {
		public void setAuthorName(String name);
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
	@Inject ListPresenter<Paper> paperListPresenter;
    @Inject SessionInformationManager sessionInformationManager;
	
	@Inject CommentsPanelPresenter commentPresenter;
	
	@Override
	protected void onReveal() {
		// TODO Auto-generated method stub
		super.onReveal();

		setInSlot(TYPE_SimilarAuthors, similarAuthorsListPresenter);
		setInSlot(TYPE_CitedAuthors, citedAuthorsListPresenter);
		setInSlot(TYPE_CoAuthors, coAuthorsListPresenter);
		setInSlot(TYPE_CitedByAuthors, citedByAuthorsListPresenter);
		setInSlot(TYPE_Papers, paperListPresenter);
		setInSlot(TYPE_Discussion, commentPresenter);
	}
	
	@Override
	public void prepareFromRequest(PlaceRequest request) {
		super.prepareFromRequest(request);
		
		String author_url = request.getParameter("q", "None");
		
		// Log author visit
		sessionInformationManager.get().logAuthor(author_url);
		
		// show Loading Overlay
		getEventBus().fireEvent(new LoadingOverlayEvent(true));
		
		dispatcher.execute(new DisplayAuthor(author_url), new AsyncCallback<DisplayAuthorResult>() {
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				getView().setAuthorName("Faild Request");	

				// hide Loading overlay
				getEventBus().fireEvent(new LoadingOverlayEvent(false));

			}
			
			@Override
			public void onSuccess(DisplayAuthorResult result) {
                Author author = new Author();
                author.setDisplayName(result.getName());
                author.setUri(result.getUri());

				getView().setAuthorName(result.getName());
				
				similarAuthorsListPresenter.setTitle("Similar authors");
				similarAuthorsListPresenter.setList(result.getSimilarAuthors(25),5);
				
				citedAuthorsListPresenter.setTitle("cites:");
				citedAuthorsListPresenter.setList(result.getCitedAuthors(25),5);
				
				citedByAuthorsListPresenter.setTitle("cited by:");
				citedByAuthorsListPresenter.setList(result.getCitedByAuthors(25),5);
				
				coAuthorsListPresenter.setTitle("Coauthors");
				coAuthorsListPresenter.setList(result.getCoAuthors(25),5);
				
				paperListPresenter.setTitle("Articles");
				paperListPresenter.setList(result.getWrittenPapers(30),10);
				
				// hide Loading overlay
				getEventBus().fireEvent(new LoadingOverlayEvent(false));

				getEventBus().fireEvent(new DiscussionsReloadedEvent(author.getUri(), result.getComments()));
			}
		});
		
	}
}
