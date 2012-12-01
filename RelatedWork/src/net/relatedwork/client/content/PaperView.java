package net.relatedwork.client.content;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.dom.client.UListElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class PaperView extends ViewImpl implements PaperPresenter.MyView {

	private final Widget widget;
	
	@UiField HeadingElement rwTitle;
	@UiField HTMLPanel rwAuthorPanel;
	@UiField ParagraphElement rwAbstract;
	@UiField HTMLPanel rwCitations;
	@UiField HTMLPanel rwCitedBy;
	
	public interface Binder extends UiBinder<Widget, PaperView> {
	}

	@Inject
	public PaperView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == PaperPresenter.TYPE_Citations) {
			setCitations(content);
		} else if (slot == PaperPresenter.TYPE_CitedBy) {
			setCitedBy(content);
		} else {
			super.setInSlot(slot, content);
		}
	}
	
	private void setCitations(Widget content) {
		rwCitations.clear();
		if (content != null) {
			rwCitations.add(content);
		}	
	}


	private void setCitedBy(Widget content) {
		rwCitedBy.clear();
		if (content != null){
			rwCitedBy.add(content);
		}
	}


	@Override
	public Widget asWidget() {
		return widget;
	}

	public HeadingElement getRwTitle() {
		return rwTitle;
	}

	public void setRwTitle(HeadingElement rwTitle) {
		this.rwTitle = rwTitle;
	}


	public ParagraphElement getRwAbstract() {
		return rwAbstract;
	}

	public void setRwAbstract(ParagraphElement rwAbstract) {
		this.rwAbstract = rwAbstract;
	}

	public HTMLPanel getRwAuthorPanel() {
		return rwAuthorPanel;
	}

	public void setRwAuthorPanel(HTMLPanel rwAuthorPanel) {
		this.rwAuthorPanel = rwAuthorPanel;
	}


	public HTMLPanel getRwCitations() {
		return rwCitations;
	}


	public void setRwCitations(HTMLPanel rwCitations) {
		this.rwCitations = rwCitations;
	}


	public HTMLPanel getRwReferences() {
		return rwCitedBy;
	}


	public void setRwReferences(HTMLPanel rwReferences) {
		this.rwCitedBy = rwReferences;
	}	

	
	
	
}
