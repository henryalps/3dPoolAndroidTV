package com.intel.tvpresent.ui.content;

import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.MvpView;

import java.util.List;

public interface ContentMvpView extends MvpView {
    void initListWithUsers(List<UserWrapper> userWrappers);
    void setSelectedItemIndex(int index);
    void playVideo(String url);
}
