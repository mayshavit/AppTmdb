package com.example.bankhapoalim.moviescreen;

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
import com.example.bankhapoalim.R;

/**
 * This fragment shows a movie and its details.
 */
public class MovieFragment extends Fragment {

    private static final String BASE_URL = "https://image.tmdb.org/t/p/original";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_DEACRIPTION = "movie_description";
    public static final String MOVIE_YEAR_OF_PRODUCTION = "movie_year_of_production";
    public static final String MOVIE_RATE = "movie_rate";
    public static final String MOVIE_IMAGE_POSTER_URL = "movie_image_poster_url";

    private TextView _title;
    private TextView _description;
    private TextView _yearOfProduction;
    private TextView _rate;
    private ImageView _imageView;

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
        if (getArguments() != null) {
            _title.setText(getArguments().getString(MOVIE_TITLE));
            _description.setText(getArguments().getString(MOVIE_DEACRIPTION));
            _yearOfProduction.setText(getArguments().getString(MOVIE_YEAR_OF_PRODUCTION));
            _rate.setText(String.valueOf(getArguments().getFloat(MOVIE_RATE)));
            Glide.with(this).load(BASE_URL + getArguments().getString(MOVIE_IMAGE_POSTER_URL)).into(_imageView);
        }
    }

}
