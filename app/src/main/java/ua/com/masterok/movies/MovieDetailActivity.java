package ua.com.masterok.movies;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class MovieDetailActivity extends AppCompatActivity {

    private ImageView imageViewPoster;
    private TextView tvTitle, tvYear, tvDescription;

    private static final String EXTRA_MOVIE = "movie";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        init();
        intent();

    }

    private void intent() {
        // При отриманні об'єкта Серіалайзбл, потрібно виконати явне перетворення типів
        Movie movie = (Movie) getIntent().getSerializableExtra(EXTRA_MOVIE);
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
    }

    public static Intent newIntent(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(EXTRA_MOVIE, movie);
        return intent;
    }

}