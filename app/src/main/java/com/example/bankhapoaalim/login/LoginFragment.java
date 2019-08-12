package com.example.bankhapoaalim.login;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.bankhapoaalim.MainActivity;
import com.example.bankhapoaalim.R;

public class LoginFragment extends Fragment {

    private EditText _userEdit;
    private EditText _passwordEdit;
    private Button _loginButton;
    private LoginViewModel _viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.login_fragment, container, false);

        _userEdit = view.findViewById(R.id.login_user_edit);
        _passwordEdit = view.findViewById(R.id.login_password_edit);
        _loginButton = view.findViewById(R.id.login_button);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        _viewModel = (LoginViewModel) ((MainActivity) getActivity()).getViewModel(LoginViewModel.class);
        _viewModel.getLoginResult().observe(getViewLifecycleOwner(), result -> {
            if (result.isSuccessful()) {
                getActivity().onBackPressed();
            } else {

            }
        });

        _loginButton.setOnClickListener(v -> {
            hideKeyboard();
            String username = _userEdit.getText().toString();
            String password = _passwordEdit.getText().toString();

            if (!username.isEmpty() && !password.isEmpty()) {
                _viewModel.onLoginButton(username, password);
            }
        });
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        _loginButton.requestFocus();
        inputMethodManager.hideSoftInputFromWindow(_loginButton.getWindowToken(), 0);
    }

}
