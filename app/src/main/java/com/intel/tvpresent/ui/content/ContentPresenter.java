package com.intel.tvpresent.ui.content;

import android.media.MediaPlayer;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BasePresenter;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentPresenter extends BasePresenter<ContentMvpView> implements MediaPlayer.OnCompletionListener {

    private Subscription mSubscription;
    private final DataManager mDataManager;
    private Map<GameLevel, List<UserWrapper>> userCache = null;

    @Inject
    public ContentPresenter(DataManager dataManager) {
        mDataManager = dataManager;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (mSubscription != null) mSubscription.unsubscribe();
    }

    public void getUsers() {
        checkViewAttached();
        mDataManager.login().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Map<GameLevel, List<UserWrapper>>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Map<GameLevel, List<UserWrapper>> gameLevelListMap) {
                Map.Entry<GameLevel, List<UserWrapper>> entry= gameLevelListMap.entrySet().iterator().next();
                GameLevel gameLevel = entry.getKey();
                List<UserWrapper> userWrappers = entry.getValue();
                getMvpView().init(userWrappers, gameLevel);
                getMvpView().playNext(ContentPresenter.this);
            }
        });

        mSubscription = mDataManager.observableUsers()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Map<GameLevel, List<UserWrapper>>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Map<GameLevel, List<UserWrapper>> levelUsers) {
                        userCache = levelUsers;
                    }
                });
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        if (userCache != null) {
            Map.Entry<GameLevel, List<UserWrapper>> entry= userCache.entrySet().iterator().next();
            GameLevel gameLevel = entry.getKey();
            List<UserWrapper> userWrappers = entry.getValue();
            getMvpView().init(userWrappers, gameLevel);
            userCache = null;
        }
        getMvpView().playNext(this);
    }
}