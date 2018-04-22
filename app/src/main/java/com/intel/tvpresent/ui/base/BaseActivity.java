package com.intel.tvpresent.ui.base;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;

import com.intel.tvpresent.AndroidTvApplicationLike;
import com.intel.tvpresent.injection.component.ActivityComponent;
import com.intel.tvpresent.injection.component.DaggerActivityComponent;
import com.intel.tvpresent.injection.module.ActivityModule;
import com.tencent.bugly.beta.tinker.TinkerManager;

public class BaseActivity extends Activity {

    private ActivityComponent mActivityComponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public ActivityComponent activityComponent() {
        if (mActivityComponent == null) {
            mActivityComponent = DaggerActivityComponent.builder()
                    .activityModule(new ActivityModule(this))
                    .applicationComponent(((AndroidTvApplicationLike)TinkerManager.getTinkerApplicationLike()).getComponent())
                    .build();
        }
        return mActivityComponent;
    }

}