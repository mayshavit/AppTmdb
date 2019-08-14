package com.example.bankhapoalim.favorites;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bankhapoalim.R;
import com.example.bankhapoalim.utils.MoviesAdapter;

import info.movito.themoviedbapi.model.MovieDb;

public class FavoritesFragment extends Fragment {
    private RecyclerView _moviesRecyclerView;

    private FavoritesViewModel _viewModel;
    private MoviesAdapter _moviesAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.favorites_fragment, container, false);
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
        _viewModel = (FavoritesViewModel) ((FavoritesActivity) getActivity()).getViewModel(FavoritesViewModel.class);
        _viewModel.getFavorites().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccessful()) {
                _moviesAdapter.setMovies(result.getValue());
            }
        });
    }
}
