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

public class GenresFragment extends Fragment {

    private static final String GENRES_KEY = "genres";

    @InjectView(R.id.rvGenres) protected RecyclerView rvGenres;

    private List<SoundItem> genres;
    private SoundItemAdapter genresAdapter;
    private OnGenreSelectionListener genreSelectionListener;

    public static GenresFragment newInstance(List<SoundItem> genresList) {
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
        genresAdapter=new SoundItemAdapter(getActivity(), genres);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_genres, container, false);
        ButterKnife.inject(this, view);
        setHasOptionsMenu(true);

        rvGenres.addItemDecoration(new MarginDecoration(getActivity()));
        rvGenres.setHasFixedSize(true);
        rvGenres.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (genreSelectionListener != null) {
                    genreSelectionListener.onGenreSelected(position);
                }
            }
        }));
        rvGenres.setAdapter(genresAdapter);

        return view;
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
                    ((SoundItemAdapter) rvGenres.getAdapter()).flushFilter();
                } else {
                    ((SoundItemAdapter) rvGenres.getAdapter()).setFilter(query);
                }
                return true;
            }
        });
    }


    public interface OnGenreSelectionListener {
        public void onGenreSelected (int id);
    }

}
