package com.sarabjeet.musical.sync;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;

import com.sarabjeet.musical.data.SongModel;

import java.util.ArrayList;
import java.util.List;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ALBUM;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_PATH;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;
import static com.sarabjeet.musical.data.SongContract.SongData.URI;

/**
 * Created by sarabjeet on 5/5/17.
 */

public class LibrarySyncIntentService extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public LibrarySyncIntentService() {
        super(LibrarySyncIntentService.class.getName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        List<ContentValues> library = getAllAudioFromDevice(this);
        ContentValues[] values = library.toArray(new ContentValues[library.size()]);
        getContentResolver().delete(URI, null, null);
        getContentResolver().bulkInsert(URI, values);
    }

    public List<ContentValues> getAllAudioFromDevice(final Context context) {

        List<ContentValues> libraryData = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Audio.AudioColumns.DATA, MediaStore.Audio.AudioColumns.ALBUM, MediaStore.Audio.ArtistColumns.ARTIST,};
        Cursor c = context.getContentResolver().query(uri, projection, null, null, null);
        if (c != null) {
            while (c.moveToNext()) {
                SongModel SongModel = new SongModel();
                String path = c.getString(0);
                String album = c.getString(1);
                String artist = c.getString(2);
                String name = path.substring(path.lastIndexOf("/") + 1);

                SongModel.setAudioTitle(name);
                SongModel.setAudioAlbum(album);
                SongModel.setAudioArtist(artist);
                SongModel.setAudioPath(path);

                ContentValues values = new ContentValues();
                values.put(COLUMN_TITLE, name);
                values.put(COLUMN_ALBUM, album);
                values.put(COLUMN_ARTIST, artist);
                values.put(COLUMN_PATH, path);
                libraryData.add(values);
            }
            c.close();
        }

        return libraryData;
    }
}
