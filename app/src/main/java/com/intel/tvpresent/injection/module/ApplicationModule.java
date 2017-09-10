package com.intel.tvpresent.injection.module;

import android.app.Application;
import android.content.Context;

import com.intel.tvpresent.data.remote.AndroidTvService;
import com.intel.tvpresent.data.remote.JMSHelper;
import com.intel.tvpresent.injection.ApplicationContext;

import javax.inject.Singleton;

import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Provide application-level dependencies. Mainly singleton object that can be injected from
 * anywhere in the app.
 */
@Module
public class ApplicationModule {
    protected final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }

    @Provides
    @Singleton
    Application provideApplication() {
        return mApplication;
    }

    @Provides
    CompositeSubscription provideCompositeSubscription() {
        return new CompositeSubscription();
    }

    @Provides
    @Singleton
    AndroidTvService provideVineyardService() {
        return AndroidTvService.Creator.newVineyardService();
    }

    @Singleton
    @Provides
    Observable<JMSHelper> provideJMSHelperObservable(final Lazy<JMSHelper> jmsHelperOpt) {
        return Observable.create(new Observable.OnSubscribe<JMSHelper>() {
            @Override
            public void call(Subscriber<? super JMSHelper> subscriber) {
                subscriber.onNext(jmsHelperOpt.get());
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }

}