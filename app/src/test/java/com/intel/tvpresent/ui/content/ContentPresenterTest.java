package com.intel.tvpresent.ui.content;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.util.RxSchedulersOverrideRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class ContentPresenterTest {

    @Mock
    ContentMvpView mMockContentMvpView;
    @Mock DataManager mMockDataManager;
    private ContentPresenter mContentPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mContentPresenter = new ContentPresenter(mMockDataManager);
        mContentPresenter.attachView(mMockContentMvpView);
    }

    @After
    public void detachView() {
        mContentPresenter.detachView();
    }

}