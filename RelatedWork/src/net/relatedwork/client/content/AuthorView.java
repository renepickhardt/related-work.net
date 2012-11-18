package net.relatedwork.client.content;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AuthorView extends ViewImpl implements AuthorPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel similarAuthors;
	@UiField HTMLPanel coAuthors;
	@UiField HTMLPanel citedAuthors;
	@UiField HTMLPanel citedByAuthors;	
	@UiField HTMLPanel paperByAuthor;
	
	
	public interface Binder extends UiBinder<Widget, AuthorView> {
	}

	@Inject
	public AuthorView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HTMLPanel getSimilarAuthors() {
		return similarAuthors;
	}

	public void setSimilarAuthors(HTMLPanel similarAuthors) {
		this.similarAuthors = similarAuthors;
	}

	public HTMLPanel getCoAuthors() {
		return coAuthors;
	}

	public void setCoAuthors(HTMLPanel coAuthors) {
		this.coAuthors = coAuthors;
	}

	public HTMLPanel getCitedAuthors() {
		return citedAuthors;
	}

	public void setCitedAuthors(HTMLPanel citedAuthors) {
		this.citedAuthors = citedAuthors;
	}

	public HTMLPanel getCitedByAuthors() {
		return citedByAuthors;
	}

	public void setCitedByAuthors(HTMLPanel citedByAuthors) {
		this.citedByAuthors = citedByAuthors;
	}

	public HTMLPanel getPaperByAuthor() {
		return paperByAuthor;
	}

	public void setPaperByAuthor(HTMLPanel paperByAuthor) {
		this.paperByAuthor = paperByAuthor;
	}
}
