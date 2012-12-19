package net.relatedwork.client.layout;

import net.relatedwork.client.navigation.HistoryTokenChangeEvent;
import net.relatedwork.client.navigation.HistoryTokenChangeEvent.HistoryTokenChangeHandler;
import net.relatedwork.client.place.NameTokens;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.web.bindery.event.shared.EventBus;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Hyperlink;
import com.google.gwt.user.client.ui.Label;

public class BreadcrumbsPresenter extends
		PresenterWidget<BreadcrumbsPresenter.MyView> {

	public interface MyView extends View {
		public HTMLPanel getBreadcrumbContainer();
		public void setBreadcrumbContainer(HTMLPanel breadcrumbContainer);	
	}

	@Inject
	public BreadcrumbsPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		getEventBus().addHandler(HistoryTokenChangeEvent.getType(), new HistoryTokenChangeHandler(){
			@Override
			public void onHistoryTokenChange(HistoryTokenChangeEvent event) {
				getView().getBreadcrumbContainer().clear();
				Hyperlink h = new Hyperlink();
				h.setTargetHistoryToken(NameTokens.main);
				h.setText("Home");
				h.setStyleName("float");
				getView().getBreadcrumbContainer().add(h);
				Label l = new Label(" > ");
				l.setStyleName("float");
				getView().getBreadcrumbContainer().add(l);
				Hyperlink h1 = new Hyperlink();
				h1.setTargetHistoryToken(event.getNameToken());
				h1.setText(event.getTitle());
				h1.setStyleName("float");
				getView().getBreadcrumbContainer().add(h1);
				
			}});
	}
}
