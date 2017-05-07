package com.sarabjeet.musical.sync;


import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PLAY;

/**
 * Created by sarabjeet on 8/5/17.
 */

public class MusicPlayerService extends Service implements MediaPlayer.OnPreparedListener {

    public final String LOG_TAG = "Music.Service";
    MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        }
        if (intent.getAction().equals(ACTION_PLAY)) {
            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(intent.getStringExtra("path"));
                mediaPlayer.setOnPreparedListener(this);
                mediaPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
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
        mp.start();
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null)
            mediaPlayer.release();
    }

}
