package net.relatedwork.client.content;

import net.relatedwork.client.MainPresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.HeadingElement;
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
	@UiField HeadingElement authorName;
	
	
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

	
	
	public void setInSlot(Object slot, Widget content) {
		if (slot == AuthorPresenter.TYPE_SimilarAuthors){
			setSimilarAuthors(content);			
		}
		else if (slot == AuthorPresenter.TYPE_CoAuthors) {
			setCoAuthors(content);			
		} 
		else if (slot == AuthorPresenter.TYPE_CitedAuthors) {
			setCitedAuthors(content);			
		} 
		else if (slot == AuthorPresenter.TYPE_CitedByAuthors) {
			setCitedByAuthors(content);			
		} 
		else if (slot == AuthorPresenter.TYPE_Papers) {
			setPapers(content);			
		} 
		else {
			super.setInSlot(slot, content);
		}
	}
	
	public void setAuthorName(String name){
		authorName.setInnerHTML(name);
	}
	
	private void setPapers(Widget content) {
		paperByAuthor.clear();
		if(content!=null){
			paperByAuthor.add(content);
		}
	}

	private void setCitedByAuthors(Widget content) {
		citedAuthors.clear();
		if (content!=null){
			citedAuthors.add(content);
		}
	}

	private void setCitedAuthors(Widget content) {
		citedByAuthors.clear();
		if (content!=null){
			citedByAuthors.add(content);
		}
		
	}

	private void setCoAuthors(Widget content) {
		coAuthors.clear();
		if (content!=null){
			coAuthors.add(content);
		}
		
	}

	private void setSimilarAuthors(Widget content) {
		similarAuthors.clear();
		if (content != null) {
			similarAuthors.add(content);
		}		
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
