package org.example.spotifystreamer.app;
import kaaes.spotify.webapi.android.models.Image;

import java.util.List;

public class SpotifyImageHandler {
    private List<Image> mImages;

    public SpotifyImageHandler(List<Image> images) {
        mImages = images;
    }

    public String getArtistThumbnailUrl() {
        String imageUrl = null;

        if (mImages != null && !mImages.isEmpty()) {
            Image image = mImages.get(mImages.size() - 1);
            imageUrl = image.url;
        }
        return imageUrl;
    }

    public String getImageForSize(int pixels) {
        String imageUrl = null;
        int difference = Integer.MAX_VALUE;

        if (mImages != null) {
            for (Image image : mImages) {
                if (imageUrl == null) {
                    imageUrl = image.url;
                    difference = image.width - pixels;
                } else {
                    int newDifference = image.width - pixels;
                    if (newDifference < difference) {
                        imageUrl = image.url;
                        difference = newDifference;
                    }
                }

            }
        }
        return imageUrl;
    }
}
