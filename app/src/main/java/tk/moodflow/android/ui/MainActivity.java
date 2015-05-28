package tk.moodflow.android.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.melnykov.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import io.karim.MaterialTabs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tk.moodflow.android.R;
import tk.moodflow.android.api.CmdFm;
import tk.moodflow.android.api.CmdFmService;
import tk.moodflow.android.api.SoundCloud;
import tk.moodflow.android.api.SoundCloudService;
import tk.moodflow.android.model.SoundItem;
import tk.moodflow.android.model.Track;
import tk.moodflow.android.ui.fragments.GenresFragment;
import tk.moodflow.android.ui.fragments.MoodsFragment;
import tk.moodflow.android.ui.view.PlayPauseView;
import tk.moodflow.android.utils.AudioFocusListener;
import tk.moodflow.android.utils.ItemsStorage;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class MainActivity extends AppCompatActivity implements MoodsFragment.OnMoodSelectionListener,
        GenresFragment.OnGenreSelectionListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private ItemsStorage itemsStorage;
    private List<SoundItem> genres;
    private List<SoundItem> moods;
    private List<Track> tracks;
    private MediaPlayer mp;
    private NotificationManager notificationManager;
    private AudioManager audioManager;
    private AudioFocusListener audioFocusListener;

    @InjectView(R.id.toolbar) protected Toolbar toolbar;
    @InjectView(R.id.tabHost) protected MaterialTabs tabHost;
    @InjectView(R.id.pager) protected ViewPager pager;
    @InjectView(R.id.play_pause_view) protected PlayPauseView play_pause_view;
    @InjectView(R.id.bt_next_track) protected FloatingActionButton btNextTrack;
    @InjectView(R.id.player_progress_bar) protected ProgressBar progressBar;
    @InjectView(R.id.tv_title) protected TextView trackTitle;
    @InjectView(R.id.tv_item_name) protected TextView itemName;
    @InjectView(R.id.iv_thumbnail) protected ImageView ivThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        itemsStorage=new ItemsStorage();
        genres=itemsStorage.getGenres();
        moods=itemsStorage.getMoods();
        tracks = new ArrayList<>();

        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }
        play_pause_view.setPlay(false);
        progressBar.getIndeterminateDrawable().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabHost.setViewPager(pager);

        mp = new MediaPlayer();
        mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mp.setOnCompletionListener(this);
        mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                toggleSongState();
            }
        });

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        audioManager = (AudioManager) this.getSystemService(Context.AUDIO_SERVICE);
        audioFocusListener = new AudioFocusListener(mp, "PianoFlow");

        play_pause_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSongState();
            }
        });
        btNextTrack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playRandomSong();
            }
        });
    }

    private void loadGenre(final int id){
        toggleProgressBar();
        CmdFmService cmdFmService = CmdFm.getService();
        cmdFmService.getByGenre(genres.get(id).getName(), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
                itemName.setText(genres.get(id).getName());
                playRandomSong();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Failed call: " + error.toString());
            }
        });
    }

    private void loadMood(final int id){
        toggleProgressBar();
        SoundCloudService soundCloudService= SoundCloud.getService();
        soundCloudService.getByTag(moods.get(id).getName(), new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
                itemName.setText(moods.get(id).getName());
                playRandomSong();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Failed call: " + error.toString());
            }
        });
    }

    private void updateTracks(List<Track> tracks) {
        this.tracks.clear();
        this.tracks.addAll((tracks));
    }

    private void toggleSongState() {
        if (mp.isPlaying()){
            mp.pause();
            play_pause_view.setPlay(false);
//            notificatePlayingStatus(true);
        }else{
            mp.start();
            toggleProgressBar();
            play_pause_view.setPlay(true);
//            notificatePlayingStatus(false);
        }
    }

    private void toggleProgressBar() {
        if (mp.isPlaying()){
            progressBar.setVisibility(View.INVISIBLE);
            play_pause_view.setClickable(true);
        }else{
            progressBar.setVisibility(View.VISIBLE);
            play_pause_view.setClickable(false);
        }
    }

    private void playRandomSong(){
        Random rand = new Random();
        int currentSongIndex = rand.nextInt((tracks.size() - 1) - 0 + 1) + 0;
        Track selectedTrack = tracks.get(currentSongIndex);
        trackTitle.setText(selectedTrack.getTitle());
        Picasso.with(MainActivity.this).load(selectedTrack.getArtworkURL()).into(ivThumbnail);

        if (mp.isPlaying()){
            mp.stop();
        }
        mp.reset();
        toggleProgressBar();

        try {
            mp.setDataSource(selectedTrack.getStreamURL() + "?client_id=" + SoundCloudService.CLIENT_ID);
            mp.prepareAsync();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    private void notificatePlayingStatus(boolean isPaused) {
//        int icon=R.drawable.ic_play_not;
//        if (isPaused) {
//            icon=R.drawable.ic_pause_not;
//        }
//        Intent notificationIntent = new Intent(this, MainActivity.class);
//        NotificationCompat.Builder nb = new NotificationCompat.Builder(this)
//                .setSmallIcon(icon)
//                .setColor(getResources().getColor(R.color.primary_color))
//                .setContentText("Music to quiet your world")
//                .setContentTitle("PianoFlow")
//                .setContentIntent(PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT));
//
//        Notification notification = nb.build();
//        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
//        notificationManager.notify(1, notification);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mp != null){
            if(mp.isPlaying()){
                mp.stop();
            }
            mp.release();
            mp = null;
            notificationManager.cancel(1);
        }
        if (audioFocusListener != null){
            audioManager.abandonAudioFocus(audioFocusListener);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.search_view) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onGenreSelected(int id) {
        loadGenre(id);
    }

    @Override
    public void onMoodSelected(int id) {
        loadMood(id);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playRandomSong();
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return GenresFragment.newInstance(genres);
                case 1:
                    return MoodsFragment.newInstance(moods);
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Genres";
                case 1:
                    return "Moods";
                default:
                    return null;
            }
        }
    }
}
