package com.sarabjeet.musical.sync;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.google.firebase.analytics.FirebaseAnalytics;

import java.io.IOException;

import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PAUSE;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PLAY;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_RESUME;
import static com.sarabjeet.musical.utils.Constants.PLAYER.PLAY;

/**
 * Created by sarabjeet on 8/5/17.
 */

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    public final String LOG_TAG = "Music.Service";
    public MediaPlayer mediaPlayer;
    LocalBroadcastManager broadcastManager;
    String path = null;
    String title = null;
    String artist = null;
    FirebaseAnalytics mFirebaseAnalytics;

    @Override
    public void onCreate() {
        super.onCreate();
        broadcastManager = LocalBroadcastManager.getInstance(this);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (intent.getAction().equals(ACTION_PLAY)) {
            path = intent.getStringExtra("path");
            title = intent.getStringExtra("title");
            artist = intent.getStringExtra("artist");
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, intent.getStringExtra("title"));
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            try {
                mediaPlayer.reset();
                mediaPlayer.setDataSource(path);
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else if (intent.getAction().equals(ACTION_PAUSE)) {
            mediaPlayer.pause();
            updatePlayer("pause");
        } else if (intent.getAction().equals(ACTION_RESUME)) {
            mediaPlayer.start();
            updatePlayer("start");
        }
        return Service.START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        updatePlayer("start");
        mp.start();
    }

    private void updatePlayer(String state) {
        Intent intent = new Intent(PLAY);
        intent.putExtra("Player", state);
        intent.putExtra("Path", path);
        intent.putExtra("Artist", artist);
        intent.putExtra("Title", title);
        broadcastManager.sendBroadcast(intent);
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

}
