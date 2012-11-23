package net.relatedwork.client.tools.star;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class StarView extends ViewImpl implements StarPresenter.MyView {

	private final Widget widget;

	private boolean starAlive = false;
	@UiField FocusPanel rwStarPanel;
	
	public interface Binder extends UiBinder<Widget, StarView> {
	}

	@Inject
	public StarView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}


	public FocusPanel getRwStarPanel() {
		return rwStarPanel;
	}

	public void setRwStarPanel(FocusPanel rwStarPanel) {
		this.rwStarPanel = rwStarPanel;
	}

	public void setStar() {
		starAlive = true;
		rwStarPanel.removeStyleName("rwStarDead");
		rwStarPanel.setStyleName("rwStarShining");
	}
	
	
	public void removeStar(){
		starAlive = false;
		rwStarPanel.removeStyleName("rwStarShining");
		rwStarPanel.setStyleName("rwStarDead");
	}

	public void swapStar(){
		if (starAlive) {
			removeStar();
		} else {
			setStar();
		}
	}
	
}

