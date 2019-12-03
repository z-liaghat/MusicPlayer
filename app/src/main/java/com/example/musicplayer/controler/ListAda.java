package com.example.musicplayer.controler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicplayer.R;
import com.example.musicplayer.controler.holder.AlbumHolder;
import com.example.musicplayer.controler.holder.ItemHolder;
import com.example.musicplayer.controler.holder.MusicHolder;
import com.example.musicplayer.model.Album;
import com.example.musicplayer.model.Music;
import com.example.musicplayer.model.TabState;

import java.util.List;

public class ListAda extends RecyclerView.Adapter<ItemHolder> {
    private List items;
    private TabState mTabState;
    private Context mContext;

    public ListAda(List items, TabState tabState, Context context) {
        this.items = items;
        mTabState = tabState;
        mContext = context;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view;
        switch (mTabState) {
            case Music:
                view = inflater.inflate(R.layout.list_item_music, parent, false);
                return new MusicHolder(view, mContext);
            case Album:
                view = inflater.inflate(R.layout.list_item_album, parent, false);
                return new AlbumHolder(view, mContext);

            default:
                view = inflater.inflate(R.layout.list_item_music, parent, false);
                return new MusicHolder(view, mContext);
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
                break;
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}


