package com.intel.tvpresent.ui.content;

import android.media.MediaPlayer;

import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.MvpView;

import java.util.List;

public interface ContentMvpView extends MvpView {
    void init(List<UserWrapper> userWrappers, GameLevel gameLevel);
    void playNext(MediaPlayer.OnCompletionListener onCompletionListener);
//    void playNext();
    void setNotification(String text);
    void setNotice(String text);
    void setGameStatement(String text);
    void setBarcode(String url);
    long getDuration();
}
