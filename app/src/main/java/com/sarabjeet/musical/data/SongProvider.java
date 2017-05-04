package com.sarabjeet.musical.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by sarabjeet on 4/5/17.
 */

public class SongProvider extends ContentProvider {

    private static final int SONG = 100;
    private static final int SONG_FOR_TITLE = 101;

    private static final UriMatcher uriMatcher = buildUriMatcher();

    private SongDbHelper dbHelper;

    private static UriMatcher buildUriMatcher() {
        UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        matcher.addURI(SongContract.AUTHORITY, SongContract.PATH_SONGS, SONG);
        matcher.addURI(SongContract.AUTHORITY, SongContract.PATH_SONG_WITH_TITLE, SONG_FOR_TITLE);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        dbHelper = new SongDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor returnCursor;
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case SONG:
                returnCursor = db.query(
                        SongContract.SongData.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case SONG_FOR_TITLE:
                returnCursor = db.query(
                        SongContract.SongData.TABLE_NAME,
                        projection,
                        SongContract.SongData.COLUMN_TITLE + " = ?",
                        new String[]{SongContract.SongData.getSongFromUri(uri)},
                        null,
                        null,
                        sortOrder
                );

                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null) {
            returnCursor.setNotificationUri(context.getContentResolver(), uri);
        }

        return returnCursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Uri returnUri;

        switch (uriMatcher.match(uri)) {
            case SONG:
                db.insert(
                        SongContract.SongData.TABLE_NAME,
                        null,
                        values
                );
                returnUri = SongContract.SongData.URI;
                break;
            default:
                throw new UnsupportedOperationException("Unknown URI:" + uri);
        }

        Context context = getContext();
        if (context != null) {
            context.getContentResolver().notifyChange(uri, null);
        }

        return returnUri;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SONG:
                db.delete(SongContract.SongData.TABLE_NAME, null, null);
        }
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int bulkInsert(@NonNull Uri uri, @NonNull ContentValues[] values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case SONG:
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        db.insert(
                                SongContract.SongData.TABLE_NAME,
                                null,
                                value
                        );
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }

                Context context = getContext();
                if (context != null) {
                    context.getContentResolver().notifyChange(uri, null);
                }

                return returnCount;
            default:
                return super.bulkInsert(uri, values);
        }


    }
}
