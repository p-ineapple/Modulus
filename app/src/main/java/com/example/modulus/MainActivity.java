package com.example.modulus;

import static com.example.modulus.R.id.nav_home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.modulus.FragmentCalendar.CalendarFragment;
import com.example.modulus.FragmentHome.HomeFragment;
import com.example.modulus.FragmentInsights.InsightsFragment;
import com.example.modulus.FragmentPlanner.PlannerFragment;
import com.example.modulus.Login.ActivityLogIn;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private final String TAG = "MAIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        bottomNavigationView.setSelectedItemId(nav_home);
        bottomNavigationView.setOnItemSelectedListener(navListener);
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // Called when an item in the NavigationView is selected.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) { // Handle the selected item based on its ID
                int itemId = item.getItemId();
                if (itemId == R.id.nav_home){
                    Toast.makeText(MainActivity.this, "Home Page", Toast.LENGTH_SHORT).show();
                    getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();
                }else if (itemId == R.id.nav_logout){
                    Toast.makeText(MainActivity.this, "You are Logged Out", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Logging out");
                    Intent intent = new Intent(MainActivity.this, ActivityLogIn.class);
                    startActivity(intent);
                }
                drawerLayout.closeDrawers();// Close the drawer after selection
                return true; // Indicate that the item selection has been handled
            }
        });
    }

    //outside onCreate
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            Log.d(TAG, "Opening Home fragment");
            replaceFragment(new HomeFragment());
        } else if (itemId == R.id.calendar) {
            Log.d(TAG, "Opening Calendar fragment");
            replaceFragment(new CalendarFragment());
        } else if (itemId == R.id.planner) {
            Log.d(TAG, "Opening PlannerModel fragment");
            replaceFragment(new PlannerFragment());
        } else if (itemId == R.id.insights) {
            Log.d(TAG, "Opening Insights fragment");
            replaceFragment(new InsightsFragment());
        }
        return true;
    };
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
    private static final int TIME_DELAY = 2000;
    private static long back_pressed;
    @Override
    public void onBackPressed() {
        if (back_pressed + TIME_DELAY > System.currentTimeMillis()) {
            super.onBackPressed();
        } else {
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}