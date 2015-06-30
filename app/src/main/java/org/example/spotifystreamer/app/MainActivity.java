package org.example.spotifystreamer.app;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements View.OnClickListener {
    private final String TAG = this.getClass().getSimpleName();
    private final String FRAGMENT_TAG = "ArtistListFragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fm = getFragmentManager();
        ArtistListFragment fragment = new ArtistListFragment();
        FragmentTransaction ft = fm.beginTransaction();
//        fragment.setRetainInstance(true);
        ft.replace(R.id.activity_main_fragment_container, fragment, FRAGMENT_TAG);
        ft.commit();
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, Top10TracksActivity.class);
        intent.putExtra(Constants.SPOTIFY_ID, view.getTag().toString());
        startActivity(intent);
    }

}
