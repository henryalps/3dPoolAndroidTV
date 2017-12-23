package com.intel.tvpresent.ui.content;

import com.intel.tvpresent.data.DataManager;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.Room;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BasePresenter;

import org.videolan.vlc.listener.MediaListenerEvent;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ContentPresenter extends BasePresenter<ContentMvpView> implements MediaListenerEvent {

    private Subscription mSubscription;
    private final DataManager mDataManager;
    private Map<GameLevel, List<UserWrapper>> userCache = null;
    private boolean handlerMarker = false;

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
                for (Map.Entry<GameLevel, List<UserWrapper>> entry : room.getUserWrapperList().entrySet()) {
                    if (entry.getKey().getId().equals("1")) {
                        GameLevel gameLevel = entry.getKey();
                        if (entry.getValue().size() > 10) {
                            for (int i = 10; i < entry.getValue().size(); i++) {
                                entry.getValue().remove(i);
                            }
                        }
                        List<UserWrapper> userWrappers = entry.getValue();
                        getMvpView().init(userWrappers, gameLevel);
                        getMvpView().playNext(ContentPresenter.this);
//                        getMvpView().playNext();
                        getMvpView().setNotification(room.getNotification());
                        getMvpView().setNotice(room.getNotice());
                        getMvpView().setGameStatement(room.getGameStatement());
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
        System.out.println();
    }

    @Override
    public void eventPlayInit(boolean openClose) {
        System.out.println();
        handlerMarker=false;
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
        if (handlerMarker) {
            handlerMarker=false;
            eventStop(false);
        } else {
            handlerMarker = true;
        }
    }


}