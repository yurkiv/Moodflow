package tk.moodflow.android.api;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Query;
import tk.moodflow.android.model.Track;

/**
 * Created by yurkiv on 22.05.2015.
 */
public interface CmdFmService {

    public static final String CLIENT_ID="9c4981270863f59719aa8e62f7f4ccdd";

    @GET("/tracks/search")
    public void getByGenre(@Query("genre") String genre, Callback<List<Track>> cb);
}
