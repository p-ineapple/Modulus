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

@SuppressLint({"SimpleDateFormat", "MissingInflatedId", "SetTextI18n", "UseCompatTextViewDrawableApis"})

public class CalendarFragment extends Fragment {

    //UI Components
    private CalendarView calendarView;
    private CalendarDayView hourlyDayView;
    private TextView selectedDateText;

    //Data
    private ArrayList<Popup> allPops;
    private final String TAG = "Calendar";



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_calendar, container, false);


        //Initialise Module data
        if (allPops == null || allPops.isEmpty())  {
            Log.d(TAG, "Setting up modules");
            setupData();
        }

        //Bind UI elements
        calendarView = view.findViewById(R.id.calendar);
        hourlyDayView = view.findViewById(dayView);
        selectedDateText = view.findViewById(R.id.selectedDateText);

        //Customise hour labels and event popup layout
        //Override CdvDecorationDefault in external library
        hourlyDayView.setDecorator(new CdvDecorationDefault(getContext()) {
            @Override
            public DayView getDayView(int hour) {
                DayView dayView = new DayView(getContext());
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, 0);

                //Format time to 12Hour AM/PM format
                 SimpleDateFormat sdf = new SimpleDateFormat("h a");
                dayView.setText(sdf.format(calendar.getTime()).toUpperCase());
                return dayView;
            }


            @Override
            public PopupView getPopupView(IPopup popup, Rect eventBound, int hourHeight, int seperateH) {
                PopupView view = new PopupView(mContext);
                view.setPopup(popup);

                //Position popup based on event timing with tweaks to ensure aesthetics
                view.setPosition(eventBound, -hourHeight / 4 + seperateH,  hourHeight / 2 - seperateH * 2 - 10);

                //Cast to custom Popup class
                Popup pop_up = (Popup) popup;

                //Format start and end time
                SimpleDateFormat sdf = new SimpleDateFormat("h:mm a");
                String startTimeString = sdf.format(pop_up.getStartTime().getTime());
                String endTimeString = sdf.format(pop_up.getEndTime().getTime());
                String timeString = startTimeString + " -\n" + endTimeString;

                //Bind UI elements for event details
                MaterialCardView card = view.findViewById(R.id.cardview);
                TextView timeTextView = view.findViewById(R.id.time);
                TextView descTextView = view.findViewById(R.id.desc);
                timeTextView.setText(timeString);

                //Customise colour of UI elements with module colour
                int colour = ContextCompat.getColor(mContext, pop_up.getColor());
                card.setStrokeColor(colour);
                timeTextView.setTextColor(colour);
                descTextView.setTextColor(colour);
                timeTextView.setCompoundDrawableTintList(ColorStateList.valueOf(colour));

                return view;
            }
        });

        //Display today's date and events on launch
        Calendar today = Calendar.getInstance();
        String todayFormatted = android.text.format.DateFormat.format("dd MMMM", today.getTime()).toString();
        selectedDateText.setText("Schedule On " + todayFormatted);
        loadEventsForSelectedDates(today,view);

        //Handles date selection from calendar
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


    //Retrieve events from database and add to allPops
    private void setupData() {
        DataBaseHelperCalendar myDB = new DataBaseHelperCalendar(getContext());
        allPops = (ArrayList<Popup>) myDB.getAllPop();

        //Logcat to check number of events retrieved
        Log.i(TAG, "Retrieved "+ allPops.size() + "events from database");
    }

    //Display specific events for specific date
    //Scroll to first event of the day or current time if no events
    private void loadEventsForSelectedDates(Calendar date,View v){
        ArrayList<IPopup> popForDay = new ArrayList<>();

        //Filter events for selected date
        for (Popup popup : allPops) {
            if (popup.isOnDate(date)) {
                popForDay.add(popup);
            }
        }

        //Load events onto the day view
        hourlyDayView.setPopups(popForDay);

        //Auto-scroll logic calculation
        int scrollOffset;
        int dayHeightInPixels = 170;

        if (!popForDay.isEmpty()) {
            Popup firstEvent = (Popup) popForDay.get(0);
            Calendar startTime = firstEvent.getStartTime();
            float frac= startTime.get(Calendar.HOUR_OF_DAY) + (startTime.get(Calendar.MINUTE) / 60f);
            scrollOffset = (int) (frac * dayHeightInPixels);
        } else {
            Calendar now = Calendar.getInstance();
            float fract = now.get(Calendar.HOUR_OF_DAY) + (now.get(Calendar.MINUTE) / 60f);
            scrollOffset = (int) (fract* dayHeightInPixels);
        }

        //Scroll to calculated position
        final ScrollView scrollViewDay = v.findViewById(R.id.scrollViewDay);
        scrollViewDay.post(new Runnable() {
            @Override
            public void run() {
                scrollViewDay.scrollTo(0, scrollOffset);
            }
        });
    }
}