package net.relatedwork.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import net.relatedwork.client.place.ClientPlaceManager;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.MainView;
import net.relatedwork.client.place.DefaultPlace;
import net.relatedwork.client.place.NameTokens;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.main);
	}
}
