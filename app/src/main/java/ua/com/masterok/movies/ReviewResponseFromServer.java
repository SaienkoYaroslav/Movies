package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ReviewResponseFromServer {

    @SerializedName("docs")
    private List<Review> review;

    public ReviewResponseFromServer(List<Review> review) {
        this.review = review;
    }

    public List<Review> getReview() {
        return review;
    }

    @Override
    public String toString() {
        return "ReviewResponseFromServer{" +
                "review=" + review +
                '}';
    }
}
