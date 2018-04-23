package com.intel.tvpresent.ui.content;

import android.media.MediaPlayer;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.Room;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BasePresenter;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentPresenter extends BasePresenter<ContentMvpView> implements MediaPlayer.OnCompletionListener{

    private Subscription mSubscription;
    private final DataManager mDataManager;
    private Map<GameLevel, List<UserWrapper>> userCache = null;
    private Room mRoom = null;

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
        mDataManager.login().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Room>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Room room) {
                mRoom = room;
                getMvpView().init(new ArrayList<UserWrapper>(), new GameLevel(), room.getActivityWrapper());
                getMvpView().setNotification(room.getNotification());
                getMvpView().setNotice(room.getNotice());
                getMvpView().setGameStatement(room.getGameStatement());
                getMvpView().setBarcode(room.getQrcodeUrl());
                for (Map.Entry<GameLevel, List<UserWrapper>> entry : room.getUserWrapperList().entrySet()) {
                    if (entry.getKey().getId().equals("1")) {
                        GameLevel gameLevel = entry.getKey();
                        if (entry.getValue().size() > 10) {
                            for (int i = 10; i < entry.getValue().size(); i++) {
                                entry.getValue().remove(i);
                            }
                        }
                        List<UserWrapper> userWrappers = entry.getValue();
                        getMvpView().init(userWrappers, gameLevel, room.getActivityWrapper());
                        getMvpView().playNext(ContentPresenter.this);
                    }
                }
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
        Beta.checkUpgrade(false,false);
        if (userCache != null && userCache.size() > 0) {
            Map.Entry<GameLevel, List<UserWrapper>> entry= userCache.entrySet().iterator().next();
            GameLevel gameLevel = entry.getKey();
            List<UserWrapper> userWrappers = entry.getValue();
            getMvpView().init(userWrappers, gameLevel, mRoom.getActivityWrapper());
            userCache = null;
        }
        getMvpView().playNext(this);
    }

}