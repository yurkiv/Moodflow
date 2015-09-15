package tk.genresbox.android.api;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import tk.genresbox.android.model.Track;

/**
 * Created by yurkiv on 22.05.2015.
 */
public interface SoundcloudService {

    public static final String CLIENT_ID="9c4981270863f59719aa8e62f7f4ccdd";

    @GET("/tracks")
    public void getByGenre(@Query("genres") String genre, Callback<List<Track>> cb);
}
