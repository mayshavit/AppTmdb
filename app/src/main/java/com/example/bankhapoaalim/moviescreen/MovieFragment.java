package com.example.bankhapoaalim.moviescreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.bankhapoaalim.R;

import info.movito.themoviedbapi.model.MovieDb;

public class MovieFragment extends Fragment {

    private static final String BASE_URL = "https://image.tmdb.org/t/p/original";
    private MovieDb _movieDb;
    private TextView _title;
    private TextView _description;
    private TextView _yearOfProduction;
    private TextView _rate;
    private ImageView _imageView;

    public static MovieFragment createMovieFragment(MovieDb movieDb) {
        MovieFragment movieFragment = new MovieFragment();
        movieFragment.setMovieDb(movieDb);

        return movieFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.movie_fragment, container, false);
        _title = view.findViewById(R.id.movie_title);
        _description = view.findViewById(R.id.movie_description);
        _yearOfProduction = view.findViewById(R.id.movie_year_production);
        _rate = view.findViewById(R.id.movie_rate);
        _imageView = view.findViewById(R.id.movie_image);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (_movieDb != null) {
            _title.setText(_movieDb.getTitle());
            _description.setText(_movieDb.getOverview());
            _yearOfProduction.setText(_movieDb.getReleaseDate());
            _rate.setText(String.valueOf(_movieDb.getUserRating()));
            Glide.with(this).load(BASE_URL + _movieDb.getPosterPath()).into(_imageView);
        }
    }

    public void setMovieDb(MovieDb movieDb) {
        _movieDb = movieDb;
    }

}
