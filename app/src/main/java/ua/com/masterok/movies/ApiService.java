package ua.com.masterok.movies;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("movie?token=9J87GP5-QT147CF-J3FDMKJ-GH8132M&field=rating.imdb&search=6-10&sortField=votes.imdb&sortType=-1&limit=30")
    // @Query("page") - так можна динамічно додавати параметри запита в сам запит
    // до ендпоінта додасться &page=int page
    Single<MovieResponseFromServer> loadMovies(@Query("page") int page);

    @GET("movie?token=9J87GP5-QT147CF-J3FDMKJ-GH8132M&field=id")
    Single<TrailerResponseFromServer> loadTrailers(@Query("search") int id);

}
