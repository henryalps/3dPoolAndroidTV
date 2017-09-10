package com.intel.tvpresent.ui.content;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;
import android.widget.VideoView;

import com.intel.tvpresent.R;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.PlayRecordWrapper;
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

    @Bind(R.id.score)
    TextView mScore;

    @Bind(R.id.lucky_ball)
    TextView mLuckyBall;

    @Bind(R.id.star)
    TextView mStar;

    @Bind(R.id.multipot)
    TextView mMultiPot;

    @Bind(R.id.max_combo)
    TextView mMaxCombo;

    @Inject
    ContentPresenter mContentPresenter;

    private ContentRecycleViewAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this); // init general injection
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mContentPresenter.attachView(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startFetchUser();
    }

    // subscribe user record list & update display
    private void startFetchUser() {
        mContentPresenter.getUsers();
    }

    @Override
    public void init(List<UserWrapper> userWrappers, GameLevel gameLevel) {
        if (null == mAdapter) {
            mAdapter = new ContentRecycleViewAdapter(gameLevel, userWrappers);
        } else {
            int currentIndex = mAdapter.getmSelectdPos();
            mAdapter = new ContentRecycleViewAdapter(gameLevel, userWrappers);
            mAdapter.setmSelectdPos(currentIndex);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void playNext(MediaPlayer.OnCompletionListener onCompletionListener) {
        mVideoView.setOnCompletionListener(onCompletionListener);
        if (mVideoView.getDuration() > 0) { // initialized
            if (mAdapter.getmSelectdPos() >= mAdapter.getItemCount() - 1) {
                mAdapter.setmSelectdPos(0);
            } else {
                mAdapter.setmSelectdPos(mAdapter.getmSelectdPos() + 1);
            }
        }
        PlayRecordWrapper wrapper = mAdapter.getSelectedUserWrapper().getPlayRecordWrapper();
        mVideoView.setVideoURI(Uri.parse(wrapper.getVideoUrl())); // "android.resource://" + getPackageName() + "/" + R.raw.local_video
        setRecordParams(wrapper);
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.start();
            }
        });
    }

    public void setRecordParams(PlayRecordWrapper wrapper) {
        mScore.setText(String.format("分数:%s", wrapper.getScore()));
        mLuckyBall.setText(String.format("幸运球:%s", wrapper.getLuckyBall()));
        mMultiPot.setText(String.format("MULTIPOT:%s", wrapper.getMaxMultiPot()));
        mStar.setText(String.format("星级:%s", wrapper.getStarBag()));
        mMaxCombo.setText(String.format("最大连击:%s", wrapper.getMaxCombo()));
    }
}