package net.relatedwork.client.layout;

import java.util.ArrayList;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.shared.ItemSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestion;
import net.relatedwork.shared.dto.RequestGlobalSearchSuggestionResult;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.SuggestOracle.Callback;
import com.google.gwt.user.client.ui.SuggestOracle.Request;
import com.google.gwt.user.client.ui.SuggestOracle.Response;
import com.google.gwt.user.client.ui.SuggestOracle.Suggestion;
import com.google.inject.Inject;

public class HeaderView extends ViewImpl implements HeaderPresenter.MyView {

	private final Widget widget;

	@UiField HTMLPanel rwBreadcrumbs;
	@UiField HTMLPanel rwHeaderSearch;
	

	public HTMLPanel getRwBreadcrumbs() {
		return rwBreadcrumbs;
	}

	public void setRwBreadcrumbs(HTMLPanel rwBreadcrumbs) {
		this.rwBreadcrumbs = rwBreadcrumbs;
	}

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


	public void setInSlot(Object slot, Widget content) {
		if (slot == HeaderPresenter.TYPE_Breadcrumbs){
			setBreadcrumbs(content);
		} else if (slot == HeaderPresenter.TYPE_Search){
			setSearchBox(content);
		}
		else{
			super.setInSlot(slot, content);
		}
	}

	private void setSearchBox(Widget content) {
		rwHeaderSearch.clear();
		if (content != null){
			rwHeaderSearch.add(content);
		}
	}

	private void setBreadcrumbs(Widget content) {
		rwBreadcrumbs.clear();
		if (content != null) {
			rwBreadcrumbs.add(content);
		}
	}	
}
