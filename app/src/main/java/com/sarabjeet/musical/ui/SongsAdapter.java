package com.sarabjeet.musical.ui;

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
import com.sarabjeet.musical.data.SongContract;
import com.sarabjeet.musical.data.SongModel;
import com.sarabjeet.musical.sync.MusicPlayerService;
import com.sarabjeet.musical.utils.Utility;

import java.util.ArrayList;

import static com.sarabjeet.musical.utils.Constants.ACTION.ACTION_PLAY;

/**
 * Created by sarabjeet on 4/5/17.
 */

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public SongsAdapter(Context context) {
        mContext = context;
    }

    public void setCursor(Cursor cursor) {
        mCursor = cursor;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.songs_list_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.song_title.setText(mCursor.getString(mCursor.getColumnIndex(SongContract.SongData.COLUMN_TITLE)));

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (mCursor != null) {
            count = mCursor.getCount();
        }
        return count;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView song_title;

        public ViewHolder(View itemView) {
            super(itemView);
            song_title = (TextView) itemView.findViewById(R.id.song_title_textView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            ArrayList<SongModel> playList = new Utility().getPlaylist(mCursor, getAdapterPosition());
            Bundle bundle = new Bundle();
            bundle.putSerializable(mContext.getString(R.string.playlist), playList);
            Intent intent = new Intent(mContext, MusicPlayerService.class);
            intent.setAction(ACTION_PLAY);
            intent.putExtras(bundle);
            mContext.startService(intent);
        }
    }
}
