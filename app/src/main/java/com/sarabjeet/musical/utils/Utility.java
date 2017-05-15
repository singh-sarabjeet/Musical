package com.sarabjeet.musical.utils;

import android.database.Cursor;

import com.sarabjeet.musical.data.SongModel;

import java.util.ArrayList;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ALBUM;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_PATH;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;

/**
 * Created by sarabjeet on 15/5/17.
 */

public class Utility {

    public ArrayList<SongModel> getPlaylist(Cursor mCursor, int position) {
        SongModel songModel;
        ArrayList<SongModel> playList = new ArrayList<>();
        mCursor.moveToPosition(position);
        do {
            songModel = new SongModel();
            songModel.setAudioTitle(mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE)));
            songModel.setAudioArtist(mCursor.getString(mCursor.getColumnIndex(COLUMN_ARTIST)));
            songModel.setAudioPath(mCursor.getString(mCursor.getColumnIndex(COLUMN_PATH)));
            songModel.setAudioAlbum(mCursor.getString(mCursor.getColumnIndex(COLUMN_ALBUM)));
            playList.add(songModel);

        } while (mCursor.moveToNext());

        return playList;

    }
}
