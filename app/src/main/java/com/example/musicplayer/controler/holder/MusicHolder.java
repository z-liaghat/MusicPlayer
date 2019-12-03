package com.example.musicplayer.controler.holder;


import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicplayer.BeatBox;
import com.example.musicplayer.R;
import com.example.musicplayer.controler.PlayMusicActivity;

import com.example.musicplayer.controler.TabFragment;
import com.example.musicplayer.model.Music;

public class MusicHolder extends ItemHolder {
    private Context mContext;
    private Music mMusic;
    private TextView textViewMusicName;
    private TextView textViewSinger;
    private TextView textViewDuration;
    private ImageView imageViewAlbumArt;
    private IPlayMusicCallBack iPlayMusicCallBack;


    public MusicHolder(@NonNull View itemView , final Context context ) {
        super(itemView);
        mContext = context;
        textViewMusicName = itemView.findViewById(R.id.text_view_music_name);
        textViewSinger = itemView.findViewById(R.id.text_view_singer_name);
        textViewDuration = itemView.findViewById(R.id.text_view_duration);
        imageViewAlbumArt = itemView.findViewById(R.id.image_view_music_album_picture);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BeatBox.getInstance(context).playMusic(mMusic);
//                iPlayMusicCallBack.playMusicOnBottomSheet(mMusic);
//                updateSeekBar(mMusic);
//                playInBottomSheet(mMusic);
                    Intent intent = PlayMusicActivity.newIntent(mContext ,mMusic);
                    mContext.startActivity(intent);
            }
        });

    }

    public void bind(Music music) {
        mMusic = music;
        textViewMusicName.setText(mMusic.getMusicName());
        textViewSinger.setText(mMusic.getSinger());
        textViewDuration.setText(mMusic.getDurationString() + "");

        if(mMusic.getMusicCoverPath()!= null) {
            imageViewAlbumArt.setImageBitmap(BitmapFactory.decodeFile(mMusic.getMusicCoverPath()));
        }
    }
    public void setIPlayMusicCallBack(IPlayMusicCallBack iPlayMusicCallBack){
        this.iPlayMusicCallBack = iPlayMusicCallBack;

    }
    public interface IPlayMusicCallBack{
        public void playMusicOnBottomSheet(Music music);
    }
}