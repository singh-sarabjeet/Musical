package com.sarabjeet.musical.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sarabjeet.musical.R;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;

/**
 * Created by sarabjeet on 4/5/17.
 */

public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private Cursor mCursor;
    private Activity mContext;

    public ArtistsAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.artists_list_item, parent, false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        holder.artist_title.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_ARTIST)));

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

        TextView artist_title;
        View artistName;

        public ViewHolder(View itemView) {
            super(itemView);
            artistName = itemView.findViewById(R.id.artist_title_textView);
            artist_title = (TextView) artistName;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            Intent intent = new Intent(mContext, ArtistDetailActivity.class);
            intent.putExtra("artist_title", mCursor.getString(mCursor.getColumnIndex(COLUMN_ARTIST)));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(mContext, artistName, "artist_title_transition");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            }
        }

    }
}
