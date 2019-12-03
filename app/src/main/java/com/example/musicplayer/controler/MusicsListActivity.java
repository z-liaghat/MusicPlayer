package com.example.musicplayer.controler;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.musicplayer.model.TabState;

public class MusicsListActivity extends SingleFragmentActivity {

    private static final String EXTRA_LIST_ITEM_ID = "com.example.musicplayer.listItemID";
    private long mAlbumId;
    @Override
    public Fragment createFragment() {

        mAlbumId = getIntent().getLongExtra(EXTRA_LIST_ITEM_ID ,0);
        return MusicListFragment.newInstance(TabState.Music  ,mAlbumId);
    }

    public static Intent newIntent(Context sourceContext , long albumId){
        Intent intent = new Intent(sourceContext , MusicsListActivity.class );
        intent.putExtra(EXTRA_LIST_ITEM_ID, albumId);
        return intent;
    }
}
