package net.relatedwork.gwtp.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import net.relatedwork.gwtp.client.place.ClientPlaceManager;
import net.relatedwork.gwtp.client.core.MainPagePresenter;
import net.relatedwork.gwtp.client.core.MainPageView;
import net.relatedwork.gwtp.client.place.DefaultPlace;
import net.relatedwork.gwtp.client.place.NameTokens;
import net.relatedwork.gwtp.client.core.ResponsePresenter;
import net.relatedwork.gwtp.client.core.ResponseView;
import net.relatedwork.gwtp.client.core.AuthorPagePresenter;
import net.relatedwork.gwtp.client.core.AuthorPageView;
import net.relatedwork.gwtp.client.core.LoadDataToServletContextPresenter;
import net.relatedwork.gwtp.client.core.LoadDataToServletContextView;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));

		bindPresenter(MainPagePresenter.class, MainPagePresenter.MyView.class,
				MainPageView.class, MainPagePresenter.MyProxy.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.main);

		bindPresenter(ResponsePresenter.class, ResponsePresenter.MyView.class,
				ResponseView.class, ResponsePresenter.MyProxy.class);

		bindPresenter(AuthorPagePresenter.class,
				AuthorPagePresenter.MyView.class, AuthorPageView.class,
				AuthorPagePresenter.MyProxy.class);

		bindPresenter(LoadDataToServletContextPresenter.class,
				LoadDataToServletContextPresenter.MyView.class,
				LoadDataToServletContextView.class,
				LoadDataToServletContextPresenter.MyProxy.class);
	}
}
