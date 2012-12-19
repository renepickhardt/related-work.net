package net.relatedwork.client.gin;


import net.relatedwork.client.MainPresenter;
import net.relatedwork.client.content.AuthorPresenter;
import net.relatedwork.client.content.SearchResultPagePresenter;
import net.relatedwork.client.discussion.CommentBoxPresenter;
import net.relatedwork.client.discussion.CommentsPanelPresenter;
import net.relatedwork.client.layout.FooterPresenter;
import net.relatedwork.client.layout.HeaderPresenter;
import net.relatedwork.client.staticpresenter.AboutPresenter;
import net.relatedwork.client.staticpresenter.DataPresenter;
import net.relatedwork.client.staticpresenter.ImprintPresenter;
import net.relatedwork.client.staticpresenter.LicensePresenter;
import net.relatedwork.client.tools.ListEntryPresenter;
import net.relatedwork.client.tools.SearchPresenter;
import net.relatedwork.client.tools.login.LoginControlsPresenter;
import net.relatedwork.client.content.PaperPresenter;

import com.google.web.bindery.event.shared.EventBus;
//import com.google.web.bindery.event.shared.EventBus;

import com.google.gwt.inject.client.AsyncProvider;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import net.relatedwork.client.staticpresenter.UserVerifyPresenter;
import net.relatedwork.client.sidebar.SidebarPresenter;

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

	AsyncProvider<LoginControlsPresenter> getLoginControlsPresenter();

	AsyncProvider<PaperPresenter> getPaperPresenter();

	AsyncProvider<ListEntryPresenter> getListEntryPresenter();

    AsyncProvider<CommentBoxPresenter> getCommentBoxPresenter();

    AsyncProvider<CommentsPanelPresenter> getCommentsPanelPresenter();
    
    AsyncProvider<UserVerifyPresenter> getUserVerifyPresenter();

	AsyncProvider<SidebarPresenter> getSidebarPresenter();
}
