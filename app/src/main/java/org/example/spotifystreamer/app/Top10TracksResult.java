/*
 * Copyright (C) 2015 Tom Belunis
 */

package org.example.spotifystreamer.app;

import android.os.Parcel;
import android.os.Parcelable;

public class Top10TracksResult implements Parcelable {
    private String mImageUrl;
    private String mAlbumTitle;
    private String mTrackTitle;
    private String mPreviewUrl;

    public Top10TracksResult(String imageUrl, String albumTitle, String trackTitle, String previewUrl) {
        mImageUrl = imageUrl;
        mAlbumTitle = albumTitle;
        mTrackTitle = trackTitle;
        mPreviewUrl = previewUrl;
    }

    private Top10TracksResult(Parcel parcel) {
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

    public static final Creator<Top10TracksResult> CREATOR;

    static {
        CREATOR = new Creator<Top10TracksResult>() {
            @Override
            public Top10TracksResult createFromParcel(Parcel source) {
                return new Top10TracksResult(source);
            }

            @Override
            public Top10TracksResult[] newArray(int size) {
                return new Top10TracksResult[size];
            }
        };
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
