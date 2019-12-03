package com.example.musicplayer.model;

import java.util.List;

public class Album implements ModelItemInterface{
    private long album_ID;
    private String albumName;
    private String singer;
    private long singer_ID;
    private List<Music> musics;
    private String path;
    private int numberOFSongs;

    public Album(long album_ID, String albumName, String singer, long singer_ID, List<Music> musics, String path, int numberOFSongs) {
        this.album_ID = album_ID;
        this.albumName = albumName;
        this.singer = singer;
        this.singer_ID = singer_ID;
        this.musics = musics;
        this.path = path;
        this.numberOFSongs = numberOFSongs;
    }

    public long getAlbum_ID() {
        return album_ID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public String getSinger() {
        return singer;
    }

    public long getSinger_ID() {
        return singer_ID;
    }

    public List<Music> getMusics() {
        return musics;
    }

    public String getPath() {
        return path;
    }

    public int getNumberOFSongs() {
        return numberOFSongs;
    }
}
