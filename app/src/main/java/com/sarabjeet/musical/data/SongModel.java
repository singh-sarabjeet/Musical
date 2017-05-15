package com.sarabjeet.musical.data;

import java.io.Serializable;

/**
 * Created by sarabjeet on 3/5/17.
 */

public class SongModel implements Serializable {
    private String audioPath;
    private String audioTitle;
    private String audioAlbum;
    private String audioArtist;

    public String getAudioPath() {
        return audioPath;
    }

    public void setAudioPath(String audioPath) {
        this.audioPath = audioPath;
    }

    public String getAudioTitle() {
        return audioTitle;
    }

    public void setAudioTitle(String audioTitle) {
        this.audioTitle = audioTitle;
    }

    public String getAudioAlbum() {
        return audioAlbum;
    }

    public void setAudioAlbum(String audioAlbum) {
        this.audioAlbum = audioAlbum;
    }

    public String getAudioArtist() {
        return audioArtist;
    }

    public void setAudioArtist(String audioArtist) {
        this.audioArtist = audioArtist;
    }
}
