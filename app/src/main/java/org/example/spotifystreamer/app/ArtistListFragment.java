package org.example.spotifystreamer.app;

import android.app.Fragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Pager;

import java.util.ArrayList;

public class ArtistListFragment extends Fragment implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();
    private RecyclerView mRecyclerView;
    private ArtistSearchResultsAdapter mAdapter;

    private ArrayList<ArtistSearchResult> mResults = new ArrayList<ArtistSearchResult>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.artist_list_fragment, container, false);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.artist_list_recyclerview);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        EditText searchQueryText = (EditText)getView().findViewById(R.id.search_text);
        searchQueryText.setOnEditorActionListener(this);

        mRecyclerView = (RecyclerView)getView().findViewById(R.id.artist_list_recyclerview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mAdapter = new ArtistSearchResultsAdapter(getActivity());
        mRecyclerView.setAdapter(mAdapter);

        setRetainInstance(true);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Log.d(TAG, "The search text is " + v.getText());
            SpotifyArtistSearchTask task = new SpotifyArtistSearchTask();
            task.execute(v.getText().toString());
        }
        return false;
    }


    private void loadArtistSearchResults(ArtistsPager pager) {
        mResults.clear();
        Pager<Artist> artists = pager.artists;
        for (Artist artist : artists.items) {
            String imageUrl = null;
            if (artist.images != null && !artist.images.isEmpty()) {
                SpotifyImageHandler imageHandler = new SpotifyImageHandler(artist.images);
                imageUrl = imageHandler.getArtistThumbnailUrl();
            }
            ArtistSearchResult result = new ArtistSearchResult(
                    artist.name,
                    artist.id,
                    imageUrl
            );
            mResults.add(result);
        }
        mAdapter.notifyDataSetChanged();
        mRecyclerView.scrollToPosition(0);
    }

    private class ArtistSearchResultsAdapter extends RecyclerView.Adapter<ArtistSearchResultViewHolder> {
        //private ArrayList<ArtistSearchResult> results;
        private Context context;

        public ArtistSearchResultsAdapter(Context context) {
            this.context = context;
            // this.results = results;
        }

        @Override
        public ArtistSearchResultViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = getActivity().getLayoutInflater().inflate(R.layout.artist_search_list_item, viewGroup, false);
            return new ArtistSearchResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistSearchResultViewHolder artistSearchResultViewHolder, int i) {
            ArtistSearchResult result = mResults.get(i);
            String artistName = result.getArtistName();
            String imageUrl = result.getImageUrl();

            artistSearchResultViewHolder.artistName.setText(artistName);
            artistSearchResultViewHolder.itemView.setTag(result.getSpotifyId());

            if (imageUrl != null) {
                Picasso.with(context)
                        .load(imageUrl)
                        .fit()
                        .into(artistSearchResultViewHolder.artistImage);
            }

        }

        @Override
        public int getItemCount() {
            return mResults.size();
        }
    }

    private class ArtistSearchResultViewHolder extends RecyclerView.ViewHolder {
        private ImageView artistImage;
        private TextView artistName;

        public ArtistSearchResultViewHolder(View itemView) {
            super(itemView);
            artistImage = (ImageView)itemView.findViewById(R.id.artist_list_item_imageview);
            artistName = (TextView)itemView.findViewById(R.id.artist_list_item_artist_name);
            itemView.setOnClickListener((View.OnClickListener) getActivity());
        }
    }

    private class SpotifyArtistSearchTask extends AsyncTask<String, Void, ArtistsPager> {
        private String mSearchTerm;

        @Override
        protected ArtistsPager doInBackground(String... params) {
            Log.d(TAG, "Running search with search term " + params[0]);
            mSearchTerm = params[0];
            if (mSearchTerm != null && !mSearchTerm.isEmpty()) {
                SpotifyApi spotifyApi = new SpotifyApi();
                SpotifyService spotify = spotifyApi.getService();
                ArtistsPager results = spotify.searchArtists(mSearchTerm);
                return results;
            } else {
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArtistsPager artistsPager) {
            if (artistsPager == null || artistsPager.artists.items.isEmpty()) {
                Context context = getActivity();
                Toast.makeText(context, "The artist was not found", Toast.LENGTH_SHORT).show();
            } else {
                loadArtistSearchResults(artistsPager);
            }
        }
    }
}
