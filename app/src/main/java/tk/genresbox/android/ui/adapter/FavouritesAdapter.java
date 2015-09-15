package tk.genresbox.android.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.genresbox.android.R;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class FavouritesAdapter extends RecyclerView.Adapter<FavouritesAdapter.ViewHolder> {

    private static final String TAG="FavouritesAdapter";
    private Context context;
    private SharedPreferences preferences;
    private List<String> genres;
    private static OnItemClickListener onItemClickListener;

    public FavouritesAdapter(Context context) {
        this.context=context;
        this.preferences = PreferenceManager.getDefaultSharedPreferences(context);
        this.genres=new ArrayList<>(preferences.getStringSet("fav", new HashSet<String>()));
    }

    public static void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        FavouritesAdapter.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return genres.size();
    }

    @Override
    public void onBindViewHolder(final FavouritesAdapter.ViewHolder holder, final int position) {
        final String genre = genres.get(position);
        holder.tvTitle.setText(genre);

        final GradientDrawable mDrawable = (GradientDrawable) holder.ivIcon.getBackground();
        mDrawable.setColor(context.getResources().getColor(R.color.accent));

        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawable.setColor(context.getResources().getColor(R.color.divider));
                genres.remove(genre);
                Log.i(TAG, "remove genre:" + genre);
                notifyItemRemoved(position);

                SharedPreferences.Editor editor = preferences.edit();
                editor.putStringSet("fav", new HashSet<String>(genres));
                editor.commit();
                Log.i(TAG, "put genres:" + genres);
            }
        });
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    public void setFilter(String queryText) {
        this.genres=new ArrayList<>(preferences.getStringSet("fav", new HashSet<String>()));
        String filterPattern = queryText.toString().toLowerCase().trim();
        List<String> filteredGenres = new ArrayList<>();
        for (String item: genres) {
            if (item.toLowerCase().contains(filterPattern))
                filteredGenres.add(item);
        }
        genres=filteredGenres;
        notifyDataSetChanged();
    }

    public void updateGenres() {
        this.genres=new ArrayList<>(preferences.getStringSet("fav", new HashSet<String>()));
        notifyDataSetChanged();
    }

    public List<String> getGenres() {
        return genres;
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.tvTitle) public TextView tvTitle;
        @InjectView(R.id.ivIcon) public ImageView ivIcon;

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
