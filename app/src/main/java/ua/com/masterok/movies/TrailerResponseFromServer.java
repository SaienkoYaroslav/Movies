package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

public class TrailerResponseFromServer {

    @SerializedName("videos")
    private Videos videos;

    public TrailerResponseFromServer(Videos videos) {
        this.videos = videos;
    }

    public Videos getVideos() {
        return videos;
    }

    public void setVideos(Videos videos) {
        this.videos = videos;
    }
}
