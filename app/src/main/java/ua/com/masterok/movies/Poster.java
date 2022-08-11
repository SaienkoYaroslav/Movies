package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

// POJO
public class Poster {

    @SerializedName("url")
    private String url;

    public Poster(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
