package com.sarabjeet.musical.data;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by sarabjeet on 10/5/17.
 */

public class MediaPlayerModel implements Parcelable {

    // Creator
    public static final Parcelable.Creator CREATOR
            = new Parcelable.Creator() {
        public MediaPlayerModel createFromParcel(Parcel in) {
            return new MediaPlayerModel(in);
        }

        @Override
        public MediaPlayerModel[] newArray(int size) {
            return new MediaPlayerModel[size];
        }

    };
    private MediaPlayer mediaPlayer;
    private String songTitle;
    private String songArtist;
    private byte[] data;

    // "De-parcel object
    public MediaPlayerModel(Parcel in) {
        songTitle = in.readString();
        songArtist = in.readString();

    }

    public String getSongTitle() {
        return songTitle;
    }

    public void setSongTitle(String songTitle) {
        songTitle = songTitle;
    }

    public String getSongArtist() {
        return songArtist;
    }

    public void setSongArtist(String songArtist) {
        songArtist = songArtist;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setMediaPlayer(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(songTitle);
        dest.writeString(songArtist);

    }
}
