package net.relatedwork.client.tools;

import net.relatedwork.client.content.AuthorPresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class ListView extends ViewImpl implements ListPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel listTitle;
	@UiField HTMLPanel listContent;
	@UiField Anchor rwListMoreLink;
	@UiField HTMLPanel listOptions;
	@UiField FocusPanel listContainer;
	@UiField TextBox filterField;
	
	public TextBox getFilterField() {
		return filterField;
	}

	public void setFilterField(TextBox filterField) {
		this.filterField = filterField;
	}

	public Anchor getRwListMoreLink() {
		return rwListMoreLink;
	}

	public FocusPanel getListContainer() {
		return listContainer;
	}
	
	public HTMLPanel getListOptions() {
		return listOptions;
	}
	
	public void setListOptions(HTMLPanel listOptions) {
		this.listOptions = listOptions;
	}
	public void setRwListMoreLink(Anchor rwListMoreLink) {
		this.rwListMoreLink = rwListMoreLink;
	}
	public interface Binder extends UiBinder<Widget, ListView> {
	}

	@Inject
	public ListView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public HTMLPanel getListTitle() {
		return listTitle;
	}

	public void setListTitle(HTMLPanel listTitle) {
		this.listTitle = listTitle;
	}

	public HTMLPanel getListContent() {
		return listContent;
	}

	public void setListContent(HTMLPanel listContent) {
		this.listContent = listContent;
	}

	public void activateWidget(){
		widget.setStyleName("active");
	}
	public void deActivateWidget(){
		widget.setStyleName("");
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		if (slot == ListPresenter.TYPE_ListEntry) {
			addEntry(content);			
		} 
		else {
			super.setInSlot(slot, content);
		}
	}
	
	private void addEntry(Widget content) {
		if(content!=null){
			listContent.add(content);
		}
	}

	@Override
	public void showOptions(boolean b) {
		listOptions.setVisible(b);
	}
	
}
