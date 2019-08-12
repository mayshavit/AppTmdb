package com.example.bankhapoaalim;

import android.app.Service;

import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.favorites.FavoritesViewModel;
import com.example.bankhapoaalim.latestmovies.LatestMoviesViewModel;
import com.example.bankhapoaalim.login.LoginViewModel;
import com.example.bankhapoaalim.welcome.WelcomeFragmentViewModel;

public class ViewModelsFactory {

    private Navigator _navigator;
    private AppService _service;

    public ViewModelsFactory(Navigator navigator, AppService service) {
        _navigator = navigator;
        _service = service;

    }

    public ViewModel getViewModel(Class viewModelClass) {
        ViewModel viewModel = null;
        if (viewModelClass.equals(WelcomeFragmentViewModel.class)) {
            viewModel = new WelcomeFragmentViewModel(_navigator, _service);
        }
        if (viewModelClass.equals(LatestMoviesViewModel.class)) {
            viewModel = new LatestMoviesViewModel(_navigator, _service);
        }
        if (viewModelClass.equals(LoginViewModel.class)) {
            viewModel = new LoginViewModel(_service);
        }
        if (viewModelClass.equals(FavoritesViewModel.class)) {
            viewModel = new FavoritesViewModel(_service, _navigator);
        }

        return viewModel;
    }
}