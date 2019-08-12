package com.example.bankhapoaalim.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.AppService;
import com.example.bankhapoaalim.widget.Result;

public class LoginViewModel extends ViewModel {

    private AppService _service;

    public LoginViewModel(AppService service) {
        _service = service;
    }

    public void onLoginButton(String username, String password) {
        _service.loginUser(username, password);
    }

    public LiveData<Result> getLoginResult() {
        return _service.getLoginLiveData();
    }
}
