package org.example.spotifystreamer.app;

import android.app.FragmentTransaction;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.*;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity implements TextView.OnEditorActionListener {
    private final String TAG = this.getClass().getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText searchQueryText = (EditText) findViewById(R.id.search_text);
        searchQueryText.setOnEditorActionListener(this);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            Log.d(TAG, "The search text is " + v.getText());
            return false;
        }
        return false;
    }

    private class ArtistSearchResultsAdapter extends RecyclerView.Adapter<ArtistSearchResultViewHolder> {
        private ArrayList<ArtistSearchResult> results;
        private Context context;

        public ArtistSearchResultsAdapter(Context context, ArrayList<ArtistSearchResult> results) {
            this.context = context;
            this.results = results;
        }

        @Override
        public ArtistSearchResultViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = getLayoutInflater().inflate(R.layout.artist_search_list_item, viewGroup, false);
            return new ArtistSearchResultViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ArtistSearchResultViewHolder artistSearchResultViewHolder, int i) {
            ArtistSearchResult result = results.get(i);
            String artistName = result.getArtistName();
            String imageUrl = result.getImageUrl();

            artistSearchResultViewHolder.artistName.setText(artistName);

            if (imageUrl != null) {
                Picasso.with(context).load(imageUrl).into(artistSearchResultViewHolder.artistImage);
            }

        }

        @Override
        public int getItemCount() {
            return results.size();
        }
    }

    private class ArtistSearchResultViewHolder extends RecyclerView.ViewHolder {
        private ImageView artistImage;
        private TextView artistName;

        public ArtistSearchResultViewHolder(View itemView) {
            super(itemView);
            artistImage = (ImageView)itemView.findViewById(R.id.artist_list_item_imageview);
            artistName = (TextView)itemView.findViewById(R.id.artist_list_item_artist_name);
        }
    }
}
