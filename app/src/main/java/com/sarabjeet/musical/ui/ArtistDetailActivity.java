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

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_PATH;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;

/**
 * Created by sarabjeet on 7/5/17.
 */

public class ArtistDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = {COLUMN_TITLE, COLUMN_ARTIST, COLUMN_PATH};
    String selection;
    ArtistDetailAdapter artistDetailAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_detail);

        ImageView artistPlaceholderArt = (ImageView) findViewById(R.id.artist_placeholder);
        TextView artistTitle = (TextView) findViewById(R.id.artist_title_textView);
        RecyclerView artistSongList = (RecyclerView) findViewById(R.id.artist_detail_recycler_view);
        artistDetailAdapter = new ArtistDetailAdapter(this);
        artistSongList.setAdapter(artistDetailAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        artistSongList.setLayoutManager(linearLayoutManager);
        Intent intent = getIntent();
        String artistName = intent.getStringExtra("artist_title");
        artistTitle.setText(artistName);
        selection = COLUMN_ARTIST + " = '" + artistName + "'";
        Log.d("SELECTION", selection);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.artist_placeholder);
        artistPlaceholderArt.setImageBitmap(bitmap);
        artistPlaceholderArt.setAdjustViewBounds(true);


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
        artistDetailAdapter.setCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        artistDetailAdapter.setCursor(null);
    }
}
