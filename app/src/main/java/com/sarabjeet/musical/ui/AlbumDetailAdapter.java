package com.sarabjeet.musical.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.sync.MusicPlayerService;
import com.sarabjeet.musical.utils.Utility;

import java.util.ArrayList;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;
import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PLAY;

/**
 * Created by sarabjeet on 6/5/17.
 */

public class AlbumDetailAdapter extends RecyclerView.Adapter<AlbumDetailAdapter.ViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public AlbumDetailAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.album_song_list_item, parent, false);
        final AlbumDetailAdapter.ViewHolder vh = new AlbumDetailAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.albumSongTitle.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_TITLE)));
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount();
        }
        return count;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView albumSongTitle;

        public ViewHolder(View itemView) {
            super(itemView);
            albumSongTitle = (TextView) itemView.findViewById(R.id.album_song_title_textView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            ArrayList playList = new Utility().getPlaylist(mCursor, getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(mContext.getString(R.string.playlist), playList);
            Intent intent = new Intent(mContext, MusicPlayerService.class);
            intent.setAction(ACTION_PLAY);
            intent.putExtras(bundle);
            mContext.startService(intent);
        }

    }
}
