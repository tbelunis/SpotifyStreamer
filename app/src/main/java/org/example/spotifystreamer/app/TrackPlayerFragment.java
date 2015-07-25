package org.example.spotifystreamer.app;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackPlayerFragment extends DialogFragment implements View.OnClickListener {
    private final String TAG = getClass().getSimpleName();

    private ArrayList<Top10TracksResult> mTracks;
    private int mCurrentPosition;
    private boolean mIsPlaying;
    private ImageButton mPlayButton;
    private ImageButton mPauseButton;
    private ImageButton mPreviousTrack;
    private ImageButton mNextTrack;
    private TextView mArtistName;
    private TextView mAlbumName;
    private TextView mTrackTitle;
    private ImageView mTrackImage;
    private TrackPreviewService mTrackPreviewService;
    private Intent mPlayerIntent;
    private boolean mIsPlayerBound = false;

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
        mPauseButton = (ImageButton) view.findViewById(R.id.track_button_pause);
        mPauseButton.setOnClickListener(this);
        mPreviousTrack = (ImageButton) view.findViewById(R.id.track_button_prev);
        mPreviousTrack.setOnClickListener(this);
        mNextTrack = (ImageButton) view.findViewById(R.id.track_button_next);
        mNextTrack.setOnClickListener(this);

        setView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
        if (mPlayerIntent == null) {
            mPlayerIntent = new Intent(getActivity(), TrackPreviewService.class);
            getActivity().bindService(mPlayerIntent, playerConnection, Context.BIND_AUTO_CREATE);
            getActivity().startService(mPlayerIntent);
        }
    }

    private ServiceConnection playerConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            TrackPreviewService.TrackPreviewBinder binder = (TrackPreviewService.TrackPreviewBinder) service;
            mTrackPreviewService = binder.getService();
            mIsPlayerBound = true;
            Log.d(TAG, "onServiceConnected finished");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mIsPlayerBound = false;
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.track_button_play:
                playTrack();
                break;
            case R.id.track_button_pause:
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

    private void playTrack() {
//        if (mPlayerIntent == null) {
//            mPlayerIntent = new Intent(getActivity(), TrackPreviewService.class);
//            mPlayerIntent.putExtra(Constants.TRACK_TO_PLAY, mCurrentPosition);
//            mPlayerIntent.putParcelableArrayListExtra(Constants.TRACKS, mTracks);
//            getActivity().startService(mPlayerIntent);
//            mTrackPreviewService.playSong(mTracks.get(mCurrentPosition).getPreviewUrl());
//        }
        Log.d(TAG, "playTrack");
        mTrackPreviewService.playSong(mTracks.get(mCurrentPosition).getPreviewUrl());
        //mPlayButton.setVisibility(View.INVISIBLE);

    }

    private void pauseTrack() {

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
