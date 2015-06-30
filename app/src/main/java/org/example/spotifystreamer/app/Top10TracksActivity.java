package org.example.spotifystreamer.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Picture;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import kaaes.spotify.webapi.android.models.TracksPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * Created by tom on 6/29/15.
 */
public class Top10TracksActivity extends ActionBarActivity {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private Top10TracksAdapter mAdapter;
    private ArrayList<Top10TrackResult> mResults = new ArrayList<Top10TrackResult>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_top10tracks);

        Intent intent = getIntent();
        String spotifyId = intent.getStringExtra(Constants.SPOTIFY_ID);
        Log.d(TAG, spotifyId);

        mRecyclerView = (RecyclerView)findViewById(R.id.track_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new Top10TracksAdapter(this);
        mRecyclerView.setAdapter(mAdapter);

        Top10TrackTask task = new Top10TrackTask();
        task.execute(new String[] {spotifyId});
    }

    private void loadTop10Tracks(Tracks tracks) {
        mResults.clear();
        for (Track track : tracks.tracks) {
            SpotifyImageHandler imageHandler = new SpotifyImageHandler(track.album.images);
            String imageUrl = imageHandler.getImageForSize(200);
            String albumTitle = track.album.name;
            String trackTitle = track.name;
            String previewUrl = track.preview_url;

            Top10TrackResult result = new Top10TrackResult(imageUrl, albumTitle, trackTitle, previewUrl);
            mResults.add(result);
        }
        mAdapter.notifyDataSetChanged();
    }

    private class Top10TracksAdapter extends RecyclerView.Adapter<Top10TracksViewHolder> {
      //  private ArrayList<Top10TrackResult> mResults;
        private Context mContext;

        public Top10TracksAdapter(Context context) {
            mContext = context;
     //       mResults = results;
        }

        @Override
        public Top10TracksViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = getLayoutInflater().inflate(R.layout.track_list_item, parent, false);
            return new Top10TracksViewHolder(view);
        }

        @Override
        public void onBindViewHolder(Top10TracksViewHolder holder, int position) {
            Top10TrackResult result = mResults.get(position);
            holder.mAlbumTitle.setText(result.getAlbumTitle());
            holder.mTrackTitle.setText(result.getTrackTitle());
            holder.itemView.setTag(result.getPreviewUrl());

            Picasso.with(mContext)
                    .load(result.getImageUrl())
                    .fit()
                    .into(holder.mAlbumImage);
        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }
    private class Top10TracksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mAlbumImage;
        private TextView mAlbumTitle;
        private TextView mTrackTitle;

        public Top10TracksViewHolder(View itemView) {
            super(itemView);
            mAlbumImage = (ImageView)itemView.findViewById(R.id.track_list_item_imageview);
            mAlbumTitle = (TextView)itemView.findViewById(R.id.track_list_item_album_name);
            mTrackTitle = (TextView)itemView.findViewById(R.id.track_list_item_track_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Toast.makeText(view.getContext(), "Clicked on " + view.getTag().toString(), Toast.LENGTH_SHORT).show();

        }
    }

    private class Top10TrackTask extends AsyncTask<String, Void, Tracks> {

        @Override
        protected Tracks doInBackground(String... params) {
            String spotifyId = params[0];
            HashMap<String, Object> queryParams = new HashMap<String, Object>();
            SpotifyApi spotifyApi = new SpotifyApi();
            SpotifyService spotify = spotifyApi.getService();
            queryParams.put(spotify.COUNTRY, "US");


            Tracks tracks = null;
            try {
                tracks = spotify.getArtistTopTrack(spotifyId, queryParams);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tracks;
        }

        @Override
        protected void onPostExecute(Tracks tracks) {
            loadTop10Tracks(tracks);
        }
    }
}
