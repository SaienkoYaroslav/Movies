package ua.com.masterok.movies;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster, imageViewStar;
    private TextView tvTitle, tvYear, tvDescription;
    private RecyclerView rvTrailers, rvReviews;

    private MovieDetailViewModel movieDetailViewModel;
    private TrailersAdapter trailersAdapter;
    private ReviewAdapter reviewAdapter;

    private Movie movie;

    private Drawable starOff, starOn;

    private static final String EXTRA_MOVIE = "movie";

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        init();
        drawable();
        intent();
        adapter();
        viewModel();

    }

    private void drawable() {
        starOff = ContextCompat.getDrawable(MovieDetailActivity.this, android.R.drawable.star_big_off);
        starOn = ContextCompat.getDrawable(MovieDetailActivity.this, android.R.drawable.star_big_on);
    }

    private void adapter() {
        trailersAdapter = new TrailersAdapter();
        rvTrailers.setAdapter(trailersAdapter);
        rvTrailers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        trailersAdapter.setOnTrailerClickListener(new TrailersAdapter.OnTrailerClickListener() {
            @Override
            public void onTrailerClick(Trailers trailer) {
                // неявний інтент. ACTION_VIEW - екшн для відображення адреси в інтернеті
                Intent intent = new Intent(Intent.ACTION_VIEW);
                // парсим строку в посилання (Url)
                intent.setData(Uri.parse(trailer.getUrl()));
                startActivity(intent);
            }
        });

        reviewAdapter = new ReviewAdapter();
        rvReviews.setAdapter(reviewAdapter);
        rvReviews.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    }

    private void viewModel() {
        movieDetailViewModel = new ViewModelProvider(this).get(MovieDetailViewModel.class);
        movieDetailViewModel.loadTrailers(id);
        movieDetailViewModel.getListTrailersMutableLiveData().observe(this, new Observer<List<Trailers>>() {
            @Override
            public void onChanged(List<Trailers> trailers) {
                trailersAdapter.setTrailers(trailers);
            }
        });
        movieDetailViewModel.loadReviews(id);
        movieDetailViewModel.getListReviewsMutableLiveData().observe(this, new Observer<List<Review>>() {
            @Override
            public void onChanged(List<Review> reviews) {
                reviewAdapter.setReviews(reviews);
            }
        });
        movieDetailViewModel.getFavouriteMovie(id).observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movieFromDb) {
                if (movieFromDb == null) {
                    imageViewStar.setImageDrawable(starOff);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            movieDetailViewModel.insertMovie(movie);
                        }
                    });
                } else {
                    imageViewStar.setImageDrawable(starOn);
                    imageViewStar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            movieDetailViewModel.removeMovie(id);
                        }
                    });
                }
            }
        });
    }

    private void intent() {
        // При отриманні об'єкта Серіалайзбл, потрібно виконати явне перетворення типів
        movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
        id = movie.getId();
        Glide.with(this)
                .load(movie.getPoster().getUrl())
                .into(imageViewPoster);
        tvTitle.setText(movie.getName());
        tvYear.setText(String.valueOf(movie.getYear()));
        tvDescription.setText(movie.getDescription());
    }

    private void init() {
        imageViewPoster = findViewById(R.id.image_view_detail_poster);
        tvTitle = findViewById(R.id.text_view_title);
        tvYear = findViewById(R.id.text_view_year);
        tvDescription = findViewById(R.id.text_view_description);
        rvTrailers = findViewById(R.id.recycler_view_trailers);
        rvReviews = findViewById(R.id.recycler_view_reviews);
        imageViewStar = findViewById(R.id.image_view_star);
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

}