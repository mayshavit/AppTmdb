package com.example.bankhapoaalim.favorites;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoaalim.AppNavigator;
import com.example.bankhapoaalim.AppService;
import com.example.bankhapoaalim.Navigator;
import com.example.bankhapoaalim.R;
import com.example.bankhapoaalim.ViewModelsFactory;

public class FavoritesActivity extends AppCompatActivity {

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
                navigator.goFavorites();
            } else {
                _errorTextView.setVisibility(View.VISIBLE);
            }
        });
    }

    public ViewModel getViewModel(Class viewModelClass) {
        return _viewModelsFactory.getViewModel(viewModelClass);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }

    }
}
