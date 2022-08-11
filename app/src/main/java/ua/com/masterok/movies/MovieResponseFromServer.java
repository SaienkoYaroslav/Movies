package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

// POJO - клас в який трансформується відповідь з сервера (джейсон)
public class MovieResponseFromServer {

    // @SerializedName - потрібно оголошувати скрізь де ми працюємо з інтернетом, так як після того,
    // як аппка викладеться в плейМаркет буде обфускація (у всіх змінних зміниться ім'я)

    // @SerializedName - оголошується ключ по якому отримуються дані з джейсона
    // інший варіант назвати змінну ключем, тобто не movies, а docs
    @SerializedName("docs")
    private List<Movie> movies;

    public MovieResponseFromServer(List<Movie> movies) {
        this.movies = movies;
    }

    public List<Movie> getMovies() {
        return movies;
    }
}
