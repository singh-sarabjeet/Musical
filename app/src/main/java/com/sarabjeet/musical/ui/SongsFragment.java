package com.sarabjeet.musical.ui;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sarabjeet.musical.R;

/**
 * Created by sarabjeet on 2/5/17.
 */

public class SongsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.songs_fragment, container, false);
        return rootView;
    }


}
