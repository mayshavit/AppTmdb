package com.example.bankhapoaalim.widget;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.bankhapoaalim.MainActivity;
import com.example.bankhapoaalim.R;
import com.example.bankhapoaalim.favorites.FavoritesActivity;
import com.example.bankhapoaalim.favorites.FavoritesFragment;
import com.example.bankhapoaalim.latestmovies.LatestMoviesFragment;
import com.example.bankhapoaalim.login.LoginFragment;
import com.example.bankhapoaalim.moviescreen.MovieFragment;
import com.example.bankhapoaalim.welcome.WelcomeFragment;

import info.movito.themoviedbapi.model.MovieDb;

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
        MovieFragment movieFragment = MovieFragment.createMovieFragment(movieDb);
        openFragment(movieFragment);
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = _activity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragments_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}
