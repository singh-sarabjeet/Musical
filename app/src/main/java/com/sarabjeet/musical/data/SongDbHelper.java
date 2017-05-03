package com.sarabjeet.musical.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sarabjeet.musical.data.SongContract.SongData;

/**
 * Created by sarabjeet on 4/5/17.
 */

public class SongDbHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "song_database.db";

    public SongDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SONG_TABLE = "CREATE TABLE IF NOT EXISTS " + SongData.TABLE_NAME + " (" +
                SongData._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +

                SongData.COLUMN_TITLE + " TEXT NOT NULL, " +

                SongData.COLUMN_ALBUM + " TEXT NOT NULL, " +
                SongData.COLUMN_ARTIST + " TEXT NOT NULL," +
                SongData.COLUMN_PATH + " TEXT NOT NULL " + ");";

        db.execSQL(SQL_CREATE_SONG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SongData.TABLE_NAME);
        onCreate(db);
    }
}
