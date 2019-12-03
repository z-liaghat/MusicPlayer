package com.example.musicplayer.controler.holder;

import android.view.Display;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.model.ModelItemInterface;
import com.example.musicplayer.model.Music;

public  class ItemHolder extends RecyclerView.ViewHolder {



    public ItemHolder(@NonNull View itemView) {
        super(itemView);
    }

    public void bind(ModelItemInterface item){}
}
