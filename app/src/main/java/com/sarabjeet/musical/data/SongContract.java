package com.sarabjeet.musical.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by sarabjeet on 4/5/17.
 */

public class SongContract {

    static final String AUTHORITY = "com.sarabjeet.musical";
    static final String PATH_SONGS = "songs";
    static final String PATH_SONG_WITH_TITLE = "songs/*";
    private static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static final class SongData implements BaseColumns {
        public static final Uri URI = BASE_URI.buildUpon().appendPath(PATH_SONGS).build();
        public static final String TABLE_NAME = "songs";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ALBUM = "album";
        public static final String COLUMN_ARTIST = "artist";
        public static final String COLUMN_PATH = "path";

        public static String getSongFromUri(Uri queryUri) {
            return queryUri.getLastPathSegment();
        }

    }


}
