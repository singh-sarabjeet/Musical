package com.sarabjeet.musical.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarabjeet.musical.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class SongsFragment extends Fragment{
@BindView(R.id.songs_recycler_view)
    RecyclerView songsRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.songs_fragment, container, false);
        ButterKnife.bind(getActivity(),rootView);
        return rootView;
    }


}
