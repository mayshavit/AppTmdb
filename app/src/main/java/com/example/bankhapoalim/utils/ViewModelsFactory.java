package com.example.bankhapoalim.utils;

import androidx.lifecycle.ViewModel;

import com.example.bankhapoalim.AppNetwork;
import com.example.bankhapoalim.Navigator;
import com.example.bankhapoalim.favorites.FavoritesViewModel;
import com.example.bankhapoalim.latestmovies.LatestMoviesViewModel;
import com.example.bankhapoalim.login.LoginViewModel;
import com.example.bankhapoalim.welcome.WelcomeFragmentViewModel;

/**
 * This class creates the requested ViewModel according to the give class.
 */
public class ViewModelsFactory {

    private Navigator _navigator;
    private AppNetwork _service;

    public ViewModelsFactory(Navigator navigator, AppNetwork service) {
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
