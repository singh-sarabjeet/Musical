package com.sarabjeet.musical.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sarabjeet.musical.R;
import com.squareup.picasso.Picasso;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ALBUM;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_PATH;

/**
 * Created by sarabjeet on 5/5/17.
 */

class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private Cursor mCursor;
    private Activity mContext;

    public AlbumsAdapter(Activity context) {
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.albums_grid_item, parent, false);
        final AlbumsAdapter.ViewHolder vh = new AlbumsAdapter.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(AlbumsAdapter.ViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(mCursor.getString(mCursor.getColumnIndex(COLUMN_PATH)));

        byte[] data = mmr.getEmbeddedPicture();
        // convert the byte array to a bitmap
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            holder.album_art.setImageBitmap(bitmap); //associated cover art in bitmap
            holder.album_art.setAdjustViewBounds(true);
            holder.album_art.setLayoutParams(new LinearLayout.LayoutParams(500, 500));
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.fallback_cover)
                    .resize(500, 500)
                    .centerCrop()
                    .into(holder.album_art);
        }
        holder.album_title.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_ALBUM)));
        holder.albumArtist.setText(mCursor.getString(mCursor.getColumnIndex(COLUMN_ARTIST)));

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

        TextView album_title;
        ImageView album_art;
        View albumView;
        View albumTitle;
        View album_artist;
        TextView albumArtist;

        public ViewHolder(View itemView) {
            super(itemView);
            albumTitle = itemView.findViewById(R.id.album_title_textView);
            album_title = (TextView) albumTitle;
            albumView = itemView.findViewById(R.id.album_art);
            album_art = (ImageView) albumView;
            album_artist = itemView.findViewById(R.id.album_artist_name_textView);
            albumArtist = (TextView) album_artist;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mCursor.getString(mCursor.getColumnIndex(COLUMN_PATH)));
            byte[] data = mmr.getEmbeddedPicture();

            Intent intent = new Intent(mContext, AlbumDetailActivity.class);
            intent.putExtra(mContext.getString(R.string.intent_extra_album_art), data);
            intent.putExtra(mContext.getString(R.string.intent_extra_album_name),
                    mCursor.getString(mCursor.getColumnIndex(COLUMN_ALBUM)));
            intent.putExtra(mContext.getString(R.string.intent_extra_album_artist),
                    mCursor.getString(mCursor.getColumnIndex(COLUMN_ARTIST)));
            Pair<View, String> p1 =
                    Pair.create(albumView, mContext.getString(R.string.album_art_transition));
            Pair<View, String> p2 =
                    Pair.create(albumTitle, mContext.getString(R.string.album_title_transition));
            Pair<View, String> p3 =
                    Pair.create(album_artist, mContext.getString(R.string.album_artist_transition));

            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(mContext, p1, p2, p3);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            } else mContext.startActivity(intent);
        }

    }
}
