package tk.moodflow.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.moodflow.android.R;
import tk.moodflow.android.model.SoundItem;
import tk.moodflow.android.ui.adapter.SoundItemAdapter;
import tk.moodflow.android.ui.view.MarginDecoration;
import tk.moodflow.android.ui.view.RecyclerItemClickListener;

public class MoodsFragment extends Fragment {
    private static final String MOODS_KEY = "moods";

    @InjectView(R.id.rvMoods) protected RecyclerView rvMoods;

    private List<SoundItem> moods;
    private SoundItemAdapter moodsAdapter;
    private OnMoodSelectionListener moodSelectionListener;

    public static MoodsFragment newInstance(List<SoundItem> moodsList) {
        MoodsFragment fragment = new MoodsFragment();
        Bundle args = new Bundle();
        args.putParcelable(MOODS_KEY, Parcels.wrap(moodsList));
        fragment.setArguments(args);
        return fragment;
    }

    public MoodsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            moods=Parcels.unwrap(getArguments().getParcelable(MOODS_KEY));
        }
        moodsAdapter=new SoundItemAdapter(getActivity(), moods);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_moods, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);
        rvMoods.addItemDecoration(new MarginDecoration(getActivity()));
        rvMoods.setHasFixedSize(true);
        rvMoods.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                moodsAdapter.setLockedAnimations(true);
            }
        });
        rvMoods.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (moodSelectionListener != null) {
                    moodSelectionListener.onMoodSelected(position);
                }
            }
        }));
        rvMoods.setAdapter(moodsAdapter);

        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            moodSelectionListener = (OnMoodSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        moodSelectionListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()){
                    ((SoundItemAdapter) rvMoods.getAdapter()).flushFilter();
                } else {
                    ((SoundItemAdapter) rvMoods.getAdapter()).setFilter(query);
                }
                return true;
            }
        });
    }

    public interface OnMoodSelectionListener {
        public void onMoodSelected(int id);
    }

}
