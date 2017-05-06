package com.sarabjeet.musical.ui;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
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

        public ViewHolder(View itemView) {
            super(itemView);
            album_title = (TextView) itemView.findViewById(R.id.album_title_textView);
            albumView = itemView.findViewById(R.id.album_art);
            album_art = (ImageView) albumView;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            mCursor.moveToPosition(getAdapterPosition());
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(mCursor.getString(mCursor.getColumnIndex(COLUMN_PATH)));

            byte[] data = mmr.getEmbeddedPicture();


            Intent intent = new Intent(mContext, AlbumDetailActivity.class);
            intent.putExtra("album_cover", data);
            intent.putExtra("album_title", mCursor.getString(mCursor.getColumnIndex(COLUMN_ALBUM)));
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(mContext, albumView, "album_cover");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                mContext.startActivity(intent, options.toBundle());
            } else mContext.startActivity(intent);
        }

    }
}
