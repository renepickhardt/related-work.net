package net.relatedwork.client.sidebar;

import net.relatedwork.client.MainPresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class SidebarView extends ViewImpl implements SidebarPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel sidebarWidgets;
	
	public interface Binder extends UiBinder<Widget, SidebarView> {
	}

	@Inject
	public SidebarView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	public HTMLPanel getSidebarWidgets() {
		return sidebarWidgets;
	}

	public void setSidebarWidgets(HTMLPanel sidebarWidgets) {
		this.sidebarWidgets = sidebarWidgets;
	}

	@Override
	public Widget asWidget() {
		return widget;
	}

	public void clearWidgets(){
		sidebarWidgets.clear();
	}
	
	@Override
	public void append(String test) {
		sidebarWidgets.add(new HTML(test));
		
	}
	
	public void setInSlot(Object slot, Widget content) {
		if (slot == SidebarPresenter.TYPE_Widgets){
			addWidget(content);
		}
		else {
			super.setInSlot(slot, content);
		}
	}

	private void addWidget(Widget content) {
		if (content != null) {
			sidebarWidgets.add(content);
		}
	}
}
