package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Videos {

    @SerializedName("trailers")
    private List<Trailers> trailers;

    public Videos(List<Trailers> trailers) {
        this.trailers = trailers;
    }

    public List<Trailers> getTrailers() {
        return trailers;
    }

    public void setTrailers(List<Trailers> trailers) {
        this.trailers = trailers;
    }
}
