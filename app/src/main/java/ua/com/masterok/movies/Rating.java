package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

// POJO
public class Rating implements Serializable {

    @SerializedName("imdb")
    private double imdb;

    public Rating(double imdb) {
        this.imdb = imdb;
    }

    public double getImdb() {
        return imdb;
    }

    @Override
    public String toString() {
        return "Rating{" +
                "imdb='" + imdb + '\'' +
                '}';
    }
}
