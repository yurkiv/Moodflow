package tk.genresbox.android.api;

import retrofit.RestAdapter;

/**
 * Created by yurkiv on 22.05.2015.
 */
public class SoundcloudApi {
    private static final String API_URL = "http://api.soundcloud.com";

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .build();
    private static final SoundcloudService SERVICE = REST_ADAPTER.create(SoundcloudService.class);

    public static SoundcloudService getService(){
        return SERVICE;
    }
}
