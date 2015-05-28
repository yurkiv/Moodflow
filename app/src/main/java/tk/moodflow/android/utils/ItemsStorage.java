package tk.moodflow.android.utils;

import java.util.ArrayList;
import java.util.List;

import tk.moodflow.android.R;
import tk.moodflow.android.model.SoundItem;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class ItemsStorage {
    private List<SoundItem> moods;
    private List<SoundItem> genres;

    public ItemsStorage() {
        moods=new ArrayList<>();
        genres=new ArrayList<>();
        generateGenres();
        generateMoods();
    }

    public List<SoundItem> getMoods() {
        return moods;
    }

    public List<SoundItem> getGenres() {
        return genres;
    }

    private void generateGenres(){
        SoundItem item=new SoundItem("Alternative Rock", R.drawable.alternative_rock);
        genres.add(item);
        item=new SoundItem("Ambient", R.drawable.ambient);
        genres.add(item);
        item=new SoundItem("Classical", R.drawable.classical);
        genres.add(item);
        item=new SoundItem("Country", R.drawable.country);
        genres.add(item);
        item=new SoundItem("Dance", R.drawable.dance);
        genres.add(item);
        item=new SoundItem("Disco", R.drawable.disco);
        genres.add(item);
        item=new SoundItem("Drum & Bass", R.drawable.dnb);
        genres.add(item);
        item=new SoundItem("Dubstep", R.drawable.dubstep);
        genres.add(item);
        item=new SoundItem("Electronic", R.drawable.electronic);
        genres.add(item);
        item=new SoundItem("Folk", R.drawable.folk);
        genres.add(item);
        item=new SoundItem("Hip Hop & Rap", R.drawable.hip_hop_rap);
        genres.add(item);
        item=new SoundItem("House", R.drawable.house);
        genres.add(item);
        item=new SoundItem("Indie", R.drawable.indie);
        genres.add(item);
        item=new SoundItem("Jazz & Blues", R.drawable.jazz);
        genres.add(item);
        item=new SoundItem("Latin", R.drawable.latin);
        genres.add(item);
        item=new SoundItem("Metal", R.drawable.metal);
        genres.add(item);
        item=new SoundItem("Piano", R.drawable.piano);
        genres.add(item);
        item=new SoundItem("Pop", R.drawable.pop);
        genres.add(item);
        item=new SoundItem("R&B & Soul", R.drawable.rnb);
        genres.add(item);
        item=new SoundItem("Reggae", R.drawable.reggae);
        genres.add(item);
        item=new SoundItem("Rock", R.drawable.rock);
        genres.add(item);
        item=new SoundItem("Trance", R.drawable.trance);
        genres.add(item);
    }

    private void generateMoods(){
        SoundItem item=new SoundItem("Acoustic", R.drawable.acoustic);
        moods.add(item);
        item=new SoundItem("Adventure", R.drawable.adventure);
        moods.add(item);
        item=new SoundItem("Afrobeats", R.drawable.afrobeats);
        moods.add(item);
        item=new SoundItem("Aggressive", R.drawable.aggressive);
        moods.add(item);
        item=new SoundItem("Alone", R.drawable.alone);
        moods.add(item);
        item=new SoundItem("Angry", R.drawable.angry);
        moods.add(item);
        item=new SoundItem("Beach", R.drawable.beach);
        moods.add(item);
        item=new SoundItem("Beautiful", R.drawable.beautiful);
        moods.add(item);
        item=new SoundItem("Bicycle", R.drawable.bicycle);
        moods.add(item);
        item=new SoundItem("Calm", R.drawable.calm);
        moods.add(item);
        item=new SoundItem("Cello", R.drawable.cello);
        moods.add(item);
        item=new SoundItem("Chillout", R.drawable.chillout);
        moods.add(item);
        item=new SoundItem("Cinema", R.drawable.cinema);
        moods.add(item);
        item=new SoundItem("Cloudy", R.drawable.cloudy);
        moods.add(item);
        item=new SoundItem("Coding", R.drawable.coding);
        moods.add(item);
        item=new SoundItem("Cooking time", R.drawable.cooking_time);
        moods.add(item);
        item=new SoundItem("Cool", R.drawable.cool);
        moods.add(item);
        item=new SoundItem("Creative", R.drawable.creative);
        moods.add(item);
        item=new SoundItem("Depressed", R.drawable.depressed);
        moods.add(item);
        item=new SoundItem("Digital", R.drawable.digital);
        moods.add(item);
        item=new SoundItem("Dreamy", R.drawable.dreamy);
        moods.add(item);
        item=new SoundItem("Drinking Coffee", R.drawable.drinking_coffe);
        moods.add(item);
        item=new SoundItem("Driving", R.drawable.driving);
        moods.add(item);
        item=new SoundItem("Eating", R.drawable.eating);
        moods.add(item);
        item=new SoundItem("Elegant", R.drawable.elegant);
        moods.add(item);
        item=new SoundItem("Energetic", R.drawable.energetic);
        moods.add(item);
        item=new SoundItem("Epic", R.drawable.epic);
        moods.add(item);
        item=new SoundItem("Female", R.drawable.female);
        moods.add(item);
        item=new SoundItem("Flying Away", R.drawable.flying_away);
        moods.add(item);
        item=new SoundItem("Funny", R.drawable.funny);
        moods.add(item);
        item=new SoundItem("Gaming", R.drawable.gaming);
        moods.add(item);
        item=new SoundItem("Groovy", R.drawable.groovy);
        moods.add(item);
        item=new SoundItem("Halloween", R.drawable.halloween);
        moods.add(item);
        item=new SoundItem("Happy", R.drawable.happy);
        moods.add(item);
        item=new SoundItem("High", R.drawable.high);
        moods.add(item);
        item=new SoundItem("Homework", R.drawable.homework);
        moods.add(item);
        item=new SoundItem("Hopeful", R.drawable.hopeful);
        moods.add(item);
        item=new SoundItem("Iceland", R.drawable.iceland);
        moods.add(item);
        item=new SoundItem("In Love", R.drawable.in_love);
        moods.add(item);
        item=new SoundItem("Instrumental", R.drawable.instrumental);
        moods.add(item);
        item=new SoundItem("Lonely", R.drawable.lonely);
        moods.add(item);
        item=new SoundItem("Magic", R.drawable.magic);
        moods.add(item);
        item=new SoundItem("Melancholy", R.drawable.melancholy);
        moods.add(item);
        item=new SoundItem("Mellow", R.drawable.mellow);
        moods.add(item);
        item=new SoundItem("Mod", R.drawable.mod);
        moods.add(item);
        item=new SoundItem("Optimistic", R.drawable.optimistic);
        moods.add(item);
        item=new SoundItem("Painting", R.drawable.painting);
        moods.add(item);
        item=new SoundItem("Peaceful", R.drawable.peaceful);
        moods.add(item);
        item=new SoundItem("Psychedelic", R.drawable.psychedelic);
        moods.add(item);
        item=new SoundItem("Reading", R.drawable.reading);
        moods.add(item);
        item=new SoundItem("Relax", R.drawable.relax);
        moods.add(item);
        item=new SoundItem("Romantic", R.drawable.romantic);
        moods.add(item);
        item=new SoundItem("Running", R.drawable.running);
        moods.add(item);
        item=new SoundItem("Sad", R.drawable.sad);
        moods.add(item);
        item=new SoundItem("Shower", R.drawable.shower);
        moods.add(item);
        item=new SoundItem("Sleeping", R.drawable.sleeping);
        moods.add(item);
        item=new SoundItem("Spring", R.drawable.spring);
        moods.add(item);
        item=new SoundItem("Studying", R.drawable.studying);
        moods.add(item);
        item=new SoundItem("Summer", R.drawable.summer);
        moods.add(item);
        item=new SoundItem("Sunny day", R.drawable.sunny_day);
        moods.add(item);
        item=new SoundItem("Sunrise", R.drawable.sunrise);
        moods.add(item);
        item=new SoundItem("Sunset", R.drawable.sunset);
        moods.add(item);
        item=new SoundItem("Sweet", R.drawable.sweet);
        moods.add(item);
        item=new SoundItem("Travel", R.drawable.travel);
        moods.add(item);
        item=new SoundItem("Tropical", R.drawable.tropical);
        moods.add(item);
        item=new SoundItem("Urban", R.drawable.urban);
        moods.add(item);
        item=new SoundItem("Vintage", R.drawable.vintage);
        moods.add(item);
        item=new SoundItem("Walking", R.drawable.walking);
        moods.add(item);
        item=new SoundItem("Winter", R.drawable.winter);
        moods.add(item);
        item=new SoundItem("Working", R.drawable.working);
        moods.add(item);
        item=new SoundItem("Writing", R.drawable.writing);
        moods.add(item);
    }
}
