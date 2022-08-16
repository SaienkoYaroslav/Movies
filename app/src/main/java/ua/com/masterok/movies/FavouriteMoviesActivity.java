package ua.com.masterok.movies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavouriteMoviesActivity extends AppCompatActivity {

    private RecyclerView rvFavouriteMovies;
    private MoviesAdapter favouriteMovieAdapter;
    private FavouriteMoviesViewModel favouriteMoviesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite_movies);
        init();
        adapter();
        viewModel();
    }

    private void viewModel() {
        favouriteMoviesViewModel = new ViewModelProvider(this).get(FavouriteMoviesViewModel.class);
        favouriteMoviesViewModel.getMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                favouriteMovieAdapter.setMovies(movies);
            }
        });
    }

    private void adapter() {
        favouriteMovieAdapter = new MoviesAdapter();
        rvFavouriteMovies.setAdapter(favouriteMovieAdapter);
        rvFavouriteMovies.setLayoutManager(new GridLayoutManager(getApplicationContext(), 2));
        favouriteMovieAdapter.setOnClickMovieListener(new MoviesAdapter.OnClickMovieListener() {
            @Override
            public void onClickMovie(Movie movie) {
                Intent intent = MovieDetailActivity.newIntent(
                        FavouriteMoviesActivity.this,
                        movie
                );
                startActivity(intent);
            }
        });
    }

    private void init() {
        rvFavouriteMovies = findViewById(R.id.recycler_view_favourite_movies);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, FavouriteMoviesActivity.class);
    }

}