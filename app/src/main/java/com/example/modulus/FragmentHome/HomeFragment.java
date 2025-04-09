package com.example.modulus.FragmentHome;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;
import android.widget.Button;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Adapter.DateItemAdapter;
import com.example.modulus.Adapter.ToDoAdapter;
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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements OnDialogCloseListener {
    RecyclerView taskRecyclerView, dateItemRecycler;
    FloatingActionButton addButton;
    DataBaseHelperHome myDB;
    Button testButton;
    private List<ToDoModel> mList;
    private ToDoAdapter toDoAdapter;
    final Calendar calendar = Calendar.getInstance();
    final int year = calendar.get(Calendar.YEAR);
    final int month = calendar.get(Calendar.MONTH) + 1;
    final int day = calendar.get(Calendar.DAY_OF_MONTH);
    final String currentDate = day + "/" + month + "/" + year;


    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //insertTasksFromJson(this.getContext());
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.homeMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        taskRecyclerView = view.findViewById(R.id.recyclerView);

        dateItemRecycler = view.findViewById(R.id.dateItemRecycler);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext(), LinearLayoutManager.HORIZONTAL, false);
        ScaleCenterItemManager layoutManager = new ScaleCenterItemManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        dateItemRecycler.setLayoutManager(layoutManager);
        generateData();



        addButton = view.findViewById(R.id.addButton); //ToDo: edit these
        myDB = new DataBaseHelperHome(this.getContext());
        mList = new ArrayList<>();
        toDoAdapter = new ToDoAdapter(myDB, this);
        /*
        mList = myDB.getDateTask(currentDate);
        Collections.reverse(mList);
        toDoAdapter.setTasks(mList);
        Collections.reverse(mList);
        HomeFragment.this.toDoAdapter.setTasks(mList); */

        taskRecyclerView.setHasFixedSize(true);
        taskRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        //taskRecyclerView.setAdapter(toDoAdapter);

        taskRecyclerView.setAdapter(HomeFragment.this.toDoAdapter);
        mList = myDB.getDateTask(currentDate); // Adjust format if needed
        Collections.reverse(mList);
        HomeFragment.this.toDoAdapter.setTasks(mList);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getParentFragmentManager(), AddNewTask.TAG);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(toDoAdapter)); // for delete and edit
        itemTouchHelper.attachToRecyclerView(taskRecyclerView);
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
                System.out.println("Selected date: " + date.toString());

                taskRecyclerView.setAdapter(HomeFragment.this.toDoAdapter);
                mList = myDB.getDateTask(date.format(DateTimeFormatter.ofPattern("d/M/yyyy"))); // Adjust format if needed
                Collections.reverse(mList);
                HomeFragment.this.toDoAdapter.setTasks(mList);
            }
        });

        dateItemRecycler.setAdapter(adapter);

        // Scroll to current date when opening
        int todayPosition = today.getDayOfMonth() - 1;
        dateItemRecycler.scrollToPosition(todayPosition-2);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        toDoAdapter.setTasks(mList);
        toDoAdapter.notifyDataSetChanged();
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

            Log.d("DB_INSERT", "All tasks inserted successfully");

        } catch (IOException | JSONException e) {
            e.printStackTrace();
            Log.e("DB_INSERT", "Error inserting tasks: " + e.getMessage());
        }
    }


}