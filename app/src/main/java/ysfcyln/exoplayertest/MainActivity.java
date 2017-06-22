package ysfcyln.exoplayertest;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
//import com.google.android.exoplayer2.trackselection.AdaptiveVideoTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;

public class MainActivity extends AppCompatActivity implements ExoPlayer.EventListener{

    // https://codelabs.developers.google.com/codelabs/exoplayer-intro/#0
    // https://github.com/arpit/ExoPlayerTest/blob/master/app/src/main/java/com/arpitonline/exoplayertest/MainActivity.java
    // https://github.com/yusufcakmak/ExoPlayerSample/blob/master/app/src/main/java/com/yusufcakmak/exoplayersample/RadioPlayerActivity.java

    private BandwidthMeter bandwidthMeter;
    private TrackSelector trackSelector;
    private TrackSelection.Factory trackSelectionFactory;
    private SimpleExoPlayer player;
    private DataSource.Factory dataSourceFactory;
    private ExtractorsFactory extractorsFactory;
    private MediaSource mediaSource;

    private String songUrl = "http://www.mfiles.co.uk/mp3-downloads/edvard-grieg-peer-gynt1-morning-mood-piano.mp3";
    private Button stopButton;
    private Button startButton;
    private Button videoActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoActivity = (Button) findViewById(R.id.videoActivity);
        videoActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,VideoActivity.class);
                startActivity(i);
            }
        });

        startButton = (Button) findViewById(R.id.btnStart);
        stopButton = (Button) findViewById(R.id.btnStop);

        bandwidthMeter = new DefaultBandwidthMeter();
        extractorsFactory = new DefaultExtractorsFactory();

        trackSelectionFactory = new AdaptiveTrackSelection.Factory(bandwidthMeter);

        trackSelector = new DefaultTrackSelector(trackSelectionFactory);


/*        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"),
                (TransferListener<? super DataSource>) bandwidthMeter);*/

        DefaultBandwidthMeter defaultBandwidthMeter = new DefaultBandwidthMeter();
        dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "mediaPlayerSample"),defaultBandwidthMeter);


        mediaSource = new ExtractorMediaSource(Uri.parse(songUrl),
                dataSourceFactory,
                extractorsFactory,
                null,
                null);

        player = ExoPlayerFactory.newSimpleInstance(this,trackSelector);

        player.addListener(this);
        player.prepare(mediaSource);

        Log.v("TEST","playing state : " + player.getPlaybackState());

        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setPlayWhenReady(true);
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                player.setPlayWhenReady(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.setPlayWhenReady(false);
    }


    @Override
    public void onLoadingChanged(boolean isLoading) {
        Log.i("TEST", "onLoadingChanged: " + isLoading + "");
        Log.i("TEST", "Buffered Position: " + player.getBufferedPosition() + "");
        Log.i("TEST", "Buffered Percentage: " + player.getBufferedPercentage() + "");
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

        if(playbackState == ExoPlayer.STATE_READY){
            Log.i("TEST", "ExoPlayer State is: READY");
            //player.setPlayWhenReady(true);
        } else if (playbackState == ExoPlayer.STATE_BUFFERING){
            Log.i("TEST", "ExoPlayer State is: BUFFERING");
        } else if (playbackState == ExoPlayer.STATE_ENDED){
            Log.i("TEST", "ExoPlayer State is: ENDEND");
        } else if (playbackState == ExoPlayer.STATE_IDLE){
            Log.i("TEST", "ExoPlayer State is: IDLE");
        }


    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }
}
