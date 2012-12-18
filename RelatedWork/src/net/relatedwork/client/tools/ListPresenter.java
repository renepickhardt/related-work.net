package net.relatedwork.client.tools;

import java.awt.Button;
import java.util.ArrayList;

import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Author;

import com.gwtplatform.dispatch.annotation.In;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class ListPresenter<T extends IsRenderable> extends
		PresenterWidget<ListPresenter.MyView> {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_ListEntry = new Type<RevealContentHandler<?>>();

	public interface MyView extends View {
		public FocusPanel getListContainer();
		public HTMLPanel getListTitle();
		public void setListTitle(HTMLPanel listTitle);
		public HTMLPanel getListContent();
		public void setListContent(HTMLPanel listContent);
		public void activateWidget();
		public void deActivateWidget();
		public Anchor getRwListMoreLink();
		public void setRwListMoreLink(Anchor rwListMoreLink);
		public HTMLPanel getListOptions();		
		public void setListOptions(HTMLPanel listOptions);
		public void showOptions(boolean b);
		public TextBox getFilterField();
		public void setFilterField(TextBox filterField);
	}

	private int numElements;
	private ArrayList<T> myList;

	@Inject
	public ListPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getView().showOptions(false);
		final FocusPanel fp = getView().getListContainer();
		fp.addMouseOverHandler(new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				getView().showOptions(true);
			}
		});
		fp.addMouseOutHandler(new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				getView().showOptions(false);
			}
		});
		
		final TextBox tb = getView().getFilterField();
		tb.addKeyUpHandler(new KeyUpHandler(){

			@Override
			public void onKeyUp(KeyUpEvent event) {
				renderNewList(applyFiler(tb.getText()), numElements);	
			}});
	}

	//TODO: write a function on elements that matches against the filer
	protected ArrayList<T> applyFiler(String text) {
		ArrayList<T> filteredList = new ArrayList<T>();
		int cnt = 0;
		for (T element:myList){
			if (element.passesFilter(text)){
				filteredList.add(element);
			}
		}
		return filteredList;
	}


	// from
	// http://stackoverflow.com/questions/592046/inject-an-array-of-objects-in-guice
	@Inject
	Provider<ListEntryPresenter<IsRenderable>> provider;
	
	public void setList(ArrayList<T> list, Integer k) {
		myList = list;
		numElements = k;
		renderNewList(list,k);
	}
	
	private void renderNewList(ArrayList<T> list, Integer k){
		getView().getListContent().clear();
		int cnt = 0;
		for (T element : list) {
			// set entry presenters into list presenter
			ListEntryPresenter<T> entryPresenter = (ListEntryPresenter<T>) provider.get();
			entryPresenter.setContent(element);
			setInSlot(TYPE_ListEntry, entryPresenter);
			if (++cnt > k)
				break;
		}

		// set more link action
		Anchor link = getView().getRwListMoreLink();
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setList(myList, 2 * numElements);
			}
		});		
	}
	
	public void addMoreItems(int k){
		if (k<numElements)return;
		int min = Math.min(k, myList.size());
		for (int i=numElements;i<min;i++){
			ListEntryPresenter<T> entryPresenter = (ListEntryPresenter<T>) provider.get();
			entryPresenter.setContent(myList.get(i));
			setInSlot(TYPE_ListEntry, entryPresenter);
		}
		numElements = min;				
	}
	
	public void setTitle(String title) {
		getView().getListTitle().clear();
		getView().getListTitle().add(new HTML("<h2>" + title + "</h2>"));
	}
}
