package com.example.musicplayer.controler.holder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicplayer.controler.MusicsListActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.Album;

public class AlbumHolder extends ItemHolder{
    private Context mContext;
    private Album mAlbum;
    private TextView textViewAlbumName;
    private TextView textViewSinger;
    private TextView textViewNumberOfSongs;
    private ImageView imageViewAlbum;

    public AlbumHolder(@NonNull View itemView ,Context context) {
        super(itemView);
        mContext = context;
        textViewAlbumName = itemView.findViewById(R.id.text_view_album_name);
        textViewSinger = itemView.findViewById(R.id.text_view_singer_of_album);
        textViewNumberOfSongs = itemView.findViewById(R.id.text_view_number_of_songs);
        imageViewAlbum = itemView.findViewById(R.id.image_view_album);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                mBeatBox.playMusic(mMusic);
//                updateSeekBar(mMusic);
//                playInBottomSheet(mMusic);
//                Intent intent = PlayMusicActivity.newIntent(mContext);
//                mContext.startActivity(intent);
                Intent intent = MusicsListActivity.newIntent(mContext ,mAlbum.getAlbum_ID());
                mContext.startActivity(intent);
            }
        });

    }

    public void bind(Album album) {
        mAlbum = album;
        textViewAlbumName.setText(mAlbum.getAlbumName());
        textViewSinger.setText(mAlbum.getSinger());
        textViewNumberOfSongs.setText(mAlbum.getNumberOFSongs() + "");
        if(mAlbum.getPath()!= null) {
            Bitmap bm = BitmapFactory.decodeFile(mAlbum.getPath());
            imageViewAlbum.setImageBitmap(bm);
        }
//        imageViewAlbum.
    }
}







