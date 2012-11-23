package net.relatedwork.client.tools.star;

import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.View;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;

public class StarPresenter extends PresenterWidget<StarPresenter.MyView> {

	public interface MyView extends View {
		public FocusPanel getRwStarPanel();
		public void setRwStarPanel(FocusPanel rwStarPanel);
		public void setStar();
		public void removeStar();
		public void swapStar();
	}

	@Inject
	public StarPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	@Override
	protected void onBind() {
		super.onBind();
		// Bind LoginPopup to LoginLink
		registerHandler(getView().getRwStarPanel().addClickHandler(
				new ClickHandler() {
					
					@Override
					public void onClick(ClickEvent event) {
						// TODO Auto-generated method stub
						getView().swapStar();
					}
				}));
	}
}
