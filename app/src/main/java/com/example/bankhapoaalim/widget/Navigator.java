package com.example.bankhapoaalim.widget;

import info.movito.themoviedbapi.model.MovieDb;

public interface Navigator {
    void goWelcome();
    void goLatestMovies();
    void goLogin();
    void goFavorites();
    void goMovie(MovieDb movieDb);
}
