package com.example.modulus.Adapter;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Model.ReviewModel;
import com.example.modulus.R;

import java.util.List;
// RecyclerView Adapter for reviews in ModulesDetailsActivity
public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private final List<ReviewModel> reviewList;
    private final int reviewScoreColor;

    public ReviewAdapter(List<ReviewModel> reviewList, int reviewScoreColor) {
        this.reviewList = reviewList;
        this.reviewScoreColor = reviewScoreColor;
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView reviewUser, reviewDescription, reviewScore;
        RatingBar reviewRatingBar;

        public ReviewViewHolder(View itemView) {
            super(itemView);
            reviewUser = itemView.findViewById(R.id.reviewUser);
            reviewDescription = itemView.findViewById(R.id.reviewDescription);
            reviewRatingBar = itemView.findViewById(R.id.reviewRatingBar);
            reviewScore = itemView.findViewById(R.id.reviewScore);

        }
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.insights_review_layout, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReviewViewHolder holder, int position) {
        ReviewModel review = reviewList.get(position);
        holder.reviewUser.setText(review.getUsername());
        holder.reviewDescription.setText(review.getComment());
        holder.reviewRatingBar.setRating(Float.parseFloat(review.getRating()));
        holder.reviewScore.setText(review.getRating());

        ColorStateList csl = ColorStateList.valueOf(reviewScoreColor);
        holder.reviewRatingBar.setProgressTintList(csl);
        holder.reviewRatingBar.setProgressBackgroundTintList(csl);
        holder.reviewRatingBar.setSecondaryProgressTintList(csl);

    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

