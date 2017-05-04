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

import static com.sarabjeet.musical.data.SongContract.SongData.COLUMN_TITLE;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class SongsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = {COLUMN_TITLE};
    private SongsAdapter songsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.songs_fragment, container, false);
        RecyclerView songsRecyclerView = (RecyclerView) rootView.findViewById(R.id.songs_recycler_view);
        songsAdapter = new SongsAdapter(getActivity());
        songsRecyclerView.setAdapter(songsAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        songsRecyclerView.setLayoutManager(linearLayoutManager);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(0, null, this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(getActivity(), SongContract.SongData.URI, projection, null, null, COLUMN_TITLE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        songsAdapter.setCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        songsAdapter.setCursor(null);
    }
}
