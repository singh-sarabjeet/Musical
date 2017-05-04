package com.sarabjeet.musical.ui;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarabjeet.musical.R;
import com.sarabjeet.musical.data.SongContract;

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_ARTIST;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class ArtistsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = {"DISTINCT " + COLUMN_ARTIST};
    private ArtistsAdapter artistsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.artists_fragment, container, false);
        RecyclerView artistsRecyclerView = (RecyclerView) rootView.findViewById(R.id.artist_recycler_view);
        artistsAdapter = new ArtistsAdapter(getActivity());
        artistsRecyclerView.setAdapter(artistsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        artistsRecyclerView.setLayoutManager(linearLayoutManager);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), SongContract.SongData.URI, projection, null, null, COLUMN_ARTIST);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        artistsAdapter.setCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        artistsAdapter.setCursor(null);
    }
}
