package com.sarabjeet.musical.ui;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.data.SongContract;
import com.squareup.picasso.Picasso;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ALBUM;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;

/**
 * Created by sarabjeet on 6/5/17.
 */

public class AlbumDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private final float aspectRatio = 1.5f;
    String[] projection = {COLUMN_TITLE};
    String selection;
    AlbumDetailAdapter albumSongListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);

        ImageView albumArt = (ImageView) findViewById(R.id.album_art);
        RecyclerView albumSongList = (RecyclerView) findViewById(R.id.album_detail_recycler_view);
        albumSongListAdapter = new AlbumDetailAdapter(this);
        albumSongList.setAdapter(albumSongListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        albumSongList.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        String albumName = intent.getStringExtra("album_title");
        selection = COLUMN_ALBUM + " = '" + albumName + "'";
        byte[] data = intent.getByteArrayExtra("album_cover");
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            albumArt.setImageBitmap(bitmap); //associated cover art in bitmap
            albumArt.setAdjustViewBounds(true);
        } else {
            Picasso.with(this)
                    .load(R.drawable.fallback_cover)
                    .resize(500, 500)
                    .centerCrop()
                    .into(albumArt);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

            int maxHeight = (int) (albumArt.getMaxWidth() / aspectRatio);
            albumArt.setMaxHeight(maxHeight);
            getLoaderManager().initLoader(0, null, this);
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                supportFinishAfterTransition();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, SongContract.SongData.URI, projection, selection, null, COLUMN_TITLE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        albumSongListAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        albumSongListAdapter.setCursor(null);
    }
}