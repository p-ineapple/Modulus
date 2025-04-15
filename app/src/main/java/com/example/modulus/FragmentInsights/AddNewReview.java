package com.example.modulus.FragmentInsights;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.modulus.FragmentHome.DataBaseHelperHome;
import com.example.modulus.Model.ReviewModel;
import com.example.modulus.Model.ToDoModel;
import com.example.modulus.R;
import com.example.modulus.Utils.OnDialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddNewReview extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewReview";
    private RatingBar ratingBar;
    private EditText reviewInput;
    private Button submitReviewBtn;
    private DataBaseHelperReviews db;
    public static com.example.modulus.FragmentInsights.AddNewReview newInstance(){
        Log.d(TAG,"reviewInstance");
        return new com.example.modulus.FragmentInsights.AddNewReview();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.insights_add_review, container, false);
        Log.d(TAG, "New task");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ratingBar = view.findViewById(R.id.ratingBar);
        reviewInput = view.findViewById(R.id.commentInput);
        submitReviewBtn = view.findViewById(R.id.submitReviewBtn);

        db = new DataBaseHelperReviews(getActivity());

        submitReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ReviewModel item = new ReviewModel();
                item.setRating(String.valueOf(ratingBar.getRating()));
                item.setComment(reviewInput.getText().toString());
                item.setUsername("Bob");
                item.setModuleId(ModuleDetailsActivity.selectedModule.getId());
                db.insertTask(item);

                Log.d(TAG, "Save task");
                dismiss();
            }
        });


    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Log.d("Dismiss", "dismissed");
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (getParentFragment() instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) getParentFragment()).onDialogClose(dialog);
        } else if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }

}