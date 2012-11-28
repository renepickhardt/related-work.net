package net.relatedwork.client.content;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.dom.client.DivElement;
import com.google.gwt.dom.client.HeadingElement;
import com.google.gwt.dom.client.ParagraphElement;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class PaperView extends ViewImpl implements PaperPresenter.MyView {

	private final Widget widget;
	
	@UiField HeadingElement rwTitle;
	@UiField HeadingElement rwAuthors;
	@UiField ParagraphElement rwAbstract;

	public interface Binder extends UiBinder<Widget, PaperView> {
	}

	@Inject
	public PaperView(final Binder binder) {
		widget = binder.createAndBindUi(this);
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

	public HeadingElement getRwAuthors() {
		return rwAuthors;
	}

	public void setRwAuthors(HeadingElement rwAuthors) {
		this.rwAuthors = rwAuthors;
	}

	public ParagraphElement getRwAbstract() {
		return rwAbstract;
	}

	public void setRwAbstract(ParagraphElement rwAbstract) {
		this.rwAbstract = rwAbstract;
	}	
	
}
