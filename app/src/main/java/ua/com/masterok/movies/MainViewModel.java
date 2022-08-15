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
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class MainViewModel extends AndroidViewModel {

    private static final String TAG = "MainViewModel";
    private final MutableLiveData<List<Movie>> movieMutableLiveData = new MutableLiveData<>();
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);

    private int page = 1;

    public MainViewModel(@NonNull Application application) {
        super(application);
        // цей метод викликається тут а не у мейн Активіті, для того щоб не заморочуватись з переворотом
        // екрану, так як ВьюМодел переживає переворот, а Активіті знищується і створюється заново
        loadMovies();
    }

    public LiveData<List<Movie>> getMovieMutableLiveData() {
        return movieMutableLiveData;
    }

    public LiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    public void loadMovies() {
        // Перевірка, щоб дані не завантажувались багато разів. Не завантажувались, коли йде завантаження,
        // тобто коли цей метод вже викликаний
        Boolean loading = isLoading.getValue();
        if (loading != null && loading) {
            return;
        }

        Disposable disposable = ApiFactory.apiService.loadMovies(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(new Consumer<Disposable>() {
                    @Override
                    public void accept(Disposable disposable) throws Throwable {
                        isLoading.setValue(true);
                    }
                })
                .doAfterTerminate(new Action() {
                    @Override
                    public void run() throws Throwable {
                        isLoading.setValue(false);
                    }
                })
                .subscribe(new Consumer<MovieResponseFromServer>() {
                    @Override
                    public void accept(MovieResponseFromServer movieResponseFromServer) throws Throwable {
                        List<Movie> loadedMovies = movieMutableLiveData.getValue();
                        if (loadedMovies != null) {
                            loadedMovies.addAll(movieResponseFromServer.getMovies());
                            movieMutableLiveData.setValue(loadedMovies);
                        } else {
                            movieMutableLiveData.setValue(movieResponseFromServer.getMovies());
                        }
                        page++;
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
