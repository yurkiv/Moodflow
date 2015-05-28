package tk.moodflow.android.ui.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;

import com.joooonho.SelectableRoundedImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.moodflow.android.R;
import tk.moodflow.android.model.SoundItem;

/**
 * Created by yurkiv on 21.05.2015.
 */
public class SoundItemAdapter extends RecyclerView.Adapter<SoundItemAdapter.ViewHolder> {

    private static final int MAX_PHOTO_ANIMATION_DELAY = 0;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private List<SoundItem> soundItems;
    private List<SoundItem> filteredSoundItems;
    private Context context;

    private boolean lockedAnimations = false;
    private long profileHeaderAnimationStartTime = 0;
    private int lastAnimatedItem = 1;

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
        SoundItem item = filteredSoundItems.get(position);
        holder.tvSoundItemName.setText(item.getName());
        holder.ivSoundItem.setImageDrawable(ContextCompat.getDrawable(context, item.getImage()));

        Palette.generateAsync(BitmapFactory.decodeResource(context.getResources(), item.getImage()),
                32, new Palette.PaletteAsyncListener() {
                    @Override
                    public void onGenerated(Palette palette) {
                        holder.tvSoundItemName.setBackgroundColor(palette.getVibrantColor(Color.LTGRAY));
                        animateItemCard(holder);
                    }
                });
        if (lastAnimatedItem < position) lastAnimatedItem = position;
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

    private void animateItemCard(ViewHolder viewHolder) {
        if (!lockedAnimations) {
            if (lastAnimatedItem == viewHolder.getPosition()) {
                setLockedAnimations(true);
            }

            long animationDelay = profileHeaderAnimationStartTime + MAX_PHOTO_ANIMATION_DELAY - System.currentTimeMillis();
            if (profileHeaderAnimationStartTime == 0) {
                animationDelay = viewHolder.getPosition() * 30 + MAX_PHOTO_ANIMATION_DELAY;
            } else if (animationDelay < 0) {
                animationDelay = viewHolder.getPosition() * 30;
            } else {
                animationDelay += viewHolder.getPosition() * 30;
            }

            viewHolder.cardView.setScaleY(0);
            viewHolder.cardView.setScaleX(0);
            viewHolder.cardView.animate()
                    .scaleY(1)
                    .scaleX(1)
                    .setDuration(200)
                    .setInterpolator(INTERPOLATOR)
                    .setStartDelay(animationDelay)
                    .start();
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
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
