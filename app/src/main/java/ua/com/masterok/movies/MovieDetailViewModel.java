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
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();


    public LiveData<List<Trailers>> getListTrailersMutableLiveData() {
        return listTrailersMutableLiveData;
    }

    public MovieDetailViewModel(@NonNull Application application) {
        super(application);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        compositeDisposable.dispose();
    }
}
