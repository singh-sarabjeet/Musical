package com.sarabjeet.musical.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.widget.RemoteViews;

import com.sarabjeet.musical.R;

/**
 * Created by sarabjeet on 10/5/17.
 */

public class MusicWidgetProvider extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int appWidgetId : appWidgetIds) {


            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
           /* views.setTextViewText(R.id.widget_song_artist, "Artist");
            views.setTextViewText(R.id.widget_song_title, "Title");
            views.setImageViewResource(R.id.widget_album_art, R.drawable.fallback_cover);
            views.setImageViewResource(R.id.widget_play_button, R.drawable.ic_play_arrow);
            views.setImageViewResource(R.id.widget_next_button, R.drawable.ic_skip_next);*/

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }


    }
}
