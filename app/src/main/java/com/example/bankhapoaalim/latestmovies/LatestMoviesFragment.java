package com.example.bankhapoaalim.latestmovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankhapoaalim.MainActivity;
import com.example.bankhapoaalim.widget.MoviesAdapter;
import com.example.bankhapoaalim.R;

import info.movito.themoviedbapi.model.MovieDb;

public class LatestMoviesFragment extends Fragment {

    private RecyclerView _moviesRecyclerView;

    private LatestMoviesViewModel _viewModel;
    private MoviesAdapter _moviesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.latest_movies_fragment, container, false);
        _moviesRecyclerView = view.findViewById(R.id.movies_recycler_view);
        _moviesAdapter = new MoviesAdapter(getContext(), new MoviesAdapter.OnMovieClickListener() {
            @Override
            public void onMovieClicked(MovieDb movieDb) {
                _viewModel.onMovieClicked(movieDb);
            }
        });
        _moviesRecyclerView.setAdapter(_moviesAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = (LatestMoviesViewModel) ((MainActivity) getActivity()).getViewModel(LatestMoviesViewModel.class);
        _viewModel.getLatestMovies().observe(getViewLifecycleOwner(), result -> {
            _moviesAdapter.setMovies(result);
        });

    }
}
