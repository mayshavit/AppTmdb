package com.example.bankhapoaalim;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.utils.ViewModelsFactory;

public class MainActivity extends AppCompatActivity {
    private ViewModelsFactory _viewModelsFactory;
    private View _loadingTextView;
    private TextView _errorTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _loadingTextView = findViewById(R.id.loading_view);
        _errorTextView = findViewById(R.id.error_view);

        AppNetwork service = AppNetwork.getInstance();
        Navigator navigator = new AppNavigator(this);
        service.getConnectLiveData().observe(this, result -> {
            _loadingTextView.setVisibility(View.GONE);
            if (result.isSuccessful()) {
                _viewModelsFactory = new ViewModelsFactory(navigator, service);
                navigator.goWelcome();
            } else {
                _errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    public ViewModel getViewModel(Class viewModelClass) {
        return _viewModelsFactory.getViewModel(viewModelClass);
    }
}
