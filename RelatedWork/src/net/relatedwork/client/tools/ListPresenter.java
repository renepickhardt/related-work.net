package net.relatedwork.client.tools;

import java.util.ArrayList;

import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Author;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.gargoylesoftware.htmlunit.javascript.host.Window;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;

public class ListPresenter<T extends IsRenderable> extends PresenterWidget<ListPresenter.MyView> {

	public interface MyView extends View {
		public HTMLPanel getListTitle();
		public void setListTitle(HTMLPanel listTitle);
		public HTMLPanel getListContent();
		public void setListContent(HTMLPanel listContent);
		public void activateWidget();
		public void deActivateWidget();
	}

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

	public void setList(ArrayList<T> list) {
		getView().getListContent().clear();
		for (T element: list){
			getView().getListContent().add(element.getLink());	
		}
	}
	
	public void setTitle(String title){
		getView().getListTitle().clear();
		getView().getListTitle().add(new HTML("<h2>"+title+"</h2>"));
	}
}
