package com.example.musicplayer.controler;


import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.musicplayer.BeatBox;
import com.example.musicplayer.R;
import com.example.musicplayer.controler.holder.AlbumHolder;
import com.example.musicplayer.controler.holder.ArtistHolder;
import com.example.musicplayer.controler.holder.ItemHolder;
import com.example.musicplayer.controler.holder.MusicHolder;
import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Artist;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.model.TabState;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabFragment extends Fragment implements MusicHolder.IPlayMusicCallBack {

    private static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;
    public static final String ARG_TAB_STATE = "tab_state";
    private static final String ARG_IsTabFragment = "is_tab_fragment";
    private static final String ARG_ALBUMID ="album_id" ;
    private BeatBox mBeatBox;
    private TabState mTabState;
    private RecyclerView mRecyclerView;
    private ListAdapter mRecyclerListAdapter;
    private boolean isTabFragment;

    //bottom sheet items:
    private ImageView imageButtonPlay;
    private ImageView imageButtonPause;
    private ImageView imageButtonPrevious;
    private ImageView imageButtonNext;

    private LinearLayout layoutBottomSheet;
    private SeekBar mSeekBar;
    private Runnable updateSeekbarRunnable;
    private Handler mSeekbarUpdateHandler;
    private MusicHolder mMusicHolder;


    public static TabFragment newInstance(TabState tabState, boolean isTabFragment) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TAB_STATE, tabState);
        args.putBoolean(ARG_IsTabFragment, isTabFragment);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static TabFragment newInstance(TabState tabState, boolean isTabFragment , long albumID) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TAB_STATE, tabState);
        args.putBoolean(ARG_IsTabFragment, isTabFragment);
        args.putLong(ARG_ALBUMID , albumID);
        TabFragment fragment = new TabFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTabState = (TabState) getArguments().getSerializable(ARG_TAB_STATE);
        isTabFragment = getArguments().getBoolean(ARG_IsTabFragment);
        checkStoragePermission();
        mBeatBox =  BeatBox.getInstance(getContext());

        mSeekbarUpdateHandler = new Handler();



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_tab_fragmnet, container, false);
        mSeekBar = view.findViewById(R.id.seekbar);
        mRecyclerView = view.findViewById(R.id.recycler_view);

        setRecyclerAdapter();


        imageButtonPlay = view.findViewById(R.id.button_play);
        imageButtonPrevious = view.findViewById(R.id.button_back);
        imageButtonNext = view.findViewById(R.id.button_forward);
        imageButtonPause = view.findViewById(R.id.button_pause);
        layoutBottomSheet = view.findViewById(R.id.layout_bottom_sheet);
        layoutBottomSheet.setVisibility(View.INVISIBLE);


        setSheetListener();
        setSeekBarListener();
//        if(mTabState==TabState.Music)
//          mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        else {
//            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity()));
//        }

        return view;
    }

    private void setSeekBarListener() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mBeatBox.getMediaPlayer() != null && fromUser)
                    mBeatBox.getMediaPlayer().seekTo(progress * 1000);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void setSheetListener() {
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mBeatBox.playMusic();
            }
        });
    }

    private void setRecyclerAdapter() {
        if (!isTabFragment) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            mRecyclerListAdapter = new ListAdapter(mBeatBox.getMusicsOfAlbum(1), mTabState);
        } else {
            switch (mTabState) {
                case Music:
                    mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                    mRecyclerListAdapter = new ListAdapter(mBeatBox.getMusics(), mTabState);
                    break;
                case Album:
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    mRecyclerListAdapter = new ListAdapter(mBeatBox.getAlbums(), mTabState);
                    break;
                case Singer:
                    mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                    mRecyclerListAdapter = new ListAdapter(mBeatBox.getArtistList(), mTabState);
                    break;
            }
        }

        mRecyclerView.setAdapter(mRecyclerListAdapter);

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

    private void playInBottomSheet(final Music music) {

        layoutBottomSheet.setVisibility(View.VISIBLE);
        imageButtonPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeatBox.pauseMusic();
            }
        });
        imageButtonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeatBox.playMusic(music);
            }
        });
        imageButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeatBox.playNextMusic();
            }
        });
        imageButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBeatBox.playPreviousMusic();
            }
        });
    }

    @Override
    public void playMusicOnBottomSheet(Music music) {
        updateSeekBar(music);
        playInBottomSheet(music);
    }


    public class ListAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List items;
        private TabState mTabState;

        public ListAdapter(List items, TabState tabState) {
            this.items = items;
            mTabState = tabState;
        }

        @NonNull
        @Override
        public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view;
            switch (mTabState) {
                case Music:
                    view = inflater.inflate(R.layout.list_item_music, parent, false);
                    MusicHolder  musicHolder =  new MusicHolder(view, getContext() );
                    return musicHolder;
                case Album:
                    view = inflater.inflate(R.layout.list_item_album, parent, false);
                    return new AlbumHolder(view, getContext());
                case Singer:
                    view = inflater.inflate(R.layout.list_item_artist, parent, false);
                    return new ArtistHolder(view, getContext());

                default:
                    view = inflater.inflate(R.layout.list_item_music, parent, false);
                    return new MusicHolder(view, getContext());
            }

        }


        @Override
        public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
            switch (mTabState) {
                case Music:
                    MusicHolder musicHolder = (MusicHolder) holder;
                    musicHolder.bind((Music) items.get(position));
                    break;

                case Album:
                    ((AlbumHolder) holder).bind((Album) items.get(position));
                    break;

                case Singer:
                    ((ArtistHolder) holder).bind((Artist) items.get(position));
                    break;
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    private void checkStoragePermission() {

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setPositiveButton("allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                ActivityCompat.requestPermissions(getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

//                                Intent intent = new Intent();
//                                Fragment fragment = getTargetFragment();
//                                fragment.onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);


                            }
                        })
                        .setNegativeButton("not allow", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                getActivity().finish();

                            }
                        })
                        .setView(getView())
                        .create();

                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }

        } else
            mBeatBox =  BeatBox.getInstance(getContext());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted,
                    mBeatBox =  BeatBox.getInstance(getContext());

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Snackbar.make(getView(), "This app need your storage permission", Snackbar.LENGTH_LONG).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request.

        }
    }

}
