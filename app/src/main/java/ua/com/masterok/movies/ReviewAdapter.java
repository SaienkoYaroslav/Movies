package ua.com.masterok.movies;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<Review> reviews = new ArrayList<>();

    private static final String TYPE_REVIEW_POSITIVE = "Позитивный";
    private static final String TYPE_REVIEW_NEUTRAL = "Нейтральный";
    private static final String TYPE_REVIEW_NEGATIVE = "Негативный";

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        holder.tvAuthor.setText(review.getAuthor());
        holder.tvTitle.setText(review.getTitle());
        holder.tvReview.setText(review.getReview());

        int colorResId;
        String typeReview = review.getType();
        if (typeReview.equals(TYPE_REVIEW_POSITIVE)) {
            colorResId = android.R.color.holo_green_light;
        } else if (typeReview.equals(TYPE_REVIEW_NEGATIVE)) {
            colorResId = android.R.color.holo_red_light;
        } else {
            colorResId = android.R.color.holo_orange_light;
        }
        int color = ContextCompat.getColor(holder.itemView.getContext(), colorResId);
        holder.constraintLayoutReview.setBackgroundColor(color);

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    static class ReviewViewHolder extends RecyclerView.ViewHolder {

        private final TextView tvReview, tvAuthor, tvTitle;
        private final ConstraintLayout constraintLayoutReview;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvReview = itemView.findViewById(R.id.text_view_review);
            tvAuthor = itemView.findViewById(R.id.text_view_review_author);
            tvTitle = itemView.findViewById(R.id.text_view_review_title);
            constraintLayoutReview = itemView.findViewById(R.id.constraint_layout_review);
        }
    }

}
