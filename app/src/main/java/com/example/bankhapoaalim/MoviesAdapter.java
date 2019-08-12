package com.example.bankhapoaalim;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import info.movito.themoviedbapi.model.Artwork;
import info.movito.themoviedbapi.model.ArtworkType;
import info.movito.themoviedbapi.model.MovieDb;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesViewHolder> {

    private static final String BASE_URL = "https://image.tmdb.org/t/p/original";
    private List<MovieDb> _movies;
    private Context _context;
    private OnMovieClickListener _listener;

    public MoviesAdapter(Context context, OnMovieClickListener listener) {
        _context = context;
        _listener = listener;
        _movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public MoviesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movies_view, parent, false);
        MoviesViewHolder viewHolder = new MoviesViewHolder(movieView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesViewHolder holder, int position) {
        MovieDb movieDb = _movies.get(position);
        holder._movieTitle.setText(movieDb.getTitle());
//        List<String> urls = new ArrayList<>();
//        if (movieDb.getImages() != null) {
//            for (Artwork artworkImage : movieDb.getImages()) {
//                if (artworkImage.getArtworkType() == ArtworkType.POSTER) {
//                    urls.add(0, artworkImage.getFilePath());
//                } else {
//                    urls.add(artworkImage.getFilePath());
//                }
//
//            }

//        urls.add(BASE_URL + movieDb.getPosterPath());
//        urls.add(BASE_URL + movieDb.getBackdropPath());
        Glide.with(_context).load(BASE_URL + movieDb.getPosterPath()).centerCrop().into(holder._movieBackgroundImage);
        holder.itemView.setOnClickListener(v -> {
            _listener.onMovieClicked(movieDb);
        });
//        }

    }

    @Override
    public int getItemCount() {
        return _movies.size();
    }

    public static class MoviesViewHolder extends RecyclerView.ViewHolder {

        public TextView _movieTitle;
        public ImageView _movieBackgroundImage;

        public MoviesViewHolder(@NonNull View itemView) {
            super(itemView);
            _movieTitle = itemView.findViewById(R.id.movie_title);
            _movieBackgroundImage = itemView.findViewById(R.id.movie_background_image);
        }
    }

    public void setMovies(List<MovieDb> movies) {
        if (_movies != null) {
            _movies.clear();
        }
        _movies.addAll(movies);
        notifyDataSetChanged();
    }

    public interface OnMovieClickListener {
        void onMovieClicked(MovieDb movieDb);
    }

}
