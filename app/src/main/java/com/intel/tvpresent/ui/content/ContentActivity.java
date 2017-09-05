package com.intel.tvpresent.ui.content;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.VideoView;

import com.intel.tvpresent.R;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

public class ContentActivity extends BaseActivity implements ContentMvpView {

    @Bind(R.id.user_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.user_record_video)
    VideoView mVideoView;

    @Inject
    ContentPresenter mContentPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this); // init general injection
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentPresenter.attachView(this);

        startFetchUser();
    }

    // subscribe user record list & update display
    private void startFetchUser() {
    }

    @Override
    public void initListWithUsers(List<UserWrapper> userWrappers) {
        mRecyclerView.setAdapter(new ContentRecycleViewAdapter(userWrappers));
    }

    @Override
    public void setSelectedItemIndex(int index) {
        
    }

    @Override
    public void playVideo(String url) {
        mVideoView.setVideoURI(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.local_video));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }
}