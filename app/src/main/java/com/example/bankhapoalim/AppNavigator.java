package com.example.bankhapoalim;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bankhapoalim.favorites.FavoritesActivity;
import com.example.bankhapoalim.favorites.FavoritesFragment;
import com.example.bankhapoalim.latestmovies.LatestMoviesFragment;
import com.example.bankhapoalim.login.LoginFragment;
import com.example.bankhapoalim.moviescreen.MovieFragment;
import com.example.bankhapoalim.welcome.WelcomeFragment;

import info.movito.themoviedbapi.model.MovieDb;

/**
 * This class navigates between fragments/activities of the application
 */
public class AppNavigator implements Navigator {

    private AppCompatActivity _activity;

    public AppNavigator(AppCompatActivity activity) {
        _activity = activity;
    }

    @Override
    public void goWelcome() {
        openFragment(new WelcomeFragment());
    }

    @Override
    public void goLatestMovies() {
        openFragment(new LatestMoviesFragment());
    }

    @Override
    public void goLogin() {
        LoginFragment loginFragment = new LoginFragment();
        openFragment(loginFragment);
    }

    @Override
    public void goFavorites() {
        if (_activity instanceof MainActivity) {
            Intent intent = new Intent(_activity, FavoritesActivity.class);
            _activity.startActivity(intent);
        } else if (_activity instanceof FavoritesActivity) {
            FavoritesFragment favoritesFragment = new FavoritesFragment();
            openFragment(favoritesFragment);
        }
    }

    @Override
    public void goMovie(MovieDb movieDb) {
        Bundle bundle = new Bundle();
        bundle.putString(MovieFragment.MOVIE_TITLE, movieDb.getTitle());
        bundle.putString(MovieFragment.MOVIE_DEACRIPTION, movieDb.getOverview());
        bundle.putString(MovieFragment.MOVIE_YEAR_OF_PRODUCTION, movieDb.getReleaseDate());
        bundle.putFloat(MovieFragment.MOVIE_RATE, movieDb.getVoteAverage());
        bundle.putString(MovieFragment.MOVIE_IMAGE_POSTER_URL, movieDb.getPosterPath());
        MovieFragment movieFragment = new MovieFragment();
        movieFragment.setArguments(bundle);
        openFragment(movieFragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = _activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragments_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
