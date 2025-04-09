package com.example.modulus.FragmentCalendar;

import static com.example.modulus.R.id.dayView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.modulus.R;
import com.framgia.library.calendardayview.CalendarDayView;
import com.framgia.library.calendardayview.DayView;
import com.framgia.library.calendardayview.data.IEvent;
import com.framgia.library.calendardayview.decoration.CdvDecorationDefault;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;




public class CalendarFragment extends Fragment {


    private CalendarView calendarView;
    private CalendarDayView hourlyDayView;

    private ImageButton addEventButton;
    private TextView selectedDateText;
    private ArrayList<Event> allEvents;
    final String TAG = "Calendar";


    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);


        //set up modules list
        if (allEvents == null || allEvents.isEmpty() )  {
            Log.d(TAG, "Setting up modules");
            setupData();
        }
        Log.d(TAG, "Database set up");

        //Initialisation
        calendarView = view.findViewById(R.id.calendar);
        hourlyDayView = view.findViewById(dayView);
//        addEventButton = view.findViewById(R.id.addEventButton);
        selectedDateText = view.findViewById(R.id.selectedDateText);

        //Override library time format for nicer readability
        hourlyDayView.setDecorator(new CdvDecorationDefault(getContext()) {
            @Override
            public DayView getDayView(int hour) {
                DayView dayView = new DayView(getContext());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);
                SimpleDateFormat sdf = new SimpleDateFormat("h a");
                dayView.setText(sdf.format(calendar.getTime()).toUpperCase());
                return dayView;
            }
        });


        //Default event shown to be today's date
        Calendar today = Calendar.getInstance();
        String todayFormatted = android.text.format.DateFormat.format("dd MMMM", today.getTime()).toString();
        selectedDateText.setText("Schedule On " + todayFormatted);
        loadEventsForSelectedDates(today);


        //Onclick, change date to SelectedDate on Calendarview
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDates = Calendar.getInstance();
                selectedDates.set(year,month,dayOfMonth);
                String selectedFormatted = android.text.format.DateFormat.format("dd MMMM", selectedDates.getTime()).toString();
                selectedDateText.setText("Schedule On " + selectedFormatted);
                loadEventsForSelectedDates(selectedDates);
            }
        });


//        addEventButton.setOnClickListener();

        return view;
    }



    private void setupData() {
        DataBaseHelperCalendar myDB = new DataBaseHelperCalendar(getContext());
        allEvents = (ArrayList<Event>) myDB.getAllEvents();

        Log.d(TAG, "Retrieved "+ allEvents.size() + "events from database");

        for (Event event :allEvents){
            event.setColour(ContextCompat.getColor(getContext(),event.getColor()));
        }

    }



    //Load the events for the selected Date
    private void loadEventsForSelectedDates(Calendar date){


        //Load events list
        ArrayList<IEvent> eventsForDay = new ArrayList<>();


        for (Event event : allEvents) {
            if (event.isOnDate(date)) {
                eventsForDay.add(event);
            }
        }

        //Set the events onto the day view
        hourlyDayView.setEvents(eventsForDay);

    }




}