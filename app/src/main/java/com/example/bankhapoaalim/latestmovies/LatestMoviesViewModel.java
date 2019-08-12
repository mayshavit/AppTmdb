package com.example.bankhapoaalim.latestmovies;

import android.app.Service;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.AppService;
import com.example.bankhapoaalim.Navigator;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class LatestMoviesViewModel extends ViewModel {

    private AppService _service;
    private LiveData<List<MovieDb>> _moviesLiveData;
    private Navigator _navigator;

    public LatestMoviesViewModel(Navigator navigator, AppService service) {
        _service = service;
        _navigator = navigator;

//        List<MovieDb> movies = _service.getLatestMovies();
//
//        _moviesLiveData = ;
    }

    public LiveData<List<MovieDb>> getLatestMovies() {
        return _service.getLatestMovies();
    }

    public void onMovieClicked(MovieDb movieDb) {
        _navigator.goMovie(movieDb);
    }
}
