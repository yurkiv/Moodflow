package tk.moodflow.android.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.github.glomadrian.materialanimatedswitch.MaterialAnimatedSwitch;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.moodflow.android.R;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {

    private static final String TAG="GenresAdapter";
    private List<String> genres;
    private List<String> filteredGenres;
    private Set<String> favGenres;
    private SharedPreferences preferences;
    private static OnItemClickListener onItemClickListener;

    public GenresAdapter(Context context, List<String> genres) {
        this.genres = genres;
        this.filteredGenres = genres;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        favGenres=preferences.getStringSet("fav", new HashSet<String>());
    }

    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        GenresAdapter.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return filteredGenres.size();
    }

    @Override
    public void onBindViewHolder(final GenresAdapter.ViewHolder holder, int position) {
        final String genre = filteredGenres.get(position);
        final boolean isFav=favGenres.contains(genre);
        holder.tvTitle.setText(genre);
        TextDrawable drawable = TextDrawable.builder()
                .buildRound(genre.substring(0,1).toUpperCase(), ColorGenerator.MATERIAL.getRandomColor());
        holder.ivIcon.setImageDrawable(drawable);

        holder.pinFav.post(new Runnable() {
            @Override
            public void run() {
                if (isFav) {
                    if (!holder.pinFav.isChecked()) {
                        holder.pinFav.toggle();
                    }
                    Log.i(TAG, "contains genre:" + genre);
                } else {
                    if (holder.pinFav.isChecked()) {
                        holder.pinFav.toggle();
                        Log.i(TAG, "not contains genre:" + genre);
                    }
                }
            }
        });

        holder.pinFav.setOnCheckedChangeListener(new MaterialAnimatedSwitch.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(boolean isChecked) {
                if (isChecked){
                    favGenres.add(genre);
                    Log.i(TAG, "add genre:" + genre);
                } else {
                    favGenres.remove(genre);
                    Log.i(TAG, "remove genre:" + genre);
                }
                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet("fav", favGenres);
                editor.commit();
                Log.i(TAG, "put genres:" + favGenres);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void flushFilter(){
        filteredGenres =new ArrayList<>();
        filteredGenres.addAll(genres);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {
        String filterPattern = queryText.toString().toLowerCase().trim();
        filteredGenres = new ArrayList<>();
        for (String item: genres) {
            if (item.toLowerCase().contains(filterPattern))
                filteredGenres.add(item);
        }
        notifyDataSetChanged();
    }

    public List<String> getFilteredGenres() {
        return filteredGenres;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.tvTitle) public TextView tvTitle;
        @InjectView(R.id.ivIcon) public ImageView ivIcon;
        @InjectView(R.id.pinFav) public MaterialAnimatedSwitch pinFav;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            onItemClickListener.onItemClick(getLayoutPosition(), view);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View v);
    }

}
