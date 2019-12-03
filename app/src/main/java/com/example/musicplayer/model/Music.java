package com.example.musicplayer.model;

import com.example.musicplayer.utilities.Utility;

import java.io.Serializable;

public class Music implements ModelItemInterface , Serializable {

    private long music_ID;
    private String mMusicName;
    private String mMusicAssetPath;
    private String mSinger;
    private String mLocation;
    private int duration;
    private String mAlbumName;
    private long albumId;
    private String mMusicCoverPath;


    public Music(long id , String musicName, String albumName ,long albumId, String singer, String location, int duration) {

        music_ID = id;
        this.mMusicName = musicName;
        this.mAlbumName = albumName;
        this.albumId = albumId;
        this.mSinger = singer;
        this.mLocation = location;
        this.duration = duration;
    }
    public Music(long id , String musicName, String albumName ,long albumId, String singer, int duration , String coverPath) {

        music_ID = id;
        this.mMusicName = musicName;
        this.mAlbumName = albumName;
        this.albumId = albumId;
        this.mSinger = singer;
        this.mMusicCoverPath = coverPath;
        this.duration = duration;
    }


    public Music(String mMusicName, String musicAssetPath) {
        this.mMusicName = mMusicName;
        this.mMusicAssetPath = musicAssetPath;
    }

    public String getMusicCoverPath() {
        return mMusicCoverPath;
    }

    public String getSinger() {
        return mSinger;
    }

    public String getMusicName() {
        return mMusicName;
    }

    public void setMusicName(String mMusicName) {
        this.mMusicName = mMusicName;
    }

    public String getMusicAssetPath() {
        return mMusicAssetPath;
    }

    public void setMusicAssetPath(String mMusicAssetPath) {
        this.mMusicAssetPath = mMusicAssetPath;
    }

    public long getId() {
        return music_ID;
    }

    public long getAlbumId() {
        return albumId;
    }

    public long getDuration() {
        return duration;
    }
    public String getDurationString() {
        return Utility.milliSecondsToTimer(duration);
    }
}
