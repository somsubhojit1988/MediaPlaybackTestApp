package com.example.subhojitsom.playbacktestapp;


import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.IOException;

public class PlaybackActivity extends AppCompatActivity implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener, MediaPlayer.OnErrorListener {

    private SurfaceView mSurfaceView;
    private SurfaceHolder mSurfaceHolder;
    private MediaPlayer mMediaPlayer;

    private EditText mUrlEnter;
    private ImageButton mPlayUrl;

    private String TAG = "MediaPlaybackActivity";
    private String mContentUrl;

    //"http://www.connectedplanet.tv/olvs/OLVSDB10293848576/0700%20BluePort/test1.mp4";
    //"http://clips.vorwaerts-gmbh.de/VfE_html5.mp4";

    private void setUpMediaPlayer(){
        Log.d(TAG,"setUpMediaPlayer");
        if(mMediaPlayer == null){
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(this);
            mMediaPlayer.setOnErrorListener(this);
            mMediaPlayer.setOnPreparedListener(this);
            mMediaPlayer.setDisplay(mSurfaceHolder);
        }
    }

    private void tearDownPlayback(){
        Log.d(TAG,"tearDownPlayback");
        if(mMediaPlayer!=null){
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
    }
    /*
    * Activity life cycle
    * */
    @Override
    protected  void onStart(){
        Log.d(TAG,"Activity::onStart()");
        super.onStart();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG,"Activity::onCreate()");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playback);

        mUrlEnter = (EditText) findViewById(R.id.url_entry);
        mPlayUrl = (ImageButton) findViewById(R.id.play_url_button);
        mSurfaceView = (SurfaceView) findViewById(R.id.playback_surface);
        mUrlEnter.setEnabled(false);
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                Log.d(TAG,"Surface-created callback");
                mUrlEnter.setEnabled(true);
                mSurfaceHolder = surfaceHolder;
                setUpMediaPlayer();
            }
            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
                Log.d(TAG,"surfaceChanged");
            }
            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                Log.d(TAG,"surfaceDestroyed");
            }
        });

        mPlayUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mContentUrl = mUrlEnter.getText().toString();
                if(mContentUrl!=null || !mContentUrl.isEmpty())
                try {
                    if(mMediaPlayer ==null){
                        Log.d(TAG,"MediaPlayer.Instance null.Will call setUpMediaPlayer");
                        setUpMediaPlayer();
                    }
                    Log.d(TAG,"Will initiate playback");
                    mMediaPlayer.setDataSource(mContentUrl);
                    mMediaPlayer.prepareAsync();
                } catch (IOException e) {
                    Log.e(TAG,"Failed to create mediaplayer. IoException");
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onPause(){
        Log.d(TAG,"Activity::onPause");
        super.onPause();
    }
    @Override
    public void onStop(){
        Log.d(TAG,"Activity::onStop");
        tearDownPlayback();
        super.onStop();
    }
    @Override
    public void onDestroy(){
        Log.d(TAG,"Activity::onDestroy");
        super.onDestroy();

    }
    /*
    * Mediaplayer Callbacks
    * */
    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        Log.d(TAG,"onPrepared");
        mediaPlayer.start();
    }
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        Log.d(TAG,"Mediaplayer >> onCompletion");
        Toast.makeText(this,"Playback completed",Toast.LENGTH_LONG).show();
        tearDownPlayback();
    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        Log.d(TAG,"Mediaplayer >> onError => "+ i +" : "+i1);
        Toast.makeText(this,"Mediaplayer throwing error ( " + i +" : "+i1+" )",Toast.LENGTH_LONG).show();
        tearDownPlayback();
        return false;
    }
}
