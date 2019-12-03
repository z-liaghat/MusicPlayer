package com.example.musicplayer.controler;


import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicplayer.BeatBox;
import com.example.musicplayer.R;
import com.example.musicplayer.model.Music;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlayMusicFragment extends Fragment implements BeatBox.UpdateUICallBack {

    private static final String ARGS_Currenr_Music = "currenrMusic";

    private ImageView imageButtonPlay;
    private ImageView imageButtonPause;
    private ImageView imageButtonPrevious;
    private ImageView imageButtonNext;
    private ImageView imageButtonRepeat;
    private ImageView imageButtonShuffle;

    private TextView textViewMusicName;
    private TextView textViewArtistName;

    private ImageView imageViewAlbumArt;

    private SeekBar mSeekBar;

    private BeatBox mBeatBox;

    private Runnable updateSeekbarRunnable;
    private Handler mSeekbarUpdateHandler;


    private Music mMusic;

    public PlayMusicFragment() {
        // Required empty public constructor
    }

    public static PlayMusicFragment newInstance(Music music) {

        Bundle args = new Bundle();

        args.putSerializable(ARGS_Currenr_Music, music);
        PlayMusicFragment fragment = new PlayMusicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBeatBox = BeatBox.getInstance(getContext());
        mMusic = (Music) getArguments().getSerializable(ARGS_Currenr_Music);

        mBeatBox.setUpdateUICallBack(this);
        mSeekbarUpdateHandler = new Handler();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_play_music, container, false);
        initUI(view);
        setListener();
        setSeekBarListener();
        playMusic(mMusic);
        return view;
    }

    private void initUI(View view) {

        textViewMusicName = view.findViewById(R.id.text_view_music_name);
        textViewArtistName = view.findViewById(R.id.text_view_artist_name);
        textViewArtistName.setText(mMusic.getSinger());
        textViewMusicName.setText(mMusic.getMusicName());

        imageViewAlbumArt = view.findViewById(R.id.image_view_album_play);
        imageButtonPlay = view.findViewById(R.id.button_play);
        imageButtonPrevious = view.findViewById(R.id.button_back);
        imageButtonNext = view.findViewById(R.id.button_forward);
        imageButtonPause = view.findViewById(R.id.button_pause);

        imageButtonRepeat = view.findViewById(R.id.button_repeat);
        switch (mBeatBox.getRepeatAllOrOne()){
            case -1:
                imageButtonRepeat.setImageResource(R.drawable.ic_repeat_off);
                break;
            case 0:
                imageButtonRepeat.setImageResource(R.drawable.ic_repeat);
                break;
            case 1:
                imageButtonRepeat.setImageResource(R.drawable.ic_repeat_one);
                break;
        }

        imageButtonShuffle = view.findViewById(R.id.button_shuffle);
        if (mBeatBox.isShuffle()){
            imageButtonShuffle.setImageResource(R.drawable.ic_shuffle);
        }
        else
            imageButtonShuffle.setImageResource(R.drawable.ic_shuffle_disable);

        if (mMusic.getMusicCoverPath() != "")
            imageViewAlbumArt.setImageBitmap(BitmapFactory.decodeFile(mMusic.getMusicCoverPath()));

        mSeekBar = view.findViewById(R.id.seekbar_play_page);

    }

    private void setSeekBarListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (BeatBox.getInstance(getContext()).getMediaPlayer() != null && fromUser)
                    BeatBox.getInstance(getContext()).getMediaPlayer().seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void playMusic(Music music) {
        updateSeekBar(music);
        //playInBottomSheet(music);
    }

    private void updateSeekBar(Music music) {
        mSeekBar.setMax(mBeatBox.getMediaPlayer().getDuration() / 1000);
        //final Handler handler = new Handler();
        updateSeekbarRunnable = new Runnable() {
            @Override
            public void run() {
                if (mBeatBox.getMediaPlayer() != null) {
                    mSeekBar.setProgress(mBeatBox.getMediaPlayer().getCurrentPosition() / 1000);
                }
                mSeekbarUpdateHandler.postDelayed(this, 50);
            }
        };
        updateSeekbarRunnable.run();
    }

    private void setListener() {

        //layoutBottomSheet.setVisibility(View.VISIBLE);
//        imageButtonPause.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mBeatBox.pauseMusic();
//                imageButtonPlay.setImageResource(R.drawable.ic_play);
//            }
//        });
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //imageButtonPlay.setImageResource(R.drawable.ic_pause);
                if (mBeatBox.isPlaying()) {
                    mBeatBox.pauseMusic();
                    imageButtonPlay.setImageResource(R.drawable.ic_play_2);

                } else {
                    mBeatBox.playAfterPause();
                    imageButtonPlay.setImageResource(R.drawable.ic_pause_2);
                    //imageViewAlbumArt.setImageBitmap(BitmapFactory.decodeFile(mMusic.getMusicCoverPath()));
                }
            }
        });

        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeatBox.playNextMusic();
                updateMusicDetailUI();
            }
        });

        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeatBox.playPreviousMusic();
                updateMusicDetailUI();
            }
        });

        imageButtonRepeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (mBeatBox.getRepeatAllOrOne()){
                    case 1:
                        mBeatBox.setRepeatAllOrOne(-1);
                        Toast.makeText(getContext(), "repeat off", Toast.LENGTH_SHORT).show();
                        imageButtonRepeat.setImageResource(R.drawable.ic_repeat_off);
                        break;
                    case -1:
                        mBeatBox.setRepeatAllOrOne(0);
                        Toast.makeText(getContext(), "repeat all is on", Toast.LENGTH_SHORT).show();
                        imageButtonRepeat.setImageResource(R.drawable.ic_repeat);
                        break;
                    case 0:
                        mBeatBox.setRepeatAllOrOne(1);
                        Toast.makeText(getContext(), "repeat one is on", Toast.LENGTH_SHORT).show();
                        imageButtonRepeat.setImageResource(R.drawable.ic_repeat_one);
                        break;

                }
//                if (imageButtonRepeat.getDrawable() == getResources().getDrawable(R.drawable.ic_repeat)) {
//                    imageButtonRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat_one));
//                    mBeatBox.setRepeatAllOrOne(1);
//                    Toast.makeText(getContext(), "repeat one is on", Toast.LENGTH_SHORT).show();
//
//                } else {
//                    mBeatBox.setRepeatAllOrOne(0);
//                    imageButtonRepeat.setImageDrawable(getResources().getDrawable(R.drawable.ic_repeat));
//                    Toast.makeText(getContext(), "repeat all is on", Toast.LENGTH_SHORT).show();
//                }

            }
        });

        imageButtonShuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBeatBox.isShuffle()) {

                    mBeatBox.setShuffle(false);
                    imageButtonShuffle.setImageResource(R.drawable.ic_shuffle_disable);
                    Toast.makeText(getContext(), "shuffle is off", Toast.LENGTH_SHORT).show();

                } else {
                    mBeatBox.setShuffle(true);
                    imageButtonShuffle.setImageResource(R.drawable.ic_shuffle);
                    Toast.makeText(getContext(), "shuffle is on", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void updateMusicDetailUI() {
        mMusic = mBeatBox.getCurrentMusic();
        imageViewAlbumArt.setImageBitmap(BitmapFactory.decodeFile(mMusic.getMusicCoverPath()));
        textViewMusicName.setText(mMusic.getMusicName());
        textViewArtistName.setText(mMusic.getSinger());
    }

    @Override
    public void updateUI() {
        updateMusicDetailUI();
//        mMusic = mBeatBox.getCurrentMusic();
//        imageViewAlbumArt.setImageBitmap(BitmapFactory.decodeFile(mMusic.getMusicCoverPath()));
    }
}
