package com.example.musicplayer.controler.holder;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.musicplayer.controler.MusicsListActivity;
import com.example.musicplayer.R;
import com.example.musicplayer.model.Artist;

public class ArtistHolder extends ItemHolder{
    private Context mContext;
    private Artist mArtist;
    private TextView textViewArtistName;
    private TextView textViewNumberOfAlbums;
    private TextView textViewNumberOfSongs;
    private ImageView imageViewAlbum;

    public ArtistHolder(@NonNull View itemView , Context context) {
        super(itemView);
        mContext = context;
        textViewArtistName = itemView.findViewById(R.id.text_view_artist_name);
        textViewNumberOfAlbums = itemView.findViewById(R.id.text_view_number_of_albums);
        textViewNumberOfSongs = itemView.findViewById(R.id.text_view_number_of_tracks);
//        imageViewAlbum = itemView.findViewById(R.id.image_view_artist_album);
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = MusicsListActivity.newIntent(mContext ,mArtist.getArtist_id());
                mContext.startActivity(intent);

            }
        });

    }

    public void bind(Artist artist) {
        mArtist= artist;
        textViewArtistName.setText(mArtist.getArtistName());
        textViewNumberOfAlbums.setText(mArtist.getNumberOfAlbums()+"");
        textViewNumberOfSongs.setText(mArtist.getNumberOfMusics() + "");
//        Bitmap bm= BitmapFactory.decodeFile(mArtist.getAlbumCoverPath());
//        imageViewAlbum.setImageBitmap(bm);
//        imageViewAlbum.
    }
}
