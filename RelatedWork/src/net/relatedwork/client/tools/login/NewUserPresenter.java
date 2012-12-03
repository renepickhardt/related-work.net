package net.relatedwork.client.tools.login;

import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.tools.events.ClearPopupsEvent;
import net.relatedwork.client.tools.events.ClearPopupsEvent.ClearPopupsHandler;
import net.relatedwork.shared.dto.NewUserAction;
import net.relatedwork.shared.dto.NewUserActionResult;

import com.gwtplatform.dispatch.shared.DispatchAsync;
import com.gwtplatform.mvp.client.PresenterWidget;
import com.gwtplatform.mvp.client.PopupView;
import com.google.inject.Inject;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.TextBox;

public class NewUserPresenter extends PresenterWidget<NewUserPresenter.MyView> {

	public interface MyView extends PopupView {
		public TextBox getRwNewUsername();
		public void setRwNewUsername(TextBox rwNewUsername);
		public TextBox getRwNewEmail();
		public void setRwNewEmail(TextBox rwNewEmail);
		public TextBox getRwNewPassword();
		public void setRwNewPassword(TextBox rwNewPassword);
		public TextBox getRwNewPasswordRepeat();
		public void setRwNewPasswordRepeat(TextBox rwNewPasswordRepeat);
		public Button getRwNewUserSubmit();
		public void setRwNewUserSubmit(Button rwNewUserSubmit);
		public void clearFields();
		public void mockPassword();
	}

	@Inject
	public NewUserPresenter(final EventBus eventBus, final MyView view) {
		super(eventBus, view);
	}

	private boolean fromIsCorrect(){
		if( ! getView().getRwNewPassword().getText().equals(getView().getRwNewPasswordRepeat().getText()) ) {
			getView().mockPassword();
			return false;
		} 
		// TODO: Add email check
		return true;
	}
	
	@Inject DispatchAsync dispatcher;
	
	@Override
	protected void onBind() {
		super.onBind();
		
		registerHandler(getView().getRwNewUserSubmit().addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if ( fromIsCorrect() ) {
				dispatcher.execute(
						new NewUserAction(
								getView().getRwNewUsername().getText(),
								Integer.toString(getView().getRwNewPassword().getText().hashCode()),
								getView().getRwNewEmail().getText(), 
								MainPresenter.getSessionInformation()
								), 
						
						new AsyncCallback<NewUserActionResult>(){
							@Override
							public void onFailure(Throwable caught) {
								// TODO Auto-generated method stub
								
							}
							@Override
							public void onSuccess(NewUserActionResult result) {

								// TODO: Display new user info
								Window.alert("New user request send. Check your mails to confirm.");
								
//								// Login user !!
//								getEventBus().fireEvent(new LoginEvent(result.getSession()));
//								
								// ClearFields
								getView().clearFields();
								
								// Close all 
								getView().hide();
								getEventBus().fireEvent(new ClearPopupsEvent());
							}
							}
						);
				}
			}
		}));

		registerHandler(getEventBus().addHandler(ClearPopupsEvent.getType(), new ClearPopupsHandler() {
			@Override
			public void onclearP(ClearPopupsEvent event) {
				// TODO Auto-generated method stub
				getView().hide();
			}
		}
		));
	}

}
