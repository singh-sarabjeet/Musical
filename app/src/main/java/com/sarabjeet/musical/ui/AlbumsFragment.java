package com.sarabjeet.musical.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.data.SongContract;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ALBUM;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;
import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_PATH;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class AlbumsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {
    String[] projection = {"DISTINCT " + COLUMN_ALBUM, COLUMN_PATH, COLUMN_ARTIST};
    String selection = "(1) GROUP BY " + COLUMN_ALBUM;
    private AlbumsAdapter albumsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.albums_fragment, container, false);
        RecyclerView albumsRecyclerView = (RecyclerView) rootView.findViewById(R.id.album_recycler_view);
        albumsAdapter = new AlbumsAdapter(getActivity());
        albumsRecyclerView.setAdapter(albumsAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        albumsRecyclerView.setLayoutManager(gridLayoutManager);
        return rootView;
    }

    @Override

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), SongContract.SongData.URI, projection, selection, null, COLUMN_ALBUM);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        albumsAdapter.setCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        albumsAdapter.setCursor(null);
    }
}
