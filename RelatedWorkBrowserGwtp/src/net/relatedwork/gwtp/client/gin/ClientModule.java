package net.relatedwork.gwtp.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import net.relatedwork.gwtp.client.place.ClientPlaceManager;
import net.relatedwork.gwtp.client.MainPresenter;
import net.relatedwork.gwtp.client.MainView;
import net.relatedwork.gwtp.client.place.DefaultPlace;
import net.relatedwork.gwtp.client.place.NameTokens;
import net.relatedwork.gwtp.client.staticpresenter.ImprintPresenter;
import net.relatedwork.gwtp.client.staticpresenter.ImprintView;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.main);

		bindPresenter(ImprintPresenter.class, ImprintPresenter.MyView.class,
				ImprintView.class, ImprintPresenter.MyProxy.class);
		
		
	}
}
