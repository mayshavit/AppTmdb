package com.example.bankhapoaalim;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bankhapoaalim.widget.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import info.movito.themoviedbapi.TmdbAccount;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.SessionToken;

public class AppNetwork {
    private static final String API_KEY = "a6b73c703048141f02f8fb72c9bf289b";

    private static AppNetwork _this;
    private TmdbApi _api;
    private TmdbAccount _account;
    private TokenSession _tokenSession;
    private MutableLiveData<Result> _connectLiveData;
    private MutableLiveData<Result> _loginLiveData;
    private SessionToken _sessionToken;
    private AccountID _accountID;

    private AppNetwork() {
        _connectLiveData = new MutableLiveData<>();
        _loginLiveData = new MutableLiveData<>();
        init();
    }

    public static AppNetwork getInstance() {
        if (_this == null) {
            _this = new AppNetwork();
        }
        return _this;
    }


    private void init() {
        NetworkTaskListener<Result<TmdbApi>> listener = new NetworkTaskListener<Result<TmdbApi>>() {
            @Override
            public void onFinishRequest(Result<TmdbApi> result) {
                if (result.isSuccessful()) {
                    _api = result.getValue();
                    _account = _api.getAccount();
                    _connectLiveData.postValue(result);
                }
            }
        };

        NetworkTaskFunction<TmdbApi> function = new NetworkTaskFunction<TmdbApi>() {
            @Override
            public Result<TmdbApi> executeFunction() {
                TmdbApi api;
                try {
                    api = new TmdbApi(API_KEY);
                    return Result.successful().setValue(api);
                } catch (Exception e) {
                    return Result.failure();
                }
            }
        };

        NetworkAsyncTask<TmdbApi> task = new NetworkAsyncTask<TmdbApi>(listener, function);
        task.execute();
    }

    public void loginUser(String username, String password) {
        NetworkTaskListener<Result<TokenSession>> listener = new NetworkTaskListener<Result<TokenSession>>() {
            @Override
            public void onFinishRequest(Result<TokenSession> result) {
                if (result.isSuccessful()) {
                    _tokenSession = result.getValue();
                }
                _loginLiveData.postValue(result);
            }
        };

        NetworkTaskFunction<TokenSession> function = new NetworkTaskFunction<TokenSession>() {
            @Override
            public Result<TokenSession> executeFunction() {
                TokenSession tokenSession;
                try {
                    tokenSession = _api.getAuthentication().getSessionLogin(username,password);
                    return Result.successful().setValue(tokenSession);
                } catch (Exception e) {
                    return Result.failure();
                }
            }
        };

        NetworkAsyncTask<TokenSession> task = new NetworkAsyncTask<>(listener, function);
        task.execute();
    }

    public MutableLiveData<Result> getConnectLiveData() {
        return _connectLiveData;
    }

    public MutableLiveData<Result> getLoginLiveData() {
        return _loginLiveData;
    }

    public LiveData<Result<List<MovieDb>>> getLatestMovies() {
        MutableLiveData<Result<List<MovieDb>>> moviesMutableLiveData = new MutableLiveData<>();
        NetworkTaskListener<Result<MovieResultsPage>> listener = new NetworkTaskListener<Result<MovieResultsPage>>() {
            @Override
            public void onFinishRequest(Result<MovieResultsPage> result) {
                if (result.isSuccessful() && result.getValue() != null && result.getValue().getResults() != null) {
                    moviesMutableLiveData.postValue(Result.successful().setValue(result.getValue().getResults()));
                } else {
                    moviesMutableLiveData.postValue(Result.failure());
                }
            }
        };

        NetworkTaskFunction<MovieResultsPage> function = new NetworkTaskFunction<MovieResultsPage>() {
            @Override
            public Result<MovieResultsPage> executeFunction() {
                Calendar calendar = Calendar.getInstance();
                Date date = calendar.getTime();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                Discover discover = new Discover();
                discover.releaseDateLte(dateFormat.format(date));

                calendar.add(Calendar.MONTH, -1);
                date = calendar.getTime();

                discover.releaseDateGte(dateFormat.format(date));

                MovieResultsPage movieResultsPage;
                try {
                    movieResultsPage = _api.getDiscover().getDiscover(discover);
                    return Result.successful().setValue(movieResultsPage);
                } catch (Exception e) {
                    return Result.failure();
                }
            }
        };

        NetworkAsyncTask<MovieResultsPage> task = new NetworkAsyncTask<>(listener, function);
        task.execute();

        return moviesMutableLiveData;
    }

    public LiveData<Result<List<MovieDb>>> getFavoriteMovies() {
        MutableLiveData<Result<List<MovieDb>>> favoriteLiveData = new MutableLiveData<Result<List<MovieDb>>>();

        if (_sessionToken == null) {
            _sessionToken = new SessionToken(_tokenSession.getSessionId());
            getAccount(new NetworkCallback() {
                @Override
                public void onSuccess() {
                    getFavoriteMoviesFromServer(favoriteLiveData);
                }

                @Override
                public void onFailure() {
                    favoriteLiveData.postValue(Result.failure());
                }
            });
        } else {
            getFavoriteMoviesFromServer(favoriteLiveData);
        }
        return favoriteLiveData;
    }

    private void getFavoriteMoviesFromServer(MutableLiveData<Result<List<MovieDb>>> favoriteLiveData) {

        NetworkTaskListener<Result<MovieResultsPage>> listener = new NetworkTaskListener<Result<MovieResultsPage>>() {
            @Override
            public void onFinishRequest(Result<MovieResultsPage> result) {
                if (result.isSuccessful() && result.getValue() != null && !result.getValue().getResults().isEmpty()) {
                    favoriteLiveData.postValue(Result.successful().setValue(result.getValue().getResults()));
                } else {
                    favoriteLiveData.postValue(Result.failure());
                }
            }
        };

        NetworkTaskFunction<MovieResultsPage> function = new NetworkTaskFunction<MovieResultsPage>() {
            @Override
            public Result<MovieResultsPage> executeFunction() {

                try {
                    MovieResultsPage movieResultsPage = _account.getFavoriteMovies(_sessionToken, _accountID);
                    return Result.successful().setValue(movieResultsPage);
                } catch (Exception e) {
                    return Result.failure();
                }
            }
        };

        NetworkAsyncTask<MovieResultsPage> task = new NetworkAsyncTask<>(listener, function);
        task.execute();
    }

    private void getAccount(NetworkCallback callback) {
        NetworkTaskListener<Result<Account>> listener = new NetworkTaskListener<Result<Account>>() {

            @Override
            public void onFinishRequest(Result<Account> result) {
                if (result.isSuccessful() && result.getValue() != null) {
                    _accountID = new AccountID(result.getValue().getId());
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }
        };

        NetworkTaskFunction<Account> function = new NetworkTaskFunction<Account>() {
            @Override
            public Result<Account> executeFunction() {
                Account account;
                try {
                    account = _account.getAccount(_sessionToken);
                    return Result.successful().setValue(account);
                } catch (Exception e) {
                    return Result.failure();
                }


            }
        };

        NetworkAsyncTask<Account> task = new NetworkAsyncTask<>(listener, function);
        task.execute();
    }

    private interface NetworkTaskListener<T> {
        void onFinishRequest(T object);
    }

    private interface NetworkTaskFunction<T> {
        Result<T> executeFunction();
    }

    private interface NetworkCallback {
        void onSuccess();

        void onFailure();
    }

    private static class NetworkAsyncTask<T> extends AsyncTask<Void, Void, Result<T>> {
        private NetworkTaskListener<Result<T>> _listener;
        private NetworkTaskFunction<T> _function;

        NetworkAsyncTask(NetworkTaskListener<Result<T>> listener, NetworkTaskFunction<T> function) {
            _listener = listener;
            _function = function;
        }

        @Override
        protected Result<T> doInBackground(Void ... voids) {
            return _function.executeFunction();
        }

        @Override
        protected void onPostExecute(Result<T> t) {
            super.onPostExecute(t);
            _listener.onFinishRequest(t);
        }

    }

}
