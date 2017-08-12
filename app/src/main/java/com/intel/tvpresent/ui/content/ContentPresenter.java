package com.intel.tvpresent.ui.content;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.model.User;
import com.intel.tvpresent.ui.base.BasePresenter;

import java.util.List;

import javax.inject.Inject;

import rx.SingleSubscriber;
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

    public void getUsers(List<User> users) {
        checkViewAttached();

        mSubscription = mDataManager.getUsers(users)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new SingleSubscriber<List<User>>() {
                    @Override
                    public void onSuccess(List<User> value) {
                        getMvpView().initListWithUsers(value);
                        getMvpView().playVideo(value.get(0).videoRecord.url);
                    }

                    @Override
                    public void onError(Throwable error) {

                    }
                });

    }

}