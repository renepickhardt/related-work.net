package net.relatedwork.client.tools;

import java.awt.Button;
import java.util.ArrayList;

import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Author;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.ContentSlot;
import com.gwtplatform.mvp.client.proxy.RevealContentHandler;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.GwtEvent.Type;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Widget;

public class ListPresenter<T extends IsRenderable> extends
		PresenterWidget<ListPresenter.MyView> {

	@ContentSlot
	public static final Type<RevealContentHandler<?>> TYPE_ListEntry = new Type<RevealContentHandler<?>>();

	public interface MyView extends View {
		public HTMLPanel getListTitle();

		public void setListTitle(HTMLPanel listTitle);

		public HTMLPanel getListContent();

		public void setListContent(HTMLPanel listContent);

		public void activateWidget();

		public void deActivateWidget();

		public Anchor getRwListMoreLink();

		public void setRwListMoreLink(Anchor rwListMoreLink);

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
		getEventBus().addHandler(MouseOverEvent.getType(),
				new MouseOverHandler() {
					@Override
					public void onMouseOver(MouseOverEvent event) {
						com.google.gwt.user.client.Window.alert("test");
						getView().activateWidget();
					}
				});
		getEventBus().addHandler(MouseOutEvent.getType(),
				new MouseOutHandler() {
					@Override
					public void onMouseOut(MouseOutEvent event) {
						getView().deActivateWidget();
					}
				});
	}

	public void setList(ArrayList<T> list, int k) {
		myList = list;
		numElements = k;
		getView().getListContent().clear();
		int cnt = 0;

		// final ArrayList<ListEntryPresenter> injectedObjects;
		// injectedObjects = new ArrayList<ListEntryPresenter>();

		// ListEntryPresenter[] listEntryPresenter = getListEntryPresenters(k);

		for (T element : list) {

			/**
			 * focus panel as a container inside horzontal panel HTMLPanel
			 * visible (float Left) HTMLPanel hoverElement (float right)
			 * 
			 * API: setHoverableContent (HTMLPanels?) set visibleContent
			 * (HTMLPanel)
			 */

			// ListEntryPresenter lep = listEntryPresenter[k];
			// setInSlot(TYPE_ListEntry, lep);

			final FocusPanel fp = new FocusPanel();
			fp.setStyleName("rwListEntry");
			HTMLPanel visible = new HTMLPanel("");
			visible.setStyleName("rwVisibleListEntry");
			visible.add(element.getLink());
			final HTMLPanel hover = new HTMLPanel("see me on mouse over");
			hover.setVisible(false);
			hover.setStyleName("rwHoverableListEntry");
			fp.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					hover.setVisible(true);
					fp.setStyleName("rwListEntry-hover");
				}
			});
			fp.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					hover.setVisible(false);
					fp.setStyleName("rwListEntry");
				}
			});
			HTMLPanel hp = new HTMLPanel("");
			hp.add(visible);
			hp.add(hover);
			fp.add(hp);
			getView().getListContent().add(fp);
			if (++cnt > k)
				break;

		}
		Anchor link = getView().getRwListMoreLink();
		link.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setList(myList, 2 * numElements);
			}
		});
		// getView().getListContent().add(link);

	}

	public void setTitle(String title) {
		getView().getListTitle().clear();
		getView().getListTitle().add(new HTML("<h2>" + title + "</h2>"));
	}

	// from
	// http://stackoverflow.com/questions/592046/inject-an-array-of-objects-in-guice
	@Inject
	Provider<ListEntryPresenter> provider;

	ListEntryPresenter[] getListEntryPresenters(int k) {
		ListEntryPresenter[] objects = new ListEntryPresenter[k];
		for (int i = 0; i < k; i++) {
			objects[i] = provider.get();
		}
		return objects;
	}
}
