package com.sarabjeet.musical.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.RemoteViews;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.sync.MusicPlayerService;

import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_NEXT;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PAUSE;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_RESUME;
import static com.sarabjeet.musical.utils.Constants.PLAYER.PLAY;

/**
 * Created by sarabjeet on 10/5/17.
 */

public class MusicWidgetProvider extends AppWidgetProvider {
    BroadcastReceiver receiver = null;
    Bitmap bitmap;
    RemoteViews views;
    Boolean playerState = false;

    @Override
    public void onUpdate(final Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {
            views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);


            Intent playIntent = new Intent(context, MusicPlayerService.class);
            playIntent.setAction(ACTION_RESUME);
            PendingIntent playPendingIntent = PendingIntent.getService(context, 0, playIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_play_button, playPendingIntent);


            Intent nextIntent = new Intent(context, MusicPlayerService.class);
            nextIntent.setAction(ACTION_NEXT);
            PendingIntent nextPendingIntent = PendingIntent.getService(context, 0, nextIntent, 0);
            views.setOnClickPendingIntent(R.id.widget_next_button, nextPendingIntent);


            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String path = intent.getStringExtra(context.getString(R.string.media_path));
                    playerState = intent.getBooleanExtra(context.getString(R.string.mp_state), false);

                    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
                    mmr.setDataSource(path);
                    byte[] data = mmr.getEmbeddedPicture();
                    if (data != null) {
                        bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                        views.setImageViewBitmap(R.id.widget_album_art, bitmap);
                    } else {
                        views.setImageViewResource(R.id.widget_album_art, R.drawable.fallback_cover);
                    }
                    views.setTextViewText(R.id.widget_track_title,
                            intent.getStringExtra(context.getString(R.string.media_title)));
                    views.setTextViewText(R.id.widget_artist_text,
                            intent.getStringExtra(context.getString(R.string.media_artist)));
                    if (playerState) {
                        views.setImageViewResource(R.id.widget_play_button, R.drawable.pause);
                        Intent pauseIntent = new Intent(context, MusicPlayerService.class);
                        pauseIntent.setAction(ACTION_PAUSE);
                        PendingIntent pausePendingIntent = PendingIntent.getService(context, 0, pauseIntent, 0);
                        views.setOnClickPendingIntent(R.id.widget_play_button, pausePendingIntent);
                    } else {
                        views.setImageViewResource(R.id.widget_play_button, R.drawable.play);
                        Intent playIntent = new Intent(context, MusicPlayerService.class);
                        playIntent.setAction(ACTION_RESUME);
                        PendingIntent playPendingIntent = PendingIntent.getService(context, 0, playIntent, 0);
                        views.setOnClickPendingIntent(R.id.widget_play_button, playPendingIntent);
                    }
                    ComponentName componentName = new ComponentName(context, MusicWidgetProvider.class);
                    AppWidgetManager.getInstance(context).updateAppWidget(componentName, views);
                }


            };

            LocalBroadcastManager.getInstance(context).registerReceiver(receiver, new IntentFilter(PLAY));


            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }
}
