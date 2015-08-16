package tk.moodflow.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.moodflow.android.R;
import tk.moodflow.android.ui.adapter.FavouritesAdapter;

public class FavouritesFragment extends Fragment {
    private static final String FAV_KEY = "fav";

    @InjectView(R.id.rvFavs) protected RecyclerView rvFavs;

    private List<String> fav;
    private FavouritesAdapter favAdapter;
    private OnFavouritesSelectionListener vafSelectionListener;

    public static FavouritesFragment newInstance() {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FavouritesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favAdapter=new FavouritesAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_moods, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        rvFavs.setHasFixedSize(true);
        rvFavs.setLayoutManager(llm);

        rvFavs.setAdapter(favAdapter);
        favAdapter.setOnItemClickListener(new FavouritesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (vafSelectionListener != null) {
                    vafSelectionListener.onMoodSelected(favAdapter.getGenres().get(position));
                }
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        favAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            vafSelectionListener = (OnFavouritesSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        vafSelectionListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem menuItem = menu.findItem(R.id.search_view);
        menuItem.setVisible(false);
    }

    public interface OnFavouritesSelectionListener {
        public void onMoodSelected(String name);
    }

}
