package com.example.musicplayer.controler;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.musicplayer.BeatBox;
import com.example.musicplayer.R;
import com.example.musicplayer.model.TabState;


/**
 * A simple {@link Fragment} subclass.
 */
public class MusicListFragment extends Fragment {

    public static final String ARG_TAB_STATE = "tab_state";
    private static final String ARG_ALBUMID = "album_or_singer_id";

    private RecyclerView mRecyclerView;
    private ListAda mRecyclerListAdapter;
    private long albumOrArtistID;
    private TabState mTabState;



    public MusicListFragment() {
        // Required empty public constructor
    }


    public static MusicListFragment newInstance(TabState tabState, long albumOrArtistID) {

        Bundle args = new Bundle();
        args.putSerializable(ARG_TAB_STATE, tabState);
        args.putLong(ARG_ALBUMID, albumOrArtistID);
        MusicListFragment fragment = new MusicListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        albumOrArtistID = getArguments().getLong(ARG_ALBUMID);
        mTabState = (TabState) getArguments().getSerializable(ARG_TAB_STATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_music_list, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view_music_list_for_album_or_singer);

        setRecyclerAdapter();
        return view;
    }

    private void setRecyclerAdapter() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mRecyclerListAdapter = new ListAda(BeatBox.getInstance(getContext()).getMusicsOfAlbum(albumOrArtistID),mTabState ,getContext());
        mRecyclerView.setAdapter(mRecyclerListAdapter);
    }

}
