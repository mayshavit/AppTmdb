package com.example.bankhapoaalim;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.latestmovies.LatestMoviesFragment;
import com.example.bankhapoaalim.login.LoginFragment;
import com.example.bankhapoaalim.moviescreen.MovieFragment;
import com.example.bankhapoaalim.welcome.WelcomeFragment;

import info.movito.themoviedbapi.model.MovieDb;

public class MainActivity extends AppCompatActivity{
    private ViewModelsFactory _viewModelsFactory;
    private TextView _loadingTextView;
    private TextView _errorTextView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        _loadingTextView = findViewById(R.id.loading_view);
        _errorTextView = findViewById(R.id.error_view);

        AppService service = AppService.getInstance();
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
//        service.init();
//        startService(new Intent(getApplicationContext(), AppService.class));
    }

//    @Override
//    public View onCreateView(String name, Context context, AttributeSet attrs) {
//        View view = super.onCreateView(name, context, attrs);
//
//
//        return view;
//    }

    @Override
    protected void onStart() {
        super.onStart();
//        goWelcome();
    }

    public ViewModel getViewModel(Class viewModelClass) {
        return _viewModelsFactory.getViewModel(viewModelClass);
    }
}
