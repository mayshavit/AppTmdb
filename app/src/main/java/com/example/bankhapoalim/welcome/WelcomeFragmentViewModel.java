package com.example.bankhapoalim.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoalim.AppNetwork;
import com.example.bankhapoalim.Navigator;
import com.example.bankhapoalim.widget.Result;

public class WelcomeFragmentViewModel extends ViewModel {
    private Navigator _navigator;
    private AppNetwork _service;

    public WelcomeFragmentViewModel(Navigator navigator, AppNetwork service) {
        _navigator = navigator;
        _service = service;
    }

    public void onLatestMovies() {
        _navigator.goLatestMovies();
    }

    public void onFavorites() {
        _navigator.goFavorites();
    }

    public void onLogin() {
        _navigator.goLogin();
    }

    public LiveData<Result> getLoginLiveData() {
        return _service.getLoginLiveData();
    }
}
