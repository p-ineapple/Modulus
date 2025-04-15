package com.example.modulus.FragmentHome;

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
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.modulus.Model.ToDoModel;
import com.example.modulus.R;
import com.example.modulus.Utils.OnDialogCloseListener;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Calendar;

public class AddNewTask extends BottomSheetDialogFragment {
    public static final String TAG = "AddNewTask";
    private EditText addTask, addCategory;
    private TextView setDueDate, setDueTime;
    private Button saveButton;
    private DataBaseHelperHome myDB;
    private DatePickerDialog datePickerDialog;
    private Button dateButton;
    public static AddNewTask newInstance(){
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.home_add_new_task, container, false);
        Log.d(TAG, "New task");
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        addTask = view.findViewById(R.id.addTaskText);
        addCategory = view.findViewById(R.id.addCatText);
        saveButton = view.findViewById(R.id.saveButton);
        setDueDate = view.findViewById(R.id.addDateText);
        setDueTime = view.findViewById(R.id.addTimeText );
        final Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);
        int currentMinute = calendar.get(Calendar.MINUTE);
        //initDatePicker();
        //dateButton = view.findViewById(R.id.datePickerButton);
        //dateButton.setText(getTodaysDate());

        myDB = new DataBaseHelperHome(getActivity());
        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle!=null){
            isUpdate = true;
            String task = bundle.getString("task");
            addTask.setText(task);
            String category = bundle.getString("category");
            addCategory.setText(category);
            String date = bundle.getString("date");
            setDueDate.setText(date);
            String time = bundle.getString("time");
            setDueTime.setText(time);

            if (task.length() > 0){
                saveButton.setEnabled(false);
            }
        }

        addTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                }else{
                    saveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        addCategory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().isEmpty()){
                    saveButton.setEnabled(false);
                    saveButton.setBackgroundColor(Color.GRAY);
                }else{
                    saveButton.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


        boolean finalIsUpdate = isUpdate;
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = addTask.getText().toString();
                String date_text = setDueDate.getText().toString();
                String category = addCategory.getText().toString();
                String time = setDueTime.getText().toString();

                if (finalIsUpdate){
                    myDB.updateTask(bundle.getInt("ID"),text,date_text,category,time);
                    //myDB.updateTask(bundle.getInt("ID"),text);
                }else{
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setDate(date_text);
                    item.setStatus(0);
                    item.setCategory(category);
                    item.setTime(time);
                    myDB.insertTask(item);
                }
                Log.d(TAG, "Save task");
                dismiss();
            }
        });

        setDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        month++;
                        String date = dayOfMonth + "/" + month + "/" + year;
                        setDueDate.setText(date);
                    }
                },year, month, day);
                dialog.show();
            }
        });

        setDueTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog(requireContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String meridiem = "AM";
                        String zero = "";
                        if(hourOfDay > 12){
                            hourOfDay -= 12;
                            meridiem = "PM";
                        }
                        if(minute < 10){
                            zero = "0";
                        }
                        String time = hourOfDay + ":" + zero + minute + " " + meridiem;
                        setDueTime.setText(time);
                    }
                }, currentHour, currentMinute,false);
                dialog.show();
            }
        });

    }


    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        Log.d("Dismiss", "dismissed");
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (getParentFragment() instanceof OnDialogCloseListener){
            Log.d("Dismiss", "activity");
            ((OnDialogCloseListener)getParentFragment()).onDialogClose(dialog);
        }else if (activity instanceof OnDialogCloseListener) {
            ((OnDialogCloseListener) activity).onDialogClose(dialog);
        }
    }



    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }
}