package org.example.spotifystreamer.app;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ArtistListFragment extends Fragment {
    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.artist_list_fragment, container, false);
    }
}
