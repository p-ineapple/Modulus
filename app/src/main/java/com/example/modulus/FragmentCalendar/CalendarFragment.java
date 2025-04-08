package com.example.modulus.FragmentCalendar;

import static com.example.modulus.R.id.dayView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;


import com.example.modulus.R;
import com.framgia.library.calendardayview.CalendarDayView;
import com.framgia.library.calendardayview.data.IEvent;



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

        allEvents = new ArrayList<>();

        //set up modules list
        if (allEvents.isEmpty()) {
            Log.d(TAG, "Setting up modules");
            setupData();
        }
        Log.d(TAG, "Database set up");

        //Initialisation
        calendarView = view.findViewById(R.id.calendar);
        hourlyDayView = view.findViewById(dayView);
        addEventButton = view.findViewById(R.id.addEventButton);
        selectedDateText = view.findViewById(R.id.selectedDateText);

//        initialiseAllEvents();

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

//    private void initialiseAllEvents() {
//        allEvents = new ArrayList<>();
//
//
//
//
//        Calendar april3 = Calendar.getInstance();
//        april3.set(2025,Calendar.APRIL,3);
//
//        //HASS Lesson I hate it why is it 830
//        int HASScolour = ContextCompat.getColor(requireContext(), R.color.calendar_green);
//        Calendar HASSstart = (Calendar) april3.clone();
//        HASSstart.set(Calendar.HOUR_OF_DAY,8);
//        HASSstart.set(Calendar.MINUTE,30);
//        Calendar HASSend = (Calendar) april3.clone();
//        HASSend.set(Calendar.HOUR_OF_DAY,11);
//        HASSend.set(Calendar.MINUTE,30);
//        Event HASS = new Event(1,april3,HASSstart,HASSend,"02.147TS Interventions in Design, Technology and Society","1.308",HASScolour);
//        allEvents.add(HASS);
//
//        //ALG Lesson I hate it why is it 830
//        int ALGcolour = ContextCompat.getColor(requireContext(), R.color.calendar_red);
//        Calendar ALGstart = (Calendar) april3.clone();
//        ALGstart.set(Calendar.HOUR_OF_DAY,11);
//        ALGstart.set(Calendar.MINUTE,30);
//        Calendar ALGend = (Calendar) april3.clone();
//        ALGend.set(Calendar.HOUR_OF_DAY,16);
//        ALGend.set(Calendar.MINUTE,30);
//        Event ALG = new Event(2,april3, ALGstart,ALGend,"50.004 Algorithms","1.308",ALGcolour);
//        allEvents.add(ALG);
//
//
//        Calendar april5 = Calendar.getInstance();
//        april5.set(2025,Calendar.APRIL,5);
//
//        //HASS Lesson I hate it why is it 830
//
//        Event HASS3 = new Event(4,april5,HASSstart,HASSend,"02.147TS Interventions in Design, Technology and Society","1.308",HASScolour);
//        allEvents.add(HASS3);
//
//        //HASS Lesson I hate it why is it 830
//
//        Event ALG2 = new Event(4,april5, ALGstart,ALGend,"50.004 Algorithms","1.308",ALGcolour);
//        allEvents.add(ALG2);
//
//
//
//    }


}