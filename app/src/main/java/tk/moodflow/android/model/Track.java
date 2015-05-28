package tk.moodflow.android.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by yurkiv on 22.05.2015.
 */
public class Track {
    @SerializedName("title")
    private String mTitle;

    @SerializedName("stream_url")
    private String mStreamURL;

    @SerializedName("id")
    private int mID;

    @SerializedName("artwork_url")
    private String artworkURL;

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getStreamURL() {
        return mStreamURL;
    }

    public String getArtworkURL() {
        return artworkURL;
    }

    public String getAvatarURL(){
        String avatarURL = getAvatarURL();
        if (avatarURL != null){
            return artworkURL.replace("large","tiny");
        }
        return avatarURL;
    }
}
