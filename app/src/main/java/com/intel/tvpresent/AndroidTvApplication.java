package com.intel.tvpresent;

import android.app.Application;
import android.content.Context;

import com.intel.tvpresent.injection.component.ApplicationComponent;
import com.intel.tvpresent.injection.component.DaggerApplicationComponent;
import com.intel.tvpresent.injection.module.ApplicationModule;

import timber.log.Timber;

public class AndroidTvApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
    }

    public static AndroidTvApplication get(Context context) {
        return (AndroidTvApplication) context.getApplicationContext();
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

}
