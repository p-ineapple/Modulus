package com.example.modulus.Home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.R;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;

public class DateItemAdapter extends RecyclerView.Adapter<DateItemAdapter.MyViewHolder> {
    private List<LocalDate> dateList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LocalDate date);
    }

    public DateItemAdapter(List<LocalDate> dateList, OnItemClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textMonth, textDay, textYear;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textMonth = itemView.findViewById(R.id.text_month);
            textDay = itemView.findViewById(R.id.text_day);
            textYear = itemView.findViewById(R.id.text_year);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.date_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LocalDate date = dateList.get(position);

        // Format date components
        String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH); // e.g., "Apr"
        String dayNumber = String.valueOf(date.getDayOfMonth());
        String yearNumber = String.valueOf(date.getYear());

        // Set text in TextViews
        holder.textMonth.setText(monthName);
        holder.textDay.setText(dayNumber);
        holder.textYear.setText(yearNumber);

        // Handle click
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(date);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateList.size();
    }
}
