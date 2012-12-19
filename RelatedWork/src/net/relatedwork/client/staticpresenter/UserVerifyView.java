package net.relatedwork.client.staticpresenter;

import com.gwtplatform.mvp.client.ViewImpl;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;


public class UserVerifyView extends ViewImpl implements
		UserVerifyPresenter.MyView {

	private final Widget widget;

	@UiField Label rwUsername;
	@UiField Label rwSuccess;
	
	public interface Binder extends UiBinder<Widget, UserVerifyView> {
	}

	@Inject
	public UserVerifyView(final Binder binder) {
		widget = binder.createAndBindUi(this);
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void showSuccess(String email){
		rwUsername.setText(email);
		rwSuccess.setText("Success!");
	}
	
	public void showFailed(String email){
		rwUsername.setText(email);
		rwSuccess.setText("Failed");		
	}
	
}
