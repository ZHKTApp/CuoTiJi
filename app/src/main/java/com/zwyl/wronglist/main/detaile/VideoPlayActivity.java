package com.zwyl.wronglist.main.detaile;

import android.media.MediaPlayer;
import android.view.View;
import android.widget.MediaController;
import android.widget.VideoView;

import com.zwyl.wronglist.R;
import com.zwyl.wronglist.base.BaseActivity;


public class VideoPlayActivity extends BaseActivity {

    private VideoView video_analysis;
    private String videoPath;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_videoplay;
    }

    @Override
    protected void initView() {
        super.initView();
        setHeadView();
        videoPath = getIntent().getStringExtra("videoPath");
        video_analysis = (VideoView) findViewById(R.id.video_analysis);
        video_analysis.setMediaController(new MediaController(this));
        video_analysis.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setVideoScalingMode(MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT);
            }
        });

        if (videoPath != null) {
            video_analysis.setVideoPath(videoPath);
            video_analysis.start();
        } else
            video_analysis.setVisibility(View.GONE);
    }

    private void setHeadView() {
        setTitleCenter("讲解视频");
        setShowLeftHead(true);//左边顶部按钮
        setShowRightHead(false);//右边顶部按钮
        setShowFilter(false);//日历筛选
        setShowLogo(false);//logo筛选
        setShowRefresh(false);//刷新
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!video_analysis.isPlaying()) {
            video_analysis.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (video_analysis.canPause()) {
            video_analysis.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopPlaybackVideo();
    }

    private void stopPlaybackVideo() {
        try {
            video_analysis.stopPlayback();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
