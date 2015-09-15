package tk.genresbox.android.ui.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.genresbox.android.R;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {

    private static final String TAG="GenresAdapter";
    private Context context;
    private List<String> genres;
    private List<String> filteredGenres;
    private Set<String> favGenres;
    private SharedPreferences preferences;
    private static OnItemClickListener onItemClickListener;

    private static SparseBooleanArray selectedItems;

    public GenresAdapter(Context context, List<String> genres) {
        this.context=context;
        this.genres = genres;
        this.filteredGenres = genres;

        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        favGenres=preferences.getStringSet("fav", new HashSet<String>());
        selectedItems=new SparseBooleanArray();
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
        holder.tvTitle.setText(genre);

        if (selectedItems.get(position, false)){
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.primary));
            holder.ivIndicator.setVisibility(View.VISIBLE);
        } else {
            holder.tvTitle.setTextColor(context.getResources().getColor(R.color.primary_text));
            holder.ivIndicator.setVisibility(View.GONE);
        }

        final GradientDrawable mDrawable = (GradientDrawable) holder.ivIcon.getBackground();
        if (favGenres.contains(genre)){
            mDrawable.setColor(context.getResources().getColor(R.color.accent));
        } else {
            mDrawable.setColor(context.getResources().getColor(R.color.divider));
        }

        holder.ivIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (favGenres.contains(genre)){
                    mDrawable.setColor(context.getResources().getColor(R.color.divider));
                    favGenres.remove(genre);
                    Log.i(TAG, "remove genre:" + genre);

                } else {
                    mDrawable.setColor(context.getResources().getColor(R.color.accent));
                    favGenres.add(genre);
                    Log.i(TAG, "add genre:" + genre);
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

    public void updateGenres() {
        this.favGenres=preferences.getStringSet("fav", new HashSet<String>());
        notifyDataSetChanged();
    }

    public final class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.tvTitle) public TextView tvTitle;
        @InjectView(R.id.ivIcon) public ImageView ivIcon;
        @InjectView(R.id.ivIndicator) public ImageView ivIndicator;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View view) {
            onItemClickListener.onItemClick(getLayoutPosition(), view);
            int pos=getLayoutPosition();
            selectedItems.clear();
            notifyDataSetChanged();
            selectedItems.put(pos, true);
            tvTitle.setTextColor(context.getResources().getColor(R.color.primary));
            ivIndicator.setVisibility(View.VISIBLE);
        }
    }

    public interface OnItemClickListener {
        public void onItemClick(int position, View v);
    }

}
