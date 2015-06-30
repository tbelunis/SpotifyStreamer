package org.example.spotifystreamer.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Top10TrackResult implements Parcelable {
    private String mImageUrl;
    private String mAlbumTitle;
    private String mTrackTitle;
    private String mPreviewUrl;

    public Top10TrackResult(String imageUrl, String albumTitle, String trackTitle, String previewUrl) {
        mImageUrl = imageUrl;
        mAlbumTitle = albumTitle;
        mTrackTitle = trackTitle;
        mPreviewUrl = previewUrl;
    }

    private Top10TrackResult(Parcel parcel) {
        mImageUrl = parcel.readString();
        mAlbumTitle = parcel.readString();
        mTrackTitle = parcel.readString();
        mPreviewUrl = parcel.readString();
    }

    @Override
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(mImageUrl);
        out.writeString(mAlbumTitle);
        out.writeString(mTrackTitle);
        out.writeString(mPreviewUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public String getTrackTitle() {
        return mTrackTitle;
    }

    public String getAlbumTitle() {
        return mAlbumTitle;
    }

    public String getPreviewUrl() {
        return mPreviewUrl;
    }


}
