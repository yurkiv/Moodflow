package tk.moodflow.android.ui.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.moodflow.android.R;
import tk.moodflow.android.model.SoundItem;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class SoundItemAdapter extends RecyclerView.Adapter<SoundItemAdapter.ViewHolder> {
    private List<SoundItem> soundItems;
    private List<SoundItem> filteredSoundItems;
    private Context context;

    public SoundItemAdapter(Context context, List<SoundItem> soundItems) {
        this.context = context;
        this.soundItems = soundItems;
        this.filteredSoundItems =soundItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.sound_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return filteredSoundItems.size();
    }

    @Override
    public void onBindViewHolder(final SoundItemAdapter.ViewHolder holder, int position) {
        SoundItem item= filteredSoundItems.get(position);
        holder.tvSoundItemName.setText(item.getName());
        holder.ivSoundItem.setImageDrawable(ContextCompat.getDrawable(context, item.getImage()));
        holder.tvSoundItemName.setBackgroundColor(item.getPaletteColor());
    }

    public void flushFilter(){
        filteredSoundItems=new ArrayList<>();
        filteredSoundItems.addAll(soundItems);
        notifyDataSetChanged();
    }

    public void setFilter(String queryText) {
        String filterPattern = queryText.toString().toLowerCase().trim();
        filteredSoundItems = new ArrayList<>();
        for (SoundItem item: soundItems) {
            if (item.getName().toLowerCase().contains(filterPattern))
                filteredSoundItems.add(item);
        }
        notifyDataSetChanged();
    }

//    @Override
//    public Filter getFilter() {
//        return new ItemFilter(this, soundItems);
////        return new Filter() {
////            @Override
////            protected FilterResults performFiltering(CharSequence constraint) {
////                final FilterResults filterResults=new FilterResults();
////                final List<SoundItem> results=new ArrayList<>();
////                if (filteredSoundItems==null){
////                    filteredSoundItems=soundItems;
////                }
////                if (constraint!=null){
////                    if (filteredSoundItems!=null & filteredSoundItems.size()>0){
////                        for (final SoundItem item:filteredSoundItems){
////                            if (item.getName().toLowerCase().contains(constraint.toString())){
////                                results.add(item);
////                            }
////                        }
////                    }
////                    filterResults.values=results;
////                }
////                return filterResults;
////            }
////
////            @Override
////            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
////                filteredSoundItems= (List<SoundItem>) filterResults.values;
////                notifyDataSetChanged();
////            }
////        };
//    }

    private static class ItemFilter extends Filter{

        private final SoundItemAdapter adapter;
        private final List<SoundItem> originalList;
        private final List<SoundItem> filteredList;

        public ItemFilter(SoundItemAdapter adapter, List<SoundItem> originalList) {
            this.adapter = adapter;
            this.originalList = new LinkedList<>(originalList);
            this.filteredList = new ArrayList<>();
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            filteredList.clear();
            final FilterResults results = new FilterResults();

            if (constraint.length() == 0) {
                filteredList.addAll(originalList);
            } else {
                final String filterPattern = constraint.toString().toLowerCase().trim();

                for (final SoundItem item : originalList) {
                    if (item.getName().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }
            }
            results.values = filteredList;
            results.count = filteredList.size();
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            adapter.filteredSoundItems.clear();
            adapter.filteredSoundItems.addAll((ArrayList<SoundItem>) filterResults.values);
            adapter.notifyDataSetChanged();
        }
    }

    public final static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @InjectView(R.id.tvSoundItemName) public TextView tvSoundItemName;
        @InjectView(R.id.ivSoundItem) public SelectableRoundedImageView ivSoundItem;
        @InjectView(R.id.card_view) public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        @Override
        public void onClick(final View view) {

        }
    }

}
