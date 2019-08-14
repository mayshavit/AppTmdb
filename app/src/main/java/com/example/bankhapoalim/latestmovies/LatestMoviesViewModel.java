package com.example.bankhapoalim.latestmovies;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoalim.AppNetwork;
import com.example.bankhapoalim.Navigator;
import com.example.bankhapoalim.widget.Result;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class LatestMoviesViewModel extends ViewModel {

    private AppNetwork _service;
    private Navigator _navigator;

    public LatestMoviesViewModel(Navigator navigator, AppNetwork service) {
        _service = service;
        _navigator = navigator;
    }

    public LiveData<Result<List<MovieDb>>> getLatestMovies() {
        return _service.getLatestMovies();
    }

    public void onMovieClicked(MovieDb movieDb) {
        _navigator.goMovie(movieDb);
    }
}
