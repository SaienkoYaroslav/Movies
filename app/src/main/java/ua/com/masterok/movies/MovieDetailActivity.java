package ua.com.masterok.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView tvTitle, tvYear, tvDescription;
    private RecyclerView rvTrailers;

    private MovieDetailViewModel movieDetailViewModel;
    private TrailersAdapter trailersAdapter;

    private static final String EXTRA_MOVIE = "movie";

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        init();
        intent();
        adapter();
        viewModel();

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
    }

    private void intent() {
        // При отриманні об'єкта Серіалайзбл, потрібно виконати явне перетворення типів
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
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
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

}