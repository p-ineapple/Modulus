package com.example.modulus.Home;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

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

import com.example.modulus.Adapter.ToDoAdaptor;
import com.example.modulus.Class.ToDoModel;
import com.example.modulus.R;
import com.example.modulus.Utils.DataBaseHelper;
import com.example.modulus.Utils.OnDialogCloseListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HomeFragment extends Fragment implements OnDialogCloseListener {
    RecyclerView recyclerView;
    FloatingActionButton addButton;
    DataBaseHelper myDB;
    Button testButton;
    private List<ToDoModel> mList;
    private ToDoAdaptor adaptor;

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
        addButton = view.findViewById(R.id.addButton); //ToDo: edit these
        myDB = new DataBaseHelper(this.getContext());
        mList = new ArrayList<>();
        adaptor = new ToDoAdaptor(myDB, this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setAdapter(adaptor);

        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adaptor.setTasks(mList);
        testButton = view.findViewById(R.id.testbutton);
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LocalDate date = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Change format as needed
                String dateString = date.format(formatter);

                System.out.println(dateString);
                recyclerView.setAdapter(adaptor);
                mList = myDB.getDateTask("16-4-2025");
                Collections.reverse(mList);
                adaptor.setTasks(mList);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewTask.newInstance().show(getParentFragmentManager(), AddNewTask.TAG);

            }
        });

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerViewTouchHelper(adaptor)); // for delete and edit
        itemTouchHelper.attachToRecyclerView(recyclerView);
        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        mList = myDB.getAllTasks();
        Collections.reverse(mList);
        adaptor.setTasks(mList);
        adaptor.notifyDataSetChanged();

    }
}