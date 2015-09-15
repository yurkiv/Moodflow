package tk.genresbox.android.ui.fragments;

import android.app.Activity;
import android.app.SearchManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.genresbox.android.R;
import tk.genresbox.android.ui.adapter.FavouritesAdapter;

public class FavouritesFragment extends Fragment {
    private static final String FAV_KEY = "fav";
    private static final String TAG = "FavouritesFragment";

    @InjectView(R.id.rvFavs) protected RecyclerView rvFavs;

    private static FavouritesAdapter favAdapter;
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

    public static void updateView(){
        favAdapter.updateGenres();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach");

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
        SearchManager searchManager= (SearchManager) getActivity().getSystemService(getActivity().SEARCH_SERVICE);
        MenuItem menuItem = menu.findItem(R.id.search_view);
        menuItem.setVisible(true);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menuItem);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);
        searchView.setMaxWidth(10000);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                if (query.isEmpty()) {
                    ((FavouritesAdapter) rvFavs.getAdapter()).updateGenres();
                } else {
                    ((FavouritesAdapter) rvFavs.getAdapter()).setFilter(query);
                }
                return true;
            }
        });
    }

    public interface OnFavouritesSelectionListener {
        public void onMoodSelected(String name);
    }

}
