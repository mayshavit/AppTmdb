package com.example.bankhapoaalim.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.AppService;
import com.example.bankhapoaalim.Navigator;
import com.example.bankhapoaalim.widget.Result;

public class WelcomeFragmentViewModel extends ViewModel {
    private Navigator _navigator;
    private AppService _service;

    public WelcomeFragmentViewModel(Navigator navigator, AppService service) {
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