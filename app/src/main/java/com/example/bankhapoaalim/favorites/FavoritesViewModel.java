package com.example.bankhapoaalim.favorites;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.AppService;
import com.example.bankhapoaalim.Navigator;
import com.example.bankhapoaalim.widget.Result;

import java.util.List;

import info.movito.themoviedbapi.model.MovieDb;

public class FavoritesViewModel extends ViewModel {

    private AppService _service;
    private Navigator _navigator;

    public FavoritesViewModel(AppService service, Navigator navigator) {
        _service = service;
        _navigator = navigator;
    }

    public LiveData<Result<List<MovieDb>>> getFavorites() {
        return _service.getFavoriteMovies();
    }

    public void onMovieClicked(MovieDb movieDb) {
        _navigator.goMovie(movieDb);
    }
}
