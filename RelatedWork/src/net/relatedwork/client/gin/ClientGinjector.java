package net.relatedwork.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import net.relatedwork.client.gin.ClientModule;
import net.relatedwork.client.staticpresenter.ImprintPresenter;

import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.google.gwt.inject.client.AsyncProvider;
import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.staticpresenter.AboutPresenter;
import net.relatedwork.client.staticpresenter.DataPresenter;
import net.relatedwork.client.staticpresenter.LicensePresenter;
import net.relatedwork.client.tools.SearchPresenter;
import net.relatedwork.client.content.AuthorPresenter;
import net.relatedwork.client.layout.FooterPresenter;
import net.relatedwork.client.layout.HeaderPresenter;
import net.relatedwork.client.content.SearchResultPagePresenter;

@GinModules({ DispatchAsyncModule.class, ClientModule.class })
public interface ClientGinjector extends Ginjector {

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	AsyncProvider<MainPresenter> getMainPresenter();

	AsyncProvider<FooterPresenter> getFooterPresenter();

	AsyncProvider<ImprintPresenter> getImprintPresenter();

	AsyncProvider<AboutPresenter> getAboutPresenter();

	AsyncProvider<DataPresenter> getDataPresenter();

	AsyncProvider<LicensePresenter> getLicensePresenter();

	AsyncProvider<AuthorPresenter> getAuthorPresenter();

	AsyncProvider<HeaderPresenter> getHeaderPresenter();

	AsyncProvider<SearchPresenter> getSearchPresenter();

	AsyncProvider<SearchResultPagePresenter> getSearchResultPagePresenter();
}
