package com.example.bankhapoalim.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bankhapoalim.MainActivity;
import com.example.bankhapoalim.R;

public class WelcomeFragment extends Fragment {
    private TextView _latestMoviesBtn;
    private TextView _loginBtn;
    private TextView _loginAdditionalText;
    private TextView _favoritesBtn;
    private WelcomeFragmentViewModel _viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_fragment, container, false);

        _latestMoviesBtn = view.findViewById(R.id.welcome_latest_movies);
        _loginBtn = view.findViewById(R.id.welcome_login);
        _loginAdditionalText = view.findViewById(R.id.welcome_login_additional_text);
        _favoritesBtn = view.findViewById(R.id.welcome_favorites);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = (WelcomeFragmentViewModel) ((MainActivity) getActivity()).getViewModel(WelcomeFragmentViewModel.class);
        _latestMoviesBtn.setOnClickListener(v -> _viewModel.onLatestMovies());
        _favoritesBtn.setOnClickListener(v -> _viewModel.onFavorites());
        _loginBtn.setOnClickListener(v -> _viewModel.onLogin());

        _viewModel.getLoginLiveData().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccessful()) {
                _favoritesBtn.setVisibility(View.VISIBLE);
                _loginBtn.setVisibility(View.GONE);
                _loginAdditionalText.setVisibility(View.GONE);
            } else {
                _favoritesBtn.setVisibility(View.GONE);
                _loginBtn.setVisibility(View.VISIBLE);
                _loginAdditionalText.setVisibility(View.VISIBLE);
            }
        });
    }
}
