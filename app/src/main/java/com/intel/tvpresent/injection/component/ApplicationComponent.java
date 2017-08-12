package com.intel.tvpresent.injection.component;

import android.app.Application;
import android.content.Context;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.local.PreferencesHelper;
import com.intel.tvpresent.injection.ApplicationContext;
import com.intel.tvpresent.injection.module.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;
import rx.subscriptions.CompositeSubscription;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    @ApplicationContext
    Context context();
    Application application();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    CompositeSubscription compositeSubscription();

}