package net.relatedwork.client.tools.star;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class StarView extends ViewImpl implements StarPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwStarPanel;
	
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

	public void setStar() {
		rwStarPanel.setStyleName("rwStarShining");
	}
	
	
	public void removeStar(){
		rwStarPanel.setStyleName("rwStarDead");
	}
	
}

