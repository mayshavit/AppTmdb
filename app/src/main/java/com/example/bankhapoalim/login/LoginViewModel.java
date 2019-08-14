package com.example.bankhapoalim.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoalim.AppNetwork;
import com.example.bankhapoalim.widget.Result;

public class LoginViewModel extends ViewModel {

    private AppNetwork _service;

    public LoginViewModel(AppNetwork service) {
        _service = service;
    }

    public void onLoginButton(String username, String password) {
        _service.loginUser(username, password);
    }

    public LiveData<Result> getLoginResult() {
        return _service.getLoginLiveData();
    }
}
