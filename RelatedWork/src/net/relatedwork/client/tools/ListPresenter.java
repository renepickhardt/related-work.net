package net.relatedwork.client.tools;

import java.util.ArrayList;

import net.relatedwork.shared.IsRenderable;
import net.relatedwork.shared.dto.Author;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
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
	}

	@Inject
	public ListPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
	}

	public void setList(ArrayList<T> list) {
		getView().getListContent().clear();
		for (T element: list){
			getView().getListContent().add(element.getLink());	
		}
	}
	
	public void setTitle(String title){
		getView().getListTitle().clear();
		getView().getListTitle().add(new HTML("<h2>title</h2>"));
	}
}
