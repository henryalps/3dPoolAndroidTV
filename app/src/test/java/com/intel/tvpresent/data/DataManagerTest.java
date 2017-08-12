package com.intel.tvpresent.data;

import com.intel.tvpresent.data.local.PreferencesHelper;
import com.intel.tvpresent.data.remote.AndroidTvService;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock
    AndroidTvService mMockAndroidTvService;
    @Mock PreferencesHelper mMockPreferencesHelper;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockPreferencesHelper, mMockAndroidTvService);
    }

}