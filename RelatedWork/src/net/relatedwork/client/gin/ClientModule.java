package net.relatedwork.client.gin;

import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.gin.DefaultModule;
import net.relatedwork.client.place.ClientPlaceManager;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.MainView;
import net.relatedwork.client.place.DefaultPlace;
import net.relatedwork.client.place.NameTokens;
import net.relatedwork.client.staticpresenter.ImprintPresenter;
import net.relatedwork.client.staticpresenter.ImprintView;
import net.relatedwork.client.FooterPresenter;
import net.relatedwork.client.FooterView;
import net.relatedwork.client.layout.BreadcrumbsPresenter;
import net.relatedwork.client.layout.BreadcrumbsView;
import net.relatedwork.client.HomePresenter;
import net.relatedwork.client.HomeView;
import net.relatedwork.client.Discussions.CommentPresenter;
import net.relatedwork.client.Discussions.CommentView;
import net.relatedwork.client.staticpresenter.AboutPresenter;
import net.relatedwork.client.staticpresenter.AboutView;
import net.relatedwork.client.staticpresenter.NotYetImplementedPresenter;
import net.relatedwork.client.staticpresenter.NotYetImplementedView;

public class ClientModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		install(new DefaultModule(ClientPlaceManager.class));

		bindPresenter(MainPresenter.class, MainPresenter.MyView.class,
				MainView.class, MainPresenter.MyProxy.class);

		bindConstant().annotatedWith(DefaultPlace.class).to(NameTokens.main);

		bindPresenter(FooterPresenter.class, FooterPresenter.MyView.class,
				FooterView.class, FooterPresenter.MyProxy.class);

		bindPresenter(ImprintPresenter.class, ImprintPresenter.MyView.class,
				ImprintView.class, ImprintPresenter.MyProxy.class);

		bindPresenterWidget(BreadcrumbsPresenter.class,
				BreadcrumbsPresenter.MyView.class, BreadcrumbsView.class);

		bindPresenterWidget(HomePresenter.class, HomePresenter.MyView.class,
				HomeView.class);

		bindPresenterWidget(CommentPresenter.class,
				CommentPresenter.MyView.class, CommentView.class);

		bindPresenter(AboutPresenter.class, AboutPresenter.MyView.class,
				AboutView.class, AboutPresenter.MyProxy.class);

		bindPresenterWidget(NotYetImplementedPresenter.class,
				NotYetImplementedPresenter.MyView.class,
				NotYetImplementedView.class);
	}
}
