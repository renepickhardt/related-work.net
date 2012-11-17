package net.relatedwork.gwtp.client.gin;

import com.google.gwt.inject.client.GinModules;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import net.relatedwork.gwtp.client.gin.ClientModule;
import net.relatedwork.gwtp.client.staticpresenter.ImprintPresenter;

import com.google.gwt.inject.client.Ginjector;
import com.google.gwt.event.shared.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.google.gwt.inject.client.AsyncProvider;

import net.relatedwork.gwtp.client.MainPresenter;

@GinModules({ DispatchAsyncModule.class, ClientModule.class })
public interface ClientGinjector extends Ginjector {

	EventBus getEventBus();

	PlaceManager getPlaceManager();

	AsyncProvider<MainPresenter> getMainPresenter();

	AsyncProvider<ImprintPresenter> getImprintPresenter();
}
