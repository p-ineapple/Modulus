package com.example.modulus.FragmentHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Adapter.DateItemAdapter;
import com.example.modulus.Adapter.ToDoAdapter;
import com.example.modulus.Utils.MergeSort;
import com.example.modulus.Model.ToDoModel;
import com.example.modulus.R;
import com.example.modulus.Utils.OnDialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.TextStyle;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class HomeFragment extends Fragment implements OnDialogCloseListener {
    private RecyclerView taskRecyclerView, dateItemRecycler;
    private DataBaseHelperHome myDB;
    private Button allButton, toDoButton, completedButton;

    private List<ToDoModel> mList;
    private ToDoAdapter toDoAdapter;
    private final Calendar calendar = Calendar.getInstance();
    private final int year = calendar.get(Calendar.YEAR);
    private final int month = calendar.get(Calendar.MONTH) + 1;
    private final int day = calendar.get(Calendar.DAY_OF_MONTH);
    private final String currentDate = day + "/" + month + "/" + year;
    private final MergeSort sortTime = new MergeSort(ToDoModel.timeCompare);

    public String clickedDate = currentDate;
    private final String TAG = "Home Fragment";
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        insertTasksFromJson(this.getContext());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.homeMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Set Title to current date, convert currentDate string to a LocalDate
        TextView homeTitle = view.findViewById(R.id.homeTitle);
        DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("d/M/yyyy");
        LocalDate todayDate = LocalDate.parse(currentDate, inputFormatter);
        Log.d(TAG, "date formatted");

        // Extract day of week, day of month, and month name, set display text
        DayOfWeek dayOfWeek = todayDate.getDayOfWeek();
        int dayOfMonth = todayDate.getDayOfMonth();
        String monthName = todayDate.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH); // "April"
        String displayText = String.format("Welcome! It's %s %d %s", dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH), dayOfMonth, monthName);
        homeTitle.setText(displayText);
        Log.d(TAG, "home title set");

        //Date Items Recycler
        dateItemRecycler = view.findViewById(R.id.dateItemRecycler);
        ScaleCenterItemManager layoutManager = new ScaleCenterItemManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        dateItemRecycler.setLayoutManager(layoutManager);
        generateData();

        //Task Recycler
        taskRecyclerView = view.findViewById(R.id.recyclerView);
        myDB = new DataBaseHelperHome(this.getContext());
        toDoAdapter = new ToDoAdapter(myDB, this);
        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        taskRecyclerView.setAdapter(toDoAdapter);
//        mList = sort(myDB.getDateTask(currentDate)); // Adjust format if needed
//        toDoAdapter.setTasks(mList);
//        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(toDoAdapter));
//        itemTouchHelper.attachToRecyclerView(taskRecyclerView);

        //Add Button
        FloatingActionButton addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getChildFragmentManager(), AddNewTask.TAG);
                Log.d("addButton","AddNewTask instance");
            }
        });

        //Buttons
        //String clickedDate = currentDate;
        allButton = view.findViewById(R.id.AllButton);
        allButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList = sort(myDB.getDateTask(clickedDate)); // Adjust format if needed
                toDoAdapter.setTasks(mList);
                highlightSelectedButton(allButton, toDoButton, completedButton);
            }
        });

        toDoButton = view.findViewById(R.id.ToDoButton);
        toDoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList = sort(myDB.getStatustask(0,clickedDate)); // Adjust format if needed
                toDoAdapter.setTasks(mList);
                highlightSelectedButton(toDoButton, allButton, completedButton);
            }
        });

        completedButton = view.findViewById(R.id.CompletedButton);
        completedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mList = sort(myDB.getStatustask(1, clickedDate)); // Adjust format if needed
                HomeFragment.this.toDoAdapter.setTasks(mList);
                highlightSelectedButton(completedButton, allButton, toDoButton);
            }
        });

        highlightSelectedButton(allButton, toDoButton, completedButton);

        // Inflate the layout for this fragment
        return view;
    }

    private void generateData() {
        List<LocalDate> dateList = new ArrayList<>();
        LocalDate today = LocalDate.now();
        int currentYear = today.getYear();
        int currentMonth = today.getMonthValue();
        LocalDate firstDayOfMonth = LocalDate.of(currentYear, currentMonth, 1);
        int daysInMonth = firstDayOfMonth.lengthOfMonth();

        // Add all dates of the current month
        for (int i = 1; i <= daysInMonth; i++) {
            dateList.add(LocalDate.of(currentYear, currentMonth, i));
        }

        DateItemAdapter adapter = new DateItemAdapter(dateList, new DateItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(LocalDate date) {
                // Handle the click event (same logic as testButton)
                //System.out.println("Selected date: " + date.toString());
                String getDate = date.format(DateTimeFormatter.ofPattern("d/M/yyyy"));
                clickedDate = getDate;
                taskRecyclerView.setAdapter(HomeFragment.this.toDoAdapter);
                mList = sort(myDB.getDateTask(getDate)); // Adjust format if needed
                HomeFragment.this.toDoAdapter.setTasks(mList);
            }
        });

        dateItemRecycler.setAdapter(adapter);
        Log.d(TAG, "adapter set");

        // Scroll to current date when opening
        int todayPosition = today.getDayOfMonth() - 1;
        dateItemRecycler.scrollToPosition(todayPosition-2);
        Log.d(TAG, "current date view");
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        Log.d("Home", "onDialogClose");
        mList = sort(myDB.getDateTask(clickedDate));
        toDoAdapter.setTasks(mList);
        toDoAdapter.notifyDataSetChanged();

    }

    private void highlightSelectedButton(Button selected, Button... others) {
        selected.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.dark_purple));
        selected.setTextColor(ContextCompat.getColor(getContext(), R.color.white));

        for (Button btn : others) {
            btn.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.light_purple));
            btn.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
        }
    }


    private void insertTasksFromJson(Context context) {
        DataBaseHelperHome dbHelper = new DataBaseHelperHome(context);
        try {
            // Load JSON from assets
            InputStream is = context.getAssets().open("school_tasks.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            // Convert to JSON string
            String json = new String(buffer, StandardCharsets.UTF_8);

            // Parse JSON array
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject taskObject = jsonArray.getJSONObject(i);

                ToDoModel model = new ToDoModel();
                model.setTask(taskObject.getString("task"));
                model.setStatus(taskObject.getInt("status"));
                model.setDate(taskObject.getString("date"));
                model.setTime(taskObject.getString("time"));
                model.setCategory(taskObject.getString("category"));

                dbHelper.insertTask(model);
            }

            Log.d(TAG, "All tasks inserted successfully");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error inserting tasks: " + e.getMessage());
        }
    }

    private List<ToDoModel> sort(List<ToDoModel> list){
        List<ToDoModel> status1 = new ArrayList<>();
        List<ToDoModel> status2 = new ArrayList<>();
        for(ToDoModel task: list){
            if(task.getStatus() == 0){
                status1.add(task);
            }else{
                status2.add(task);
            }
        }
        sortTime.mergeSort(status1, status1.size());
        sortTime.mergeSort(status2, status2.size());
        status1.addAll(status2);
        return status1;
    }

}