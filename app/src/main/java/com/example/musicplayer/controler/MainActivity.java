package com.example.musicplayer.controler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.musicplayer.R;

public class MainActivity extends AppCompatActivity {

    private ImageView buttonPlay;
    private LinearLayout bottmSheet;
    private Button buttonModalSheet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonPlay = findViewById(R.id.button_play);
        bottmSheet = findViewById(R.id.bottom_sheet);
        buttonModalSheet = findViewById(R.id.button_modal_bottom_sheet);
        buttonModalSheet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModalBottomSheetFragment modalBottomSheetFragment = ModalBottomSheetFragment.newInstance();
                modalBottomSheetFragment.show(getSupportFragmentManager() ,"Modal Bottom Sheet fragment");
            }
        });
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottmSheet.setMinimumHeight(200);
            }
        });
    }
}
