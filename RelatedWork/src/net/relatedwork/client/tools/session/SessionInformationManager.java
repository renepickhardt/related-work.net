package net.relatedwork.client.tools.session;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;
import com.google.web.bindery.event.shared.EventBus;
import com.gwtplatform.mvp.client.HandlerContainerImpl;
import net.relatedwork.client.tools.events.LoginEvent;
import net.relatedwork.client.tools.events.LogoutEvent;

/**
 * This manages the {@link SessionInformation} object.
 *
 * @author Xinruo Sun <xiaoruoruo@gmail.com>
 */
@Singleton
public class SessionInformationManager extends HandlerContainerImpl implements Provider<SessionInformation> {

    private final EventBus eventBus;

    private SessionInformation sessionInformation = new SessionInformation();

    @Inject
    public SessionInformationManager(EventBus eventBus) {
        super();
        this.eventBus = eventBus;
    }

    @Override
    protected void onBind() {
        super.onBind();
        registerHandler(eventBus.addHandler(LoginEvent.getType(), loginHandler));
        registerHandler(eventBus.addHandler(LogoutEvent.getType(), logoutHandler));
    }

    @Override
    public SessionInformation get() {
        return sessionInformation;
    }

    private LoginEvent.LoginHandler loginHandler = new LoginEvent.LoginHandler() {
        @Override
        public void onLogin(LoginEvent event) {
            sessionInformation = event.getSession();
        }
    };

    private LogoutEvent.LogoutHandler logoutHandler = new LogoutEvent.LogoutHandler() {
        @Override
        public void onLogout(LogoutEvent event) {
            sessionInformation.stopSession(); // deletes cookies
            sessionInformation = new SessionInformation(); // reset internal variables
            sessionInformation.continueSession(); // register cookie, set userid
        }
    };
}
