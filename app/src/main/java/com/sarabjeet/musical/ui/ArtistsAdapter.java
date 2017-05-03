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

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private Cursor mCursor;
    private Context mContext;

    public ArtistsAdapter(Cursor cursor, Context context) {
        mCursor = cursor;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.artists_list_item, parent, false);
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
        holder.artist_title.setText(mCursor.getString(mCursor.getColumnIndex(SongContract.SongData.COLUMN_ARTIST)));

    }


    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView artist_title;

        public ViewHolder(View itemView) {
            super(itemView);
            artist_title = (TextView) itemView.findViewById(R.id.artist_title_textView);
        }

    }
}
