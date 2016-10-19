package com.apress.mediaplayer;

import android.app.Activity;
import android.os.Bundle;
import android.widget.VideoView;

/**
 * Created by Paul on 10/10/15.
 */
public class PlayerActivity extends Activity implements PlayerControlsFragment.PlayerControlsListener {
    private VideoView mVideoView;
    private Video mVideo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        initViews();
        initVideoPlayer();
    }

    private void initViews() {
        mVideoView = (VideoView) findViewById( R.id.video_view );
    }

    private void initVideoPlayer() {
        mVideo = (Video) getIntent().getSerializableExtra( VideoDetailsFragment.EXTRA_VIDEO );
        mVideoView.setVideoPath( mVideo.getVideoUrl() );
    }

    @Override
    public void play() {
        mVideoView.start();
    }

    @Override
    public void pause() {
        mVideoView.pause();
    }
}
