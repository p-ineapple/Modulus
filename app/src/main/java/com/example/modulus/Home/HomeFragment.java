package com.example.modulus.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.content.DialogInterface;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.modulus.Adapter.ToDoAdapter;
import com.example.modulus.Class.ToDoModel;
import com.example.modulus.R;
import com.example.modulus.Utils.OnDialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements OnDialogCloseListener {
    RecyclerView recyclerView, dateItemRecycler;
    FloatingActionButton addButton;
    DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adaptor;
    private final String TAG_ = "Home";
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        ViewCompat.setOnApplyWindowInsetsListener(view.findViewById(R.id.homeMain), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = view.findViewById(R.id.recyclerView);
        dateItemRecycler = view.findViewById(R.id.dateItemRecycler);
        //LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        ScaleCenterItemManager layoutManager = new ScaleCenterItemManager(this.getContext(),LinearLayoutManager.HORIZONTAL,false);
        dateItemRecycler.setLayoutManager(layoutManager);
        generateData();
        /*mAdapter.setOnItemClickListener(new ExampleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String title = exampleList.get(position).getText1();

            }
        });*/

        addButton = view.findViewById(R.id.addButton); //ToDo: edit these
        myDB = new DataBaseHelper(this.getContext());
        mList = new ArrayList<>();
        adaptor = new ToDoAdapter(myDB, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adaptor);

        mList = myDB.getDateTask("16-4-2025");
        Collections.reverse(mList);
        adaptor.setTasks(mList);
        /*
        testButton = view.findViewById(R.id.testbutton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Change format as needed
                String dateString = date.format(formatter);

                System.out.println(dateString);
                recyclerView.setAdapter(adapter);
                mList = myDB.getAllTasks();
                //mList = myDB.getDateTask("16-4-2025");
                Collections.reverse(mList);
                adapter.setTasks(mList);
            }
        });*/

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getParentFragmentManager(), AddNewTask.TAG);
            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adaptor)); // for delete and edit
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // Inflate the layout for this fragment
        Log.d(TAG_, "Home fragment inflated");
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

                recyclerView.setAdapter(adaptor);
                mList = myDB.getDateTask(date.format(DateTimeFormatter.ofPattern("d-M-yyyy"))); // Adjust format if needed
                Collections.reverse(mList);
                adaptor.setTasks(mList);
            }
        });

        dateItemRecycler.setAdapter(adapter);

        // Scroll to current date when opening
        int todayPosition = today.getDayOfMonth() - 1;
        dateItemRecycler.scrollToPosition(todayPosition);
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adaptor.setTasks(mList);
        adaptor.notifyDataSetChanged();
    }
}