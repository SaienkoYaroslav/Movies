package ua.com.masterok.movies;

import com.google.gson.annotations.SerializedName;

public class Review {

    @SerializedName("author")
    private String author;
    @SerializedName("type")
    private String type;
    @SerializedName("review")
    private String review;
    @SerializedName("title")
    private String title;

    public Review(String author, String type, String review, String title) {
        this.author = author;
        this.type = type;
        this.review = review;
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public String getType() {
        return type;
    }

    public String getReview() {
        return review;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Review{" +
                "author='" + author + '\'' +
                ", type='" + type + '\'' +
                ", review='" + review + '\'' +
                ", title='" + title + '\'' +
                '}';
    }

}
