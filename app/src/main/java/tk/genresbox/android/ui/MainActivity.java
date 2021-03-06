package tk.genresbox.android.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
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
import android.widget.TextView;

import com.github.rahatarmanahmed.cpv.CircularProgressView;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import tk.genresbox.android.R;
import tk.genresbox.android.api.SoundcloudApi;
import tk.genresbox.android.api.SoundcloudService;
import tk.genresbox.android.model.Track;
import tk.genresbox.android.ui.fragments.FavouritesFragment;
import tk.genresbox.android.ui.fragments.GenresFragment;
import tk.genresbox.android.ui.view.PlayPauseView;
import tk.genresbox.android.utils.AudioFocusListener;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class MainActivity extends AppCompatActivity implements FavouritesFragment.OnFavouritesSelectionListener,
        GenresFragment.OnGenreSelectionListener, MediaPlayer.OnCompletionListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private List<String> genres;
    private List<Track> tracks;
    private MediaPlayer mp;
    private NotificationManager notificationManager;
    private AudioManager audioManager;
    private AudioFocusListener audioFocusListener;
    private Track selectedTrack;

    @InjectView(R.id.toolbar) protected Toolbar toolbar;
    @InjectView(R.id.tabLayout) protected TabLayout tabLayout;
    @InjectView(R.id.viewPager) protected ViewPager viewPager;
    @InjectView(R.id.play_pause_view) protected PlayPauseView play_pause_view;
    @InjectView(R.id.bt_next_track) protected FloatingActionButton btNextTrack;
    @InjectView(R.id.player_progress_bar) protected CircularProgressView progressBar;
    @InjectView(R.id.tv_title) protected TextView trackTitle;
    @InjectView(R.id.tv_item_name) protected TextView itemName;
    @InjectView(R.id.iv_thumbnail) protected ImageView ivThumbnail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        play_pause_view.setPlay(false);
        play_pause_view.setEnabled(false);

        genres=getGenres();
        tracks = new ArrayList<>();

        if (toolbar!=null){
            setSupportActionBar(toolbar);
        }

        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(onPageChangeListener);

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

    private void loadGenre(final String name){
        toggleProgressBar();
        SoundcloudService soundcloudService = SoundcloudApi.getService();
        soundcloudService.getByGenre(name, new Callback<List<Track>>() {
            @Override
            public void success(List<Track> tracks, Response response) {
                updateTracks(tracks);
                play_pause_view.setEnabled(true);
                itemName.setText(name);
                playRandomSong();
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d(TAG, "Failed call: " + error.toString());
                progressBar.setVisibility(View.INVISIBLE);
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
        selectedTrack = tracks.get(currentSongIndex);
        trackTitle.setText(selectedTrack.getTitle());
        Picasso.with(MainActivity.this).load(selectedTrack.getArtworkURL()).into(ivThumbnail);

        if (mp.isPlaying()){
            mp.stop();
        }
        mp.reset();
        toggleProgressBar();

        try {
            mp.setDataSource(selectedTrack.getStreamURL() + "?client_id=" + SoundcloudService.CLIENT_ID);
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
    public void onGenreSelected(String name) {
        loadGenre(name);
    }

    @Override
    public void onMoodSelected(String name) {
        loadGenre(name);
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        playRandomSong();
    }

    private List<String> getGenres(){
        List<String> genreNames=Arrays.asList(new String[]{"80s", "Acid Jazz", "Acoustic", "Acoustic Rock", "African", "Alternative", "Alternative Rock", "Ambient", "Americana", "Arabic", "Audiobooks", "Avantgarde", "Bachata", "Bhangra", "Blues", "Blues Rock", "Bossa Nova", "Breakbeats", "Business", "Chanson", "Chillout", "Chillstep", "Chiptunes", "Choir", "Classical", "Classical Guitar", "Classic Rock", "Comedy", "Contemporary", "Country", "Cumbia", "Dance", "Dancehall", "Death Metal", "Deep House", "Dirty South", "Disco", "Dream Pop", "Drum & Bass", "Dub", "Dubstep", "Easy Listening", "Electro", "Electro House", "Electronic", "Electronica", "Electronic Pop", "Electronic Rock", "Entertainment", "Folk", "Folk Rock", "Funk", "Glitch", "Gospel", "Grime", "Grindcore", "Grunge", "Hardcore", "Hardcore Techno", "Hard Rock", "Heavy Metal", "Hip-Hop", "House", "Indie", "Indie Pop", "Indie Rock", "Industrial Metal", "Instrumental Rock", "Jazz", "Jazz Funk", "Jazz Fusion", "J-Pop", "K-Pop", "Latin", "Latin Jazz", "Learning", "Lounge", "Mambo", "Metal", "Metalcore", "Middle Eastern", "Minimal", "Minimal Techno", "Modern Jazz", "Moombahton", "News & Politics", "New Wave", "Nu Jazz", "Opera", "Orchestral", "Piano", "Pop", "Post Hardcore", "Post Rock", "Progressive House", "Progressive Metal", "Progressive Rock", "Punk", "Rap", "R&B", "Reggae", "Reggaeton", "Religion & Spirituality", "Riddim", "Rock", "Rock 'n' Roll", "Salsa", "Samba", "Science", "Shoegaze", "Singer / Songwriter", "Smooth Jazz", "Soul", "Sports", "Storytelling", "Synth Pop", "Tech House", "Techno", "Technology", "Thrash Metal", "Trance", "Trap", "Trip-hop", "Turntablism", "World"});
        List<String> genres=new ArrayList<>(genreNames.size());
        for (String s:genreNames){
            genres.add(s);
        }
        return genres;
    }

    private List<String> getTopGenres(){
        List<String> genreNames=Arrays.asList(new String[]{"80s", "Acoustic", "Alternative Rock", "Classical","Country","Electronic","Folk","Hip-Hop","Indie","Jazz","Pop","Reggae","Rock","Soul","Trance"});
        List<String> genres=new ArrayList<>(genreNames.size());
        for (String s:genreNames){
            genres.add(s);
        }
        return genres;
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {
        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return FavouritesFragment.newInstance();
                case 1:
                    return GenresFragment.newInstance(genres);
                case 2:
                    return GenresFragment.newInstance(getTopGenres());
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Favourites";
                case 1:
                    return "Genres";
                case 2:
                    return "Top 15";
                default:
                    return null;
            }
        }
    }

    private ViewPager.OnPageChangeListener onPageChangeListener=new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            switch (position){
                case 0:
                    FavouritesFragment.updateView();
                    break;
                case 1:
                    GenresFragment.updateView();
                    break;
                case 2:
                    GenresFragment.updateView();
                    break;
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
