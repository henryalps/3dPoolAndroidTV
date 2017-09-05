package com.intel.tvpresent.ui.content;

import android.content.Context;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentPresenter extends BasePresenter<ContentMvpView> {

    private Subscription mSubscription;
    private final DataManager mDataManager;

    @Inject
    public ContentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void getUsers(Context context) {
        checkViewAttached();

        mSubscription = mDataManager.observableUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<List<UserWrapper>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<UserWrapper> userWrappers) {
                        getMvpView().initListWithUsers(userWrappers);
                        getMvpView().playVideo(userWrappers.get(0).getPlayRecordWrapper().getVideoUrl());
                    }
                });

    }

}