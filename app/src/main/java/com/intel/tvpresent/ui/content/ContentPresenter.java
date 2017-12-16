package com.intel.tvpresent.ui.content;

import android.os.Handler;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.Room;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BasePresenter;

import org.videolan.vlc.listener.MediaListenerEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentPresenter extends BasePresenter<ContentMvpView> implements MediaListenerEvent {

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
        mDataManager.login().observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Subscriber<Room>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Room room) {
                ArrayList<String> array = (ArrayList<String>) Arrays.asList("1", "2");
                int curValue = new Random().nextInt(2);
                for (Map.Entry<GameLevel, List<UserWrapper>> entry : room.getUserWrapperList().entrySet()) {
                    if (entry.getKey().getId().equals(array.get(curValue))) {
                        GameLevel gameLevel = entry.getKey();
                        List<UserWrapper> userWrappers = entry.getValue();
                        getMvpView().init(userWrappers, gameLevel);
                        getMvpView().playNext(ContentPresenter.this);
//                        getMvpView().playNext();
                        getMvpView().setNotice(room.getNotice());
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
    public void eventBuffing(float buffing, boolean show) {

    }

    @Override
    public void eventPlayInit(boolean openClose) {
        System.out.println();
    }

    @Override
    public void eventStop(boolean isPlayError) {
        if (userCache != null && userCache.size() > 0) {
            Map.Entry<GameLevel, List<UserWrapper>> entry= userCache.entrySet().iterator().next();
            GameLevel gameLevel = entry.getKey();
            List<UserWrapper> userWrappers = entry.getValue();
            getMvpView().init(userWrappers, gameLevel);
            userCache = null;
        }
        getMvpView().playNext(this);
    }

    @Override
    public void eventError(int error, boolean show) {

    }

    @Override
    public void eventPlay(boolean isPlaying) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                eventStop(false);
            }
        }, getMvpView().getDuration());
    }
}