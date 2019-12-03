package com.example.musicplayer;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Artist;
import com.example.musicplayer.model.Music;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BeatBox  {

    public static final String ASSET_PATH_MUSIC = "musics";
    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static BeatBox mBeatBox;


    private MediaPlayer mMediaPlayer;
    private List<Music> mMusics;
    private List<Album> mAlbumList;


    private Music mCurrentMusic;
    private AssetManager mAssetManager;

    private Context mContext;
    private ArrayList<Artist> mArtistList;

    private boolean isShuffle;
    private int repeatAllOrOne;

    private UpdateUICallBack updateUICallBack;

    public static BeatBox getInstance(Context context) {
        if (mBeatBox == null)
            mBeatBox = new BeatBox(context);
        return mBeatBox;
    }

    private BeatBox(Context context) {

        mContext = context;
        mAssetManager = context.getAssets();
        mMusics = new ArrayList<>();
        mMediaPlayer = new MediaPlayer();
        mAlbumList = new ArrayList<>();
        mArtistList = new ArrayList<>();

        repeatAllOrOne = -1;
        isShuffle = false;
//        loadMusic();
        setOnCompletionListener();
        loadMusicFromStorage();
        loadAlbumInfo();
        loadArtistInto();

    }

    private void setOnCompletionListener() {
        mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isShuffle)
                    playShuffle();
                if (repeatAllOrOne == 0) // repeat all
                    playNextMusic();
                else if (repeatAllOrOne == 1) //repeat one
                    playMusic(mCurrentMusic);
                updateUICallBack.updateUI();
            }
        });
    }


    private void checkStoragePermission() {

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_CALENDAR)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions((Activity) mContext,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        }
    }

    private void loadArtistInto() {
        String[] columns = new String[]{
                MediaStore.Audio.Artists._ID,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS
//                MediaStore.Audio.Artists.Albums.ALBUM_ART
        };

        Cursor artistCursor = mContext.getContentResolver().query(
                MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                columns,
                null,
                null,
                null);


        long artistID;
        String artistName;
        int numberOfAlbums;
        int numberOfSongs;
        String path;

        if (artistCursor != null && artistCursor.moveToFirst()) {
            do {
                artistID = artistCursor.getLong(artistCursor.getColumnIndex(MediaStore.Audio.Artists._ID));
                artistName = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST));
                numberOfAlbums = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS));
                numberOfSongs = artistCursor.getInt(artistCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS));
//                 path = artistCursor.getString(artistCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                Artist artist = new Artist(artistID, artistName, numberOfAlbums, numberOfSongs, "");
                mArtistList.add(artist);
            }
            while (artistCursor.moveToNext());
        }
        artistCursor.close();

    }

    private void loadAlbumInfo() {

        String[] columns = new String[]{
                MediaStore.Audio.Albums._ID,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ARTIST_ID,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS,
                MediaStore.Audio.Albums.ARTIST
        };
        Cursor albumCursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, columns, null, null, null);
        if (albumCursor != null && albumCursor.moveToFirst()) {

            do {
                Long albumId = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums._ID));
                String albumName = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
                int numberOfSong = albumCursor.getInt(albumCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS));
                String artistName = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));
                long artistID = albumCursor.getLong(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST_ID));
                String path = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
                Album album = new Album(albumId, albumName, artistName, artistID, null, path, numberOfSong);
                mAlbumList.add(album);
            }
            while (albumCursor.moveToNext());
        }
        albumCursor.close();
    }


    private void loadMusicFromStorage() {
        ContentResolver contentResolver = mContext.getContentResolver();
        Uri musicUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri albumUri = MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI;
        String[] albumColumns = {MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM_ART};

        Cursor musicCursor = contentResolver.query(musicUri, null, null, null, null);
        Cursor albumCursor;


        if (musicCursor != null && musicCursor.moveToFirst()) {
            int titleColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = musicCursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            int artistColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int albumColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM);
            int durationColumn = musicCursor.getColumnIndex(MediaStore.Audio.Media.DURATION);
//            int albumID = musicCursor.getColumnIndex(MediaStore.Audio.AlbumColumns.ALBUM_ID);

            Music music;
            do {
                long thisId = musicCursor.getLong(idColumn);
                String thisTitle = musicCursor.getString(titleColumn);
                String musicArtist = musicCursor.getString(artistColumn);
                String album = musicCursor.getString(albumColumn);
                String duration = musicCursor.getString(durationColumn);
                long albumID = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                String coverPath = "";
                //get the album picture of music
                albumCursor = contentResolver.query(albumUri, albumColumns, MediaStore.Audio.Albums._ID + "= ?", new String[]{Long.toString(albumID)}, null);
                if (albumCursor != null && albumCursor.moveToFirst())
                    coverPath = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));

                music = new Music(thisId, thisTitle, album, albumID, musicArtist, Integer.parseInt(duration), coverPath);

                mMusics.add(music);

            } while (musicCursor.moveToNext());

        }
        musicCursor.close();
    }


    public Music playNextMusic() {

        int index = mMusics.indexOf(mCurrentMusic);
        Music nextMusic = mMusics.get((index + 1) % mMusics.size());
        mCurrentMusic = nextMusic;
        playMusic(nextMusic);
        return nextMusic;
    }

    public Music playPreviousMusic() {

        int index = mMusics.indexOf(mCurrentMusic);
        Music previousMusic = mMusics.get((mMusics.size() + (index - 1)) % mMusics.size());
        mCurrentMusic = previousMusic;
        playMusic(previousMusic);
        return previousMusic;
    }

    public void pauseMusic() {
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.pause();
        } else
            mMediaPlayer.start();

    }

    public void repeatOne() {
        playMusic(mCurrentMusic);
        mMediaPlayer.setLooping(true);
    }

    public void playAfterPause(){
        mMediaPlayer.start();
    }

    public void playMusic(Music music) {
        mCurrentMusic = music;
        if (mMediaPlayer.isPlaying()) {
            mMediaPlayer.release();
            //mMediaPlayer = new MediaPlayer();

//           mMediaPlayer.reset();
//            mMediaPlayer.seekTo(0);

        }
        try {

            mMediaPlayer = new MediaPlayer();
            setOnCompletionListener();
            long id = music.getId();
            Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
//            mMediaPlayer.setAudioAttributes(AudioAttribute);
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.setDataSource(mContext.getApplicationContext(), contentUri);

            mMediaPlayer.prepare();
            mMediaPlayer.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public List<Music> getMusics() {
        return mMusics;
    }

    public MediaPlayer getMediaPlayer() {
        return mMediaPlayer;
    }

    public List<Album> getAlbums() {
        return mAlbumList;
    }

    public List<Music> getMusicsOfAlbum(long albumId) {
        List<Music> musicOfAlbum = new ArrayList<>();
        for (int i = 0; i < mMusics.size(); i++) {
            if (mMusics.get(i).getAlbumId() == albumId)
                musicOfAlbum.add(mMusics.get(i));
        }
        return musicOfAlbum;
    }

    public ArrayList<Artist> getArtistList() {
        return mArtistList;
    }

    public boolean isShuffle() {
        return isShuffle;
    }

    public void setShuffle(boolean shuffle) {
        isShuffle = shuffle;
    }

    public int getRepeatAllOrOne() {
        return repeatAllOrOne;
    }

    public void setRepeatAllOrOne(int repeatAllOrOne) {
        this.repeatAllOrOne = repeatAllOrOne;
    }


    public void playShuffle() {
        Random random = new Random();
        int currentMusicIndex = random.nextInt(mMusics.size() - 1);
        mCurrentMusic = mMusics.get(currentMusicIndex);
        playMusic(mCurrentMusic);

    }

    public Music getCurrentMusic() {
        return mCurrentMusic;
    }

    public boolean isPlaying() {
        return mMediaPlayer.isPlaying();
    }

    public void setUpdateUICallBack(UpdateUICallBack updateUICallBack) {
        this.updateUICallBack = updateUICallBack;
    }


    public interface UpdateUICallBack{
         void updateUI();
    }
}
