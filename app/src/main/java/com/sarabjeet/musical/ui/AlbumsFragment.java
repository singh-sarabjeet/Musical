package com.sarabjeet.musical.ui;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarabjeet.musical.R;

import butterknife.BindView;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class AlbumsFragment extends Fragment {
    @BindView(R.id.album_recycler_view)
    RecyclerView albumRecyclerView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.albums_fragment, container, false);

        return rootView;
    }
}
