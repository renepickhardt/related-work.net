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

public class ListPresenter<T extends IsRenderable> extends PresenterWidget<ListPresenter.MyView> {

	@ContentSlot public static final Type<RevealContentHandler<?>> TYPE_ListElement = new Type<RevealContentHandler<?>>();

	
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
		getEventBus().addHandler(MouseOverEvent.getType(), new MouseOverHandler() {
			@Override
			public void onMouseOver(MouseOverEvent event) {
				com.google.gwt.user.client.Window.alert("test");
				getView().activateWidget();
			}
		});
		getEventBus().addHandler(MouseOutEvent.getType(), new MouseOutHandler() {
			@Override
			public void onMouseOut(MouseOutEvent event) {
				getView().deActivateWidget();
			}
		});
	}

//	@Inject ArrayList<ListElementPresenter> listElementPresenter;
	public void setList(ArrayList<T> list, int k) {
		myList = list;
		numElements = k;
		getView().getListContent().clear();
		int cnt = 0;
		for (T element: list){
			FocusPanel fp = new FocusPanel();
			HorizontalPanel hp = new HorizontalPanel();
			HTMLPanel visible = new HTMLPanel("");
			visible.add(element.getLink());
			final HTMLPanel hover = new HTMLPanel("see me on mouse over");
			hover.setVisible(false);
			fp.addMouseOverHandler(new MouseOverHandler() {
				@Override
				public void onMouseOver(MouseOverEvent event) {
					hover.setVisible(true);
				}
			});
			fp.addMouseOutHandler(new MouseOutHandler() {
				@Override
				public void onMouseOut(MouseOutEvent event) {
					hover.setVisible(false);
				}
			});
			hp.add(visible);
			hp.add(hover);
			fp.add(hp);
			getView().getListContent().add(fp);
			if (++cnt > k)break;
		}
		Anchor link = getView().getRwListMoreLink();
		link.addClickHandler(new ClickHandler() {			
			@Override
			public void onClick(ClickEvent event) {
				setList(myList, 2*numElements);
			}
		});
//		getView().getListContent().add(link);

	}
	
	public void setTitle(String title){
		getView().getListTitle().clear();
		getView().getListTitle().add(new HTML("<h2>"+title+"</h2>"));
	}
}
