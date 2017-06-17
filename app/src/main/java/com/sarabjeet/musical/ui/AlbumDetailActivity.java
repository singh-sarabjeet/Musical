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
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.data.SongContract;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ALBUM;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_PATH;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;

/**
 * Created by sarabjeet on 6/5/17.
 */

public class AlbumDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = {COLUMN_TITLE, COLUMN_ALBUM, COLUMN_PATH, COLUMN_ARTIST};
    String selection;
    AlbumDetailAdapter albumSongListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_detail);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        ImageView albumArt = (ImageView) findViewById(R.id.album_art);
        TextView albumSongTitle = (TextView) findViewById(R.id.album_title_textView);
        TextView albumArtist = (TextView) findViewById(R.id.album_artist);
        RecyclerView albumSongList = (RecyclerView) findViewById(R.id.album_detail_recycler_view);

        albumSongListAdapter = new AlbumDetailAdapter(this);
        albumSongList.setAdapter(albumSongListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        albumSongList.setLayoutManager(linearLayoutManager);

        Intent intent = getIntent();
        String albumName = intent.getStringExtra(getString(R.string.intent_extra_album_name));
        albumSongTitle.setText(albumName);
        Log.d("ARTIST", intent.getStringExtra(getString(R.string.intent_extra_album_artist)));
        albumArtist.setText(intent.getStringExtra(getString(R.string.intent_extra_album_artist)));
        selection = COLUMN_ALBUM + " = '" + albumName + "'";
        byte[] data = intent.getByteArrayExtra(getString(R.string.intent_extra_album_art));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        if (data != null) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            albumArt.setImageBitmap(bitmap); //associated cover art in bitmap
            albumArt.setAdjustViewBounds(true);
        } else {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.fallback_cover);
            albumArt.setImageBitmap(bitmap);
            albumArt.setAdjustViewBounds(true);
        }

        getLoaderManager().initLoader(0, null, this);
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
