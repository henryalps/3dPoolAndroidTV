package com.intel.tvpresent.data;

import com.intel.tvpresent.data.local.PreferencesHelper;
import com.intel.tvpresent.data.model.Room;
import com.intel.tvpresent.data.remote.AndroidTvService;
import com.intel.tvpresent.data.remote.JMSHelper;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import rx.Observable;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    AndroidTvService mMockAndroidTvService;
    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock
    Observable<JMSHelper> mMockJmsHelperObservable;
    @Mock
    Room mMockRoom;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockPreferencesHelper, mMockAndroidTvService, mMockJmsHelperObservable, mMockRoom);
    }

}