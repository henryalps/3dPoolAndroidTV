package com.intel.tvpresent.ui.content;

import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.MvpView;

import org.videolan.vlc.listener.MediaListenerEvent;

import java.util.List;

public interface ContentMvpView extends MvpView {
    void init(List<UserWrapper> userWrappers, GameLevel gameLevel);
    void playNext(MediaListenerEvent mediaListenerEvent);
//    void playNext();
    void setNotice(String text);
    long getDuration();
}
