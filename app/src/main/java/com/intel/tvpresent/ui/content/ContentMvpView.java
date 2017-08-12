package com.intel.tvpresent.ui.content;

import com.intel.tvpresent.data.model.User;
import com.intel.tvpresent.ui.base.MvpView;

import java.util.List;

public interface ContentMvpView extends MvpView {
    void initListWithUsers(List<User> users);
    void setSelectedItemIndex(int index);
    void playVideo(String url);
}
