package com.intel.tvpresent.data;

import com.intel.tvpresent.data.local.PreferencesHelper;
import com.intel.tvpresent.data.model.User;
import com.intel.tvpresent.data.remote.AndroidTvService;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Single;

@Singleton
public class DataManager {

    private final AndroidTvService mAndroidTvService;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper,
                       AndroidTvService androidTvService) {
        mPreferencesHelper = preferencesHelper;
        mAndroidTvService = androidTvService;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Single<List<User>> getUsers(List<User> users) {
        return Single.just(users);
    }

}
