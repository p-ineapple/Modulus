package com.example.modulus.FragmentCalendar;

import static com.example.modulus.R.id.dayView;

import android.annotation.SuppressLint;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ImageButton;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.modulus.R;
import com.framgia.library.calendardayview.CalendarDayView;
import com.framgia.library.calendardayview.DayView;
import com.framgia.library.calendardayview.PopupView;
import com.framgia.library.calendardayview.data.IPopup;
import com.framgia.library.calendardayview.decoration.CdvDecorationDefault;
import com.google.android.material.card.MaterialCardView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;




public class CalendarFragment extends Fragment {


    private CalendarView calendarView;
    private CalendarDayView hourlyDayView;

    private ImageButton addEventButton;
    private TextView selectedDateText;
    private ArrayList<Event> allEvents;
    private ArrayList<Popup> allPops;
    final String TAG = "Calendar";


    @SuppressLint("MissingInflatedId")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);


        //set up modules list
        if (allPops == null || allPops.isEmpty())  {
            Log.d(TAG, "Setting up modules");
            setupData();
        }

        //Initialisation
        calendarView = view.findViewById(R.id.calendar);
        hourlyDayView = view.findViewById(dayView);
//        addEventButton = view.findViewById(R.id.addEventButton);
        selectedDateText = view.findViewById(R.id.selectedDateText);


        //Override library time format for nicer readability in AM/PM format
        //Original format in CdvDecorationDefault
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

            @Override
            public PopupView getPopupView(IPopup popup, Rect eventBound, int hourHeight, int seperateH) {
                PopupView view = new PopupView(mContext);
                view.setOnPopupClickListener(mPopupClickListener);
                view.setPopup(popup);
                view.setPosition(eventBound, -hourHeight / 4 + seperateH,  hourHeight / 2 - seperateH * 2 - 10);
                Popup pop_up = (Popup) popup;

                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                String startTimeString = sdf.format(pop_up.getStartTime().getTime());
                String endTimeString = sdf.format(pop_up.getEndTime().getTime());
                String timeString = startTimeString + " -\n" + endTimeString;



                int colour = ContextCompat.getColor(mContext, pop_up.getColor());

                MaterialCardView card = view.findViewById(R.id.cardview);
                TextView timeTextView = view.findViewById(R.id.time);
                TextView descTextView = view.findViewById(R.id.desc);
                timeTextView.setText(timeString);

                card.setStrokeColor(colour);
                timeTextView.setTextColor(colour);
                descTextView.setTextColor(colour);
                timeTextView.setCompoundDrawableTintList(ColorStateList.valueOf(colour));

                return view;
            }
        });


        //Default event shown to be today's date
        Calendar today = Calendar.getInstance();
        String todayFormatted = android.text.format.DateFormat.format("dd MMMM", today.getTime()).toString();
        selectedDateText.setText("Schedule On " + todayFormatted);
        loadEventsForSelectedDates(today,view);


        //Onclick, change date to SelectedDate on Calendarview
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                Calendar selectedDates = Calendar.getInstance();
                selectedDates.set(year,month,dayOfMonth);
                String selectedFormatted = android.text.format.DateFormat.format("dd MMMM", selectedDates.getTime()).toString();
                selectedDateText.setText("Schedule On " + selectedFormatted);
                loadEventsForSelectedDates(selectedDates,getView());
            }
        });



        return view;
    }



    private void setupData() {

        DataBaseHelperCalendar myDB = new DataBaseHelperCalendar(getContext());
        allPops = (ArrayList<Popup>) myDB.getAllPop();

        Log.d(TAG, "Retrieved "+ allPops.size() + "events from database");





    }

    //Load the events for the selected Date
    private void loadEventsForSelectedDates(Calendar date,View v){

        //Load events list
        ArrayList<IPopup> popForDay = new ArrayList<>();

        for (Popup popup : allPops) {
            if (popup.isOnDate(date)) {
                popForDay.add(popup);


            }
        }

        //Set the events onto the day view
        hourlyDayView.setPopups(popForDay);
        int scrollOffset;
        int dayHeightInPixels = 170;
        if (!popForDay.isEmpty()) {
            Popup firstEvent = (Popup) popForDay.get(0);
            Calendar startTime = firstEvent.getStartTime();
            float frac= startTime.get(Calendar.HOUR_OF_DAY) + (startTime.get(Calendar.MINUTE) / 60);
            scrollOffset = (int) (frac * dayHeightInPixels);
        } else {
            Calendar now = Calendar.getInstance();
            float fract = now.get(Calendar.HOUR_OF_DAY) + (now.get(Calendar.MINUTE) / 60);
            scrollOffset = (int) (fract* dayHeightInPixels);

        }

        final ScrollView scrollViewDay = v.findViewById(R.id.scrollViewDay);
        scrollViewDay.post(new Runnable() {
            @Override
            public void run() {
                scrollViewDay.scrollTo(0, scrollOffset);
            }
        });


    }



}