package ua.com.masterok.movies;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MovieDetailViewModel extends AndroidViewModel {

    private static final String TAG = "MovieDetailViewModel";
    private final MutableLiveData<List<Trailers>> listTrailersMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Review>> listReviewsMutableLiveData = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    private final MovieDao movieDao;


    public LiveData<List<Trailers>> getListTrailersMutableLiveData() {
        return listTrailersMutableLiveData;
    }

    public MutableLiveData<List<Review>> getListReviewsMutableLiveData() {
        return listReviewsMutableLiveData;
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
        movieDao = MovieDatabase.getInstance(getApplication()).movieDao();
    }

    public void insertMovie(Movie movie) {
        Disposable disposable = movieDao.insertMovie(movie)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }

    public void removeMovie(int id) {
        Disposable disposable = movieDao.removeMovie(id)
                .subscribeOn(Schedulers.io())
                .subscribe();
        compositeDisposable.add(disposable);
    }


    public LiveData<Movie> getFavouriteMovie(int id) {
        return movieDao.getFavouriteMovie(id);
    }

    public void loadTrailers(int id) {
        Disposable disposable = ApiFactory.apiService.loadTrailers(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Function<TrailerResponseFromServer, List<Trailers>>() {
                    @Override
                    public List<Trailers> apply(TrailerResponseFromServer trailerResponseFromServer) throws Throwable {
                        return trailerResponseFromServer.getVideos().getTrailers();
                    }
                })
                .subscribe(new Consumer<List<Trailers>>() {
                    @Override
                    public void accept(List<Trailers> trailers) throws Throwable {
                        listTrailersMutableLiveData.setValue(trailers);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    public void loadReviews(int id) {
        Disposable disposable = ApiFactory.apiService.loadReviews(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<ReviewResponseFromServer>() {
                    @Override
                    public void accept(ReviewResponseFromServer reviewResponseFromServer) throws Throwable {
                        listReviewsMutableLiveData.setValue(reviewResponseFromServer.getReview());
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Throwable {
                        Log.d(TAG, throwable.toString());
                    }
                });
        compositeDisposable.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
