package com.intel.tvpresent.injection.component;

import com.intel.tvpresent.injection.PerActivity;
import com.intel.tvpresent.injection.module.ActivityModule;
import com.intel.tvpresent.ui.content.ContentActivity;
import dagger.Component;

/**
 * This component inject dependencies to all Activities across the application
 */
@PerActivity
@Component(dependencies = ApplicationComponent.class, modules = ActivityModule.class)
public interface ActivityComponent {

    void inject(ContentActivity contentActivity);

}