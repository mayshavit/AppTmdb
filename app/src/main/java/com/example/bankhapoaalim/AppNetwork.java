package com.example.bankhapoaalim;

import android.os.AsyncTask;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.bankhapoaalim.widget.Result;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import info.movito.themoviedbapi.TmdbAccount;
import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.TmdbAuthentication;
import info.movito.themoviedbapi.TmdbDiscover;
import info.movito.themoviedbapi.model.Discover;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.config.Account;
import info.movito.themoviedbapi.model.config.TokenSession;
import info.movito.themoviedbapi.model.core.AccountID;
import info.movito.themoviedbapi.model.core.MovieResultsPage;
import info.movito.themoviedbapi.model.core.SessionToken;

public class AppNetwork {
    private static final String API_KEY = "a6b73c703048141f02f8fb72c9bf289b";
    private static final String TASK_CONNECT = "task_connect";
    private static final String TASK_LOGIN = "task_login";
    private static final String TASK_LATEST_MOVIES = "task_latest_movies";
    private static final String TASK_ACCOUNT = "task_account";
    private static final String TASK_FAVORITES = "task_favorites";
    private static final String PARAM_USER = "username";
    private static final String PARAM_PASSWORD = "password";
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

        ApplicationAsyncTask<TmdbApi, String> task = new ApplicationAsyncTask<>(object -> {
            if (object != null) {
                _api = object;
                _account = _api.getAccount();
                _connectLiveData.postValue(Result.successful());
            } else {
                _connectLiveData.postValue(Result.failure());
            }
        }, API_KEY);
        task.execute(TASK_CONNECT);
    }

    public void loginUser(String username, String password) {
        Map<String, String> param = new HashMap<>();
        param.put(PARAM_USER, username);
        param.put(PARAM_PASSWORD, password);

        ApplicationAsyncTask<TokenSession, Pair<TmdbAuthentication, Map<String, String>>> task = new ApplicationAsyncTask<>(new AsyncTaskListener<TokenSession>() {
            @Override
            public void onFinishRequest(TokenSession tokenSession) {
                if (tokenSession != null) {
                    _tokenSession = tokenSession;
                    _loginLiveData.postValue(Result.successful());
                } else {
                    _loginLiveData.postValue(Result.failure());
                }

            }
        }, new Pair<>(_api.getAuthentication(), param));
        task.execute(TASK_LOGIN);

    }

    public MutableLiveData<Result> getConnectLiveData() {
        return _connectLiveData;
    }

    public MutableLiveData<Result> getLoginLiveData() {
        return _loginLiveData;
    }

    public LiveData<List<MovieDb>> getLatestMovies() {
        MutableLiveData<List<MovieDb>> moviesMutableLiveData = new MutableLiveData<>();
        ApplicationAsyncTask<MovieResultsPage, TmdbDiscover> task = new ApplicationAsyncTask<>(new AsyncTaskListener<MovieResultsPage>() {
            @Override
            public void onFinishRequest(MovieResultsPage resultsPage) {
                if (resultsPage != null) {
                    moviesMutableLiveData.postValue(resultsPage.getResults());
                }
            }
        }, _api.getDiscover());
        task.execute(TASK_LATEST_MOVIES);
        return moviesMutableLiveData;
    }

    public LiveData<Result<List<MovieDb>>> getFavoriteMovies() {
        MutableLiveData<Result<List<MovieDb>>> favoriteLiveData = new MutableLiveData<Result<List<MovieDb>>>();

        if (_sessionToken == null) {
            _sessionToken = new SessionToken(_tokenSession.getSessionId());
            getAccount(new AppServiceCallback() {
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
        Map<String, Object> param = new HashMap<>();
        param.put(SessionToken.class.getName(), _sessionToken);
        param.put(AccountID.class.getName(), _accountID);
        param.put(TmdbAccount.class.getName(), _account);
        ApplicationAsyncTask<MovieResultsPage, Map<String, Object>> task = new ApplicationAsyncTask<>(new AsyncTaskListener<MovieResultsPage>() {
            @Override
            public void onFinishRequest(MovieResultsPage movieResultsPage) {
                if (movieResultsPage != null) {
                    favoriteLiveData.postValue(Result.successful().setValue(movieResultsPage.getResults()));
                } else {
                    favoriteLiveData.postValue(Result.failure());
                }
            }
        }, param);
        task.execute(TASK_FAVORITES);
    }

    private void getAccount(AppServiceCallback callback) {
        ApplicationAsyncTask<Account, Pair<TmdbAccount, SessionToken>> task = new ApplicationAsyncTask<>(new AsyncTaskListener<Account>() {
            @Override
            public void onFinishRequest(Account account) {
                if (account != null) {
                    _accountID = new AccountID(account.getId());
                    callback.onSuccess();
                } else {
                    callback.onFailure();
                }
            }
        }, new Pair<>(_account, _sessionToken));
        task.execute(TASK_ACCOUNT);

    }

    private interface AsyncTaskListener<T> {
        void onFinishRequest(T object);
    }

    private interface AppServiceCallback {
        void onSuccess();

        void onFailure();
    }

    private static class ApplicationAsyncTask<T, H> extends AsyncTask<String, Void, T> {

        private AsyncTaskListener<T> _listener;
        private H _param;

        ApplicationAsyncTask(AsyncTaskListener<T> listener, H param) {
            _listener = listener;
            _param = param;
        }

        @Override
        protected T doInBackground(String... strings) {
            String task = strings[0];
            T data = null;
            switch (task) {
                case TASK_CONNECT:
                    data = (T) loginToTmdb((String) _param);
                    break;
                case TASK_LATEST_MOVIES:
                    data = (T) getLatestMovieResults((TmdbDiscover) _param);
                    break;
                case TASK_LOGIN:
                    data = (T) loginUserWithNameAndPassword((Pair<TmdbAuthentication, Map<String, String>>) _param);
                    break;
                case TASK_ACCOUNT:
                    data = (T) getAccount((Pair<TmdbAccount, SessionToken>) _param);
                    break;
                case TASK_FAVORITES:
                    data = (T) getFavorites((Map<String, Object>) _param);
                    break;
            }
            return data;
        }

        private TmdbApi loginToTmdb(String apiKey) {
            TmdbApi api;
            try {
                api = new TmdbApi(apiKey);
            } catch (Exception e) {
                return null;
            }

            return api;
        }

        private MovieResultsPage getLatestMovieResults(TmdbDiscover tmdbDiscover) {
            Calendar calendar = Calendar.getInstance();
            Date date = calendar.getTime();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            Discover discover = new Discover();
            discover.releaseDateLte(dateFormat.format(date));

            calendar.add(Calendar.MONTH, -1);
            date = calendar.getTime();

            discover.releaseDateGte(dateFormat.format(date));
            return tmdbDiscover.getDiscover(discover);

        }

        private TokenSession loginUserWithNameAndPassword(Pair<TmdbAuthentication, Map<String, String>> param) {
            TmdbAuthentication tmdbAuthentication = param.first;
            Map<String, String> userParam = param.second;

            TokenSession tokenSession;
            try {
                tokenSession = tmdbAuthentication.getSessionLogin(userParam.get(PARAM_USER), userParam.get(PARAM_PASSWORD));
            } catch (Exception e) {
                return null;
            }
            return tokenSession;
        }

        private Account getAccount(Pair<TmdbAccount, SessionToken> param) {
            TmdbAccount tmdbAccount = param.first;
            SessionToken sessionToken = param.second;
            return tmdbAccount.getAccount(sessionToken);
        }

        private MovieResultsPage getFavorites(Map<String, Object> param) {
            SessionToken sessionToken = (SessionToken) param.get(SessionToken.class.getName());
            AccountID accountID = (AccountID) param.get(AccountID.class.getName());
            TmdbAccount tmdbAccount = (TmdbAccount) param.get(TmdbAccount.class.getName());

            return tmdbAccount.getFavoriteMovies(sessionToken, accountID);
        }

        @Override
        protected void onPostExecute(T value) {
            super.onPostExecute(value);
            _listener.onFinishRequest(value);
        }
    }
}
