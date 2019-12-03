package com.example.musicplayer.model;

public class Artist {
    private long artist_id;
    private String artistName;
    private int numberOfAlbums;
    private int numberOfMusics;
    private String albumCoverPath;

    public Artist(long artist_id, String artistName, int numberOfAlbums, int numberOfMusics, String albumCoverPath) {
        this.artist_id = artist_id;
        this.artistName = artistName;
        this.numberOfAlbums = numberOfAlbums;
        this.numberOfMusics = numberOfMusics;
        this.albumCoverPath = albumCoverPath;
    }

    public long getArtist_id() {
        return artist_id;
    }

    public String getArtistName() {
        return artistName;
    }

    public int getNumberOfAlbums() {
        return numberOfAlbums;
    }

    public int getNumberOfMusics() {
        return numberOfMusics;
    }

    public String getAlbumCoverPath() {
        return albumCoverPath;
    }
}
