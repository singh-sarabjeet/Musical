package com.sarabjeet.musical.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarabjeet.musical.R;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class AlbumsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.albums_fragment, container, false);
        RecyclerView albumsRecyclerView = (RecyclerView) rootView.findViewById(R.id.album_recycler_view);
        return rootView;
    }
}
