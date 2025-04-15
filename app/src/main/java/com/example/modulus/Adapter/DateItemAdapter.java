package com.example.modulus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private LocalDate selectedDate;

    public interface OnItemClickListener {
        void onItemClick(LocalDate date);
    }

    public DateItemAdapter(List<LocalDate> dateList, OnItemClickListener listener) {
        this.dateList = dateList;
        this.listener = listener;
        this.selectedDate = LocalDate.now(); // Set default selected date to today
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textMonth, textDay, textYear;
        LinearLayout dateItemLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textMonth = itemView.findViewById(R.id.text_month);
            textDay = itemView.findViewById(R.id.text_day);
            textYear = itemView.findViewById(R.id.text_year);
            dateItemLayout = itemView.findViewById(R.id.dateItemLayout);
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_date_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LocalDate date = dateList.get(position);

        String monthName = date.getMonth().getDisplayName(TextStyle.SHORT, Locale.ENGLISH);
        String dayNumber = String.valueOf(date.getDayOfMonth());
        String yearNumber = String.valueOf(date.getYear());

        holder.textMonth.setText(monthName);
        holder.textDay.setText(dayNumber);
        holder.textYear.setText(yearNumber);

        // Check if this item is the selected date
        if (date.equals(selectedDate)) {
            holder.dateItemLayout.setBackgroundColor(0xFF7009E0); // Purple background
            holder.textMonth.setTextColor(0xFFFFFFFF); // White text
            holder.textDay.setTextColor(0xFFFFFFFF);
            holder.textYear.setTextColor(0xFFFFFFFF);
        } else {
            holder.dateItemLayout.setBackgroundColor(0xFFFFFFFF); // White background
            holder.textMonth.setTextColor(0xFF000000); // Black text
            holder.textDay.setTextColor(0xFF000000);
            holder.textYear.setTextColor(0xFF000000);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!date.equals(selectedDate)) {
                LocalDate oldDate = selectedDate;
                selectedDate = date;
                notifyItemChanged(dateList.indexOf(oldDate));
                notifyItemChanged(position);
            }
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
