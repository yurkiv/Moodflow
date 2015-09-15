package tk.moodflow.android.api;

import retrofit.RestAdapter;

/**
 * Created by yurkiv on 22.05.2015.
 */
public class CmdFm {
    private static final String API_URL = "http://cmdto.com/fm/api";

    private static final RestAdapter REST_ADAPTER = new RestAdapter.Builder()
            .setEndpoint(API_URL)
            .build();
    private static final CmdFmService SERVICE = REST_ADAPTER.create(CmdFmService.class);

    public static CmdFmService getService(){
        return SERVICE;
    }
}
