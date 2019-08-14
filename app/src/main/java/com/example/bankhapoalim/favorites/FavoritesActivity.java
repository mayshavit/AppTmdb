package com.example.bankhapoalim.favorites;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModel;

import com.example.bankhapoalim.AppNavigator;
import com.example.bankhapoalim.AppNetwork;
import com.example.bankhapoalim.Navigator;
import com.example.bankhapoalim.R;
import com.example.bankhapoalim.utils.ViewModelsFactory;

/**
 * This activity shows the user's favorites movies
 */
public class FavoritesActivity extends AppCompatActivity {

    private ViewModelsFactory _viewModelsFactory;
    private FrameLayout _loadingFrameLayout;
    private FrameLayout _errorFrameLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _loadingFrameLayout = findViewById(R.id.on_action_loader);
        _errorFrameLayout = findViewById(R.id.on_action_error);

        AppNetwork service = AppNetwork.getInstance();
        Navigator navigator = new AppNavigator(this);
        service.getConnectLiveData().observe(this, result -> {
            _loadingFrameLayout.setVisibility(View.GONE);
            if (result.isSuccessful()) {
                _errorFrameLayout.setVisibility(View.GONE);
                _viewModelsFactory = new ViewModelsFactory(navigator, service);
                navigator.goFavorites();
            } else {
                _errorFrameLayout.setVisibility(View.VISIBLE);
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
