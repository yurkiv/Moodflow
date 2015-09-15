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

import org.parceler.Parcels;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import tk.genresbox.android.R;
import tk.genresbox.android.ui.adapter.GenresAdapter;

public class GenresFragment extends Fragment {

    private static final String GENRES_KEY = "genres";

    @InjectView(R.id.rvGenres) protected RecyclerView rvGenres;

    private List<String> genres;
    private static GenresAdapter genresAdapter;
    private OnGenreSelectionListener genreSelectionListener;

    public static GenresFragment newInstance(List<String> genresList) {
        GenresFragment fragment = new GenresFragment();
        Bundle args = new Bundle();
        args.putParcelable(GENRES_KEY, Parcels.wrap(genresList));
        fragment.setArguments(args);
        return fragment;
    }

    public GenresFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            genres=Parcels.unwrap(getArguments().getParcelable(GENRES_KEY));
        }
        genresAdapter=new GenresAdapter(getActivity(), genres);
        Log.d("gf", "genres:" +genres);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        rvGenres.setHasFixedSize(true);
        rvGenres.setLayoutManager(llm);
        rvGenres.setAdapter(genresAdapter);
        genresAdapter.setOnItemClickListener(new GenresAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (genreSelectionListener != null) {
                    genreSelectionListener.onGenreSelected(genresAdapter.getFilteredGenres().get(position));
                }
            }
        });

        return view;
    }

    public static void updateView(){
        genresAdapter.updateGenres();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            genreSelectionListener = (OnGenreSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        genreSelectionListener = null;
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
                if (query.isEmpty()){
                    ((GenresAdapter) rvGenres.getAdapter()).flushFilter();
                } else {
                    ((GenresAdapter) rvGenres.getAdapter()).setFilter(query);
                }
                return true;
            }
        });
    }


    public interface OnGenreSelectionListener {
        public void onGenreSelected (String name);
    }

}
