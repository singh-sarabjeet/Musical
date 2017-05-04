package com.sarabjeet.musical.ui;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.data.SongContract;

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
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Add Action for click
            }
        });
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

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView song_title;

        public ViewHolder(View itemView) {
            super(itemView);
            song_title = (TextView) itemView.findViewById(R.id.song_title_textView);
        }

    }
}
