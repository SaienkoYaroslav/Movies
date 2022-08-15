package ua.com.masterok.movies;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    private List<Movie> movies = new ArrayList<>();
    private OnReachEndListener onReachEndListener;
    private OnClickMovieListener onClickMovieListener;

    public void setOnReachEndListener(OnReachEndListener onReachEndListener) {
        this.onReachEndListener = onReachEndListener;
    }

    public void setOnClickMovieListener(OnClickMovieListener onClickMovieListener) {
        this.onClickMovieListener = onClickMovieListener;
    }

    public void setMovies(List<Movie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        Movie movie = movies.get(position);
        Glide.with(holder.itemView)
                .load(movie.getPoster().getUrl())
                .into(holder.ivPoster);

        double rating = movie.getRating().getImdb();
        int backgroundId;
        if (rating > 7) {
            backgroundId = R.drawable.circle_green;
        } else if (rating > 5) {
            backgroundId = R.drawable.circle_orange;
        } else {
            backgroundId = R.drawable.circle_red;
        }
        // отримуємо фон
        Drawable background = ContextCompat.getDrawable(holder.itemView.getContext(), backgroundId);
        holder.tvRating.setBackground(background);
        holder.tvRating.setText(String.valueOf(rating));

        // завантаження сторінки далі при долистуванні до кінця списку
        if (position >= movies.size() - 10 && onReachEndListener != null) {
            onReachEndListener.onReachEnd();
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickMovieListener != null) {
                    onClickMovieListener.onClickMovie(movie);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    interface OnReachEndListener {

        void onReachEnd();
    }

    interface OnClickMovieListener {

        void onClickMovie(Movie movie);
    }

    static class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ImageView ivPoster;
        private final TextView tvRating;

        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            ivPoster = itemView.findViewById(R.id.image_view_poster);
            tvRating = itemView.findViewById(R.id.text_view_rating);
        }
    }

}
