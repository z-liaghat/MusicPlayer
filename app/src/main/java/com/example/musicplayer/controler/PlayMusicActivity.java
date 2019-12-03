package com.example.musicplayer.controler;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;

import com.example.musicplayer.model.Music;

public class PlayMusicActivity extends SingleFragmentActivity {

    private static final String EXTRA_INTENT_MUSIC = "com.example.musicplayer.controler.extraMusic";

    @Override
    public Fragment createFragment() {
        Music music = (Music)getIntent().getSerializableExtra(EXTRA_INTENT_MUSIC);
        return PlayMusicFragment.newInstance(music);
    }

   public static Intent newIntent(Context sourceContext , Music music) {
        Intent intent = new Intent(sourceContext , PlayMusicActivity.class);
        intent.putExtra(EXTRA_INTENT_MUSIC, music);
        return intent;

   }
}
