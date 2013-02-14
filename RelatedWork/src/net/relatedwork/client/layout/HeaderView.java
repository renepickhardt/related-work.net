package net.relatedwork.client.layout;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {

	private final Widget widget;

//	@UiField HTMLPanel rwBreadcrumbs;
	@UiField HTMLPanel rwHeaderSearch;
	@UiField HTMLPanel rwLoginControls;

	// Geters and Setters
	
	public HTMLPanel getRwLoginControls() {
		return rwLoginControls;
	}

	public void setRwLoginControls(HTMLPanel rwLoginControls) {
		this.rwLoginControls = rwLoginControls;
	}


//	public HTMLPanel getRwBreadcrumbs() {
//		return rwBreadcrumbs;
//	}

//	public void setRwBreadcrumbs(HTMLPanel rwBreadcrumbs) {
//		this.rwBreadcrumbs = rwBreadcrumbs;
//	}

	public HTMLPanel getRwHeaderSearch() {
		return rwHeaderSearch;
	}

	public void setRwHeaderSearch(HTMLPanel rwHeaderSearch) {
		this.rwHeaderSearch = rwHeaderSearch;
	}

	public interface Binder extends UiBinder<Widget, HeaderView> {
	}
	
	
	
	@Inject
	public HeaderView(final Binder binder) {
		widget = binder.createAndBindUi(this);		
	}

	@Override
	public Widget asWidget() {
		return widget;
	}


	/**
	 * Setup nested presenters Nested presenter 
	 */
	public void setInSlot(Object slot, Widget content) {
//		if (slot == HeaderPresenter.TYPE_Breadcrumbs){
//			setBreadcrumbs(content);
//		} else 
		if (slot == HeaderPresenter.TYPE_Search){
			setSearchBox(content);
		} else if (slot == HeaderPresenter.TYPE_LoginControls){
			setLoginControls(content);
		} else{
			super.setInSlot(slot, content);
		}
	}

	private void setSearchBox(Widget content) {
		rwHeaderSearch.clear();
		if (content != null){
			rwHeaderSearch.add(content);
		}
	}

	private void setLoginControls(Widget content){
		rwLoginControls.clear();
		if (content != null) {
			rwLoginControls.add(content);
		}
	}
	
//	private void setBreadcrumbs(Widget content) {
//		rwBreadcrumbs.clear();
//		if (content != null) {
//			rwBreadcrumbs.add(content);
//		}
//	}	
}
