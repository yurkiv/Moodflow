package tk.moodflow.android.api;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import tk.moodflow.android.model.Track;

/**
 * Created by yurkiv on 22.05.2015.
 */
public interface SoundCloudService {

    static final String CLIENT_ID = "954240dcf4b7c0a46e59369a69db7215";

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void searchSongs(@Query("q") String query, Callback<List<Track>> cb);

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void getRecentSongs(@Query("created_at[from]") String date, Callback<List<Track>> cb);

    public void songsAfter(@Query("created_at[to]") String date, Callback<List<Track>> cb);
    public void bpmFrom(@Query("bpm[from]") String date, Callback<List<Track>> cb);

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void getByGenre(@Query("genre") String genre, Callback<List<Track>> cb);

    @GET("/tracks?client_id=" + CLIENT_ID)
    public void getByTag(@Query("tag") String tag, Callback<List<Track>> cb);
}
