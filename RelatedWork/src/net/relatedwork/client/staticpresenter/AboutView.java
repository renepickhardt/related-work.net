package net.relatedwork.client.staticpresenter;

import net.relatedwork.client.MainPresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class AboutView extends ViewImpl implements AboutPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwDiscussions;

	
	public interface Binder extends UiBinder<Widget, AboutView> {
	}

	@Inject
	public AboutView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	@Override
	public void setInSlot(Object slot, Widget content) {
		// TODO Auto-generated method stub
		
		if (slot == AboutPresenter.TYPE_Discussion) {
			setDiscussions(content);
		} else { 
			super.setInSlot(slot, content);
		}
	}
	
	private void setDiscussions(Widget content) {
		rwDiscussions.clear();
		if (content != null) {
			rwDiscussions.add(content);
		}		
	}

}
