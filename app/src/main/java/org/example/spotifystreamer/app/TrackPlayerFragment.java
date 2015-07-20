package org.example.spotifystreamer.app;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackPlayerFragment extends DialogFragment implements View.OnClickListener {
    private ArrayList<Top10TracksResult> mTracks;
    private int mCurrentPosition;
    private boolean mIsPlaying;
    private ImageButton mPlayButton;
    private ImageButton mPreviousTrack;
    private ImageButton mNextTrack;
    private TextView mArtistName;
    private TextView mAlbumName;
    private TextView mTrackTitle;
    private ImageView mTrackImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent intent = getActivity().getIntent();
        mTracks = intent.getParcelableArrayListExtra(Constants.TRACKS);
        mCurrentPosition = intent.getIntExtra(Constants.TRACK_TO_PLAY, 0);

        View view = inflater.inflate(R.layout.track_player_fragment, container, false);

        mArtistName = (TextView) view.findViewById(R.id.track_artist_name);
        mAlbumName = (TextView) view.findViewById(R.id.track_album_name);
        mTrackTitle = (TextView) view.findViewById(R.id.track_track_title);
        mTrackImage = (ImageView) view.findViewById(R.id.track_image);
        mPlayButton = (ImageButton) view.findViewById(R.id.track_button_play);
        mPlayButton.setOnClickListener(this);
        mPreviousTrack = (ImageButton) view.findViewById(R.id.track_button_prev);
        mPreviousTrack.setOnClickListener(this);
        mNextTrack = (ImageButton) view.findViewById(R.id.track_button_next);
        mNextTrack.setOnClickListener(this);

        setView();

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.track_button_play:
                break;
            case R.id.track_button_prev:
                moveToPreviousTrack();
                break;
            case R.id.track_button_next:
                moveToNextTrack();
                break;
        }
    }

    private void moveToPreviousTrack() {
        if (mCurrentPosition > 0) {
            mCurrentPosition--;
            setView();
        }
    }

    private void moveToNextTrack() {
        if (mCurrentPosition < mTracks.size()) {
            mCurrentPosition++;
            setView();
        }
    }

    private void setView() {
        Top10TracksResult track = mTracks.get(mCurrentPosition);
        mArtistName.setText(track.getArtistName());
        mAlbumName.setText(track.getAlbumTitle());
        mTrackTitle.setText(track.getTrackTitle());
        Picasso.with(getActivity())
                .load(track.getImageUrl())
                .into(mTrackImage);
        mPreviousTrack.setEnabled(mCurrentPosition > 0);
        mNextTrack.setEnabled(mCurrentPosition < mTracks.size());
    }
}
