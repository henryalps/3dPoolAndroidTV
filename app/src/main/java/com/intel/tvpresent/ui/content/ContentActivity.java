package com.intel.tvpresent.ui.content;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.daimajia.numberprogressbar.NumberProgressBar;
import com.danikula.videocache.HttpProxyCacheServer;
import com.hanks.htextview.line.LineTextView;
import com.intel.tvpresent.R;
import com.intel.tvpresent.data.model.ActivityWrapper;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.PlayRecordWrapper;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.ui.base.BaseActivity;
import com.intel.tvpresent.ui.custom.FocusedTrue4TV;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;

import static com.intel.tvpresent.AndroidTvApplicationLike.getProxy;

//import org.videolan.vlc.listener.MediaListenerEvent;

public class ContentActivity extends BaseActivity implements ContentMvpView {

    @Bind(R.id.user_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.user_record_video)
    VideoView mVideoView; //VlcVideoView

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

    @Bind(R.id.notice)
    FocusedTrue4TV mNotice;

    @Bind(R.id.number_progress_bar)
    NumberProgressBar numberProgressBar;

    @Bind(R.id.real_header)
    View headerView;

    @Bind(R.id.rules)
    TextView mRules;

    @Bind(R.id.broadcast)
    TextView mBroadcast;

    @Inject
    ContentPresenter mContentPresenter;

    @Bind(R.id.barcode)
    ImageView mBarcode;

    @Bind(R.id.time_range)
    TextView mTimeRange;

    @Bind(R.id.placeholder)
    View mPlaceHolder;

    private ContentRecycleViewAdapter mAdapter;

//    private CarouselLayoutManager layoutManager;

    private LinearLayoutManager layoutManager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this); // init general injection
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

//        FileDownloader.setup(this);

//        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL);
//        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager = new LinearLayoutManager(this);

        mRecyclerView.setLayoutManager(layoutManager); // new LinearLayoutManager(this)
        mContentPresenter.attachView(this);
        mBarcode.setVisibility(View.VISIBLE);
        mBarcode.bringToFront();
        mBarcode.setImageResource(R.drawable.barcode_sz);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startFetchUser();
    }

    // subscribe user record list & updateInPush display
    private void startFetchUser() {
        mContentPresenter.getUsers();
    }

    @Override
    public void init(List<UserWrapper> userWrappers, GameLevel gameLevel, ActivityWrapper activityWrapper) {
        ((LineTextView)(headerView.findViewById(R.id.title))).setText(!activityWrapper.getName().isEmpty()? activityWrapper.getName() : gameLevel.getName());
        mTimeRange.setText(activityWrapper.getTimeRange());
        if (null == mAdapter || 0 == mAdapter.getItemCount()) {
            mAdapter = new ContentRecycleViewAdapter(gameLevel, userWrappers, activityWrapper);
        } else {
            int currentIndex = mAdapter.getmSelectdPos();
            mAdapter = new ContentRecycleViewAdapter(gameLevel, userWrappers, activityWrapper);
            mAdapter.setmSelectdPos(currentIndex);
        }
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void playNext(final MediaPlayer.OnCompletionListener onCompletionListener) {
        mPlaceHolder.setVisibility(View.GONE);
        mVideoView.setMinimumHeight(mVideoView.getWidth() * 9 / 16);
        if (mVideoView.getVisibility() == View.VISIBLE) { // initialized
            mVideoView.pause();
            if (mAdapter.getmSelectdPos() >= mAdapter.getItemCount() - 2) {
                mAdapter.setmSelectdPos(1);
            } else {
                mAdapter.setmSelectdPos(mAdapter.getmSelectdPos() + 1);
//                layoutManager.scrollToPositionWithOffset(mAdapter.getmSelectdPos(), 0);
            }
        } else {
            mVideoView.setVisibility(View.VISIBLE);
        }
        mVideoView.setOnCompletionListener(onCompletionListener);

        PlayRecordWrapper wrapper = mAdapter.getSelectedUserWrapper().getPlayRecordWrapper();

        setRecordParams(wrapper);

        String url = wrapper.getVideoUrl();

        if (url.endsWith(".avi") || url.endsWith(".mp4")) {
            try {
                HttpProxyCacheServer proxy = getProxy();
                String proxyUrl = proxy.getProxyUrl(url);
                mVideoView.setVideoPath(proxyUrl);
                mVideoView.start();
            } catch (Exception ex) {
                ex.printStackTrace();
                onCompletionListener.onCompletion(null);
            }
//            String[] fileNames = url.split("//");
//            String fileName = fileNames[fileNames.length - 1];
//            FileDownloader.getImpl().create(url)
//                    .setPath(this.getExternalCacheDir().getAbsolutePath() + File.separator + fileName)
//                    .setListener(new FileDownloadListener() {
//                        @Override
//                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                            numberProgressBar.setVisibility(View.VISIBLE);
//                            numberProgressBar.setMax(totalBytes);
//                        }
//
//                        @Override
//                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                            System.out.println(soFarBytes + " " + totalBytes);
//                            numberProgressBar.setProgress(soFarBytes);
//                        }
//
//                        @Override
//                        protected void completed(BaseDownloadTask task) {
//                            numberProgressBar.setVisibility(View.GONE);
//                            mVideoView.setVideoPath(task.getPath());
//                            mVideoView.start();
////                        mVideoView.setVideoURI(Uri.parse(task.getPath())); // "android.resource://" + getPackageName() + "/" + R.raw.local_video
////
////                        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
////                            @Override
////                            public void onPrepared(MediaPlayer mp) {
////                                mp.start();
////                            }
////                        });
//                            // org.videolan.vlc.VlcVideoView
//                        }
//
//                        @Override
//                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//                            System.out.println();
//
//                        }
//
//                        @Override
//                        protected void error(BaseDownloadTask task, Throwable e) {
//                            e.printStackTrace();
////                            mediaListenerEvent.eventStop(false);
//                            onCompletionListener.onCompletion(null);
//                        }
//
//                        @Override
//                        protected void warn(BaseDownloadTask task) {
//                            System.out.println();
//
//                        }
//                    })
//                    .start();
        } else {
            onCompletionListener.onCompletion(null);
        }


    }



    public void setRecordParams(PlayRecordWrapper wrapper) {
        mScore.setText(String.format("分数:%s", wrapper.getScore()));
        setText(mLuckyBall, "幸运球:%s", wrapper.getLuckyBall());
        setText(mMultiPot, "MULTIPOT:%s", wrapper.getMaxMultiPot());
        setText(mStar, "星级:%s", wrapper.getStarBag());
        setText(mMaxCombo, "最大连击:%s", wrapper.getMaxCombo());
    }

    private void setText(TextView textView, String formatter, String text) {
        if (!"0".equals(text) && !"null".equals(text) && null != text) {
            textView.setText(String.format(formatter, text));
            textView.setVisibility(View.VISIBLE);
        } else {
            textView.setVisibility(View.GONE);
        }
    }

    @Override
    public void setNotification(String text) {
        if (text.isEmpty()) {
            mNotice.setVisibility(View.GONE);
        } else {
            mNotice.setVisibility(View.VISIBLE);
            mNotice.setText(text);
            mNotice.setMarqueeEnable(true);
        }
    }

    @Override
    public void setNotice(String text) {
        mBroadcast.setText(text);
    }

    @Override
    public void setGameStatement(String text) {
        mRules.setText(text.isEmpty() ? getString(R.string.rules) : text);
    }

    @Override
    public void setBarcode(final String url) {
//        Glide.with(this).load(url).into(mBarcode);
        new Thread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Bitmap bmp = getURLimage(url);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = bmp;
                handle.sendMessage(msg);
            }
        }).start();
        mBarcode.setVisibility(View.VISIBLE);
        mBarcode.bringToFront();
    }

    @Override
    public long getDuration() {
        if (null != mVideoView) {
            return mVideoView.getDuration();
        }
        return 0L;
    }


    //在消息队列中实现对控件的更改
    private Handler handle = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Bitmap bmp=(Bitmap)msg.obj;
                    mBarcode.setImageBitmap(bmp);
                    break;
            }
        };
    };

    public Bitmap getURLimage(String url) {
        Bitmap bmp = null;
        try {
            URL myurl = new URL(url);
            // 获得连接
            HttpURLConnection conn = (HttpURLConnection) myurl.openConnection();
            conn.setConnectTimeout(6000);//设置超时
            conn.setDoInput(true);
            conn.setUseCaches(false);//不缓存
            conn.connect();
            InputStream is = conn.getInputStream();//获得图片的数据流
            bmp = BitmapFactory.decodeStream(is);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }
}