package com.intel.tvpresent.ui.content;

import android.content.res.Resources;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.VideoView;

import com.intel.tvpresent.R;
import com.intel.tvpresent.data.model.User;
import com.intel.tvpresent.data.model.VideoRecord;
import com.intel.tvpresent.ui.base.BaseActivity;

import java.util.ArrayList;
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

        getUsers();
    }

    private void getUsers() {
        // Usually we'd load things from an API or database, for example here we just create
        // a list of cats from resources and return them back after passing them to the datamanager.
        // Obviously we wouldn't usually do this, but this is just for example and allows us
        // to still have an example unit test that doesn't require robolectric!
        Resources resources = getResources();
        String[] names = resources.getStringArray(R.array.user_names);
        String[] images = resources.getStringArray(R.array.photos);

        List<User> users = new ArrayList<>();
        for (int i = 0; i < names.length; i++) {
            if (i % 2 == 1) {
                users.add(new User(names[i], i + 1, images[i], new VideoRecord("http://videocdn.bodybuilding.com/video/mp4/62000/62792m.mp4", "")));
            } else {
                users.add(new User(names[i], i + 1, images[i], new VideoRecord("rtsp://wowzaec2demo.streamlock.net/vod/mp4:BigBuckBunny_115k.mov", "")));
            }
        }

        mContentPresenter.getUsers(users);
    }

    @Override
    public void initListWithUsers(List<User> users) {
        mRecyclerView.setAdapter(new ContentRecycleViewAdapter(users));
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