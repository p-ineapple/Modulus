package com.example.modulus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Model.ReviewModel;
import com.example.modulus.R;

import java.util.List;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {
    private List<ReviewModel> reviewList;

    public ReviewAdapter(List<ReviewModel> reviewList) {
        this.reviewList = reviewList;
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
        holder.reviewRatingBar.setRating(Float.valueOf(review.getRating()));
        holder.reviewScore.setText(review.getRating());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }
}

