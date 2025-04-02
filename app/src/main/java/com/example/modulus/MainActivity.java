package com.example.modulus;

import static com.example.modulus.R.id.nav_home;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.modulus.Calendar.CalendarFragment;
import com.example.modulus.Home.HomeFragment;
import com.example.modulus.Insights.InsightsFragment;
import com.example.modulus.Planner.PlannerFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toggle = new ActionBarDrawerToggle(this, drawerLayout,toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //bottomNavigationView.setOnNavigationItemSelectedListener(navListener);

        /*if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout, new HomeFragment()).commit();
            navigationView.setCheckedItem(nav_home);
        }*/

        bottomNavigationView.setSelectedItemId(nav_home);
        bottomNavigationView.setOnItemSelectedListener(navListener);
        Fragment selectedFragment = new HomeFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showBottomDialog();
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // Called when an item in the NavigationView is selected.
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // Handle the selected item based on its ID
                if (item.getItemId() == R.id.nav_home) {
                    // Show a Toast message for the Account item
                    Toast.makeText(MainActivity.this, "Account Details", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_settings) {
                    // Show a Toast message for the Settings item
                    Toast.makeText(MainActivity.this, "Settings Opened", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_about) {
                    // Show a Toast message for the Settings item
                    Toast.makeText(MainActivity.this, "About Us", Toast.LENGTH_SHORT).show();
                }
                if (item.getItemId() == R.id.nav_logout) {
                    // Show a Toast message for the Logout item
                    Toast.makeText(MainActivity.this, "You are Logged Out", Toast.LENGTH_SHORT).show();
                    Log.d("Logout", "LOGOUTTTTT");
                    Intent intent = new Intent(MainActivity.this, ActivityLogIn.class);
                    startActivity(intent);
                }

                // Close the drawer after selection
                //drawerLayout.closeDrawers();
                // Indicate that the item selection has been handled
                return true;
            }
        });

        /*// Add a callback to handle the back button press
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            // Called when the back button is pressed.
            @Override
            public void handleOnBackPressed() {
                // Check if the drawer is open
                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    // Close the drawer if it's open
                    drawerLayout.closeDrawer(GravityCompat.START);
                } else {
                    // Finish the activity if the drawer is closed
                    finish();
                }
            }
        });*/
    }
    //outside onCreate
    /*
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            switch (item.getItemId()) {
                case R.id.home:
                    selectedFragment = new HomeFragment();
                    break;

                case R.id.calendar:
                    selectedFragment = new CalendarFragment();
                    break;

                case R.id.planner:
                    selectedFragment = new PlannerFragment();
                    break;
                case R.id.insights:
                    selectedFragment = new InsightsFragment();
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,selectedFragment).commit();
            return true;
        }
    };

    */

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private NavigationBarView.OnItemSelectedListener navListener = item -> {
        // By using switch we can easily get the selected fragment by using there id.
        int itemId = item.getItemId();
        if (itemId == R.id.home) {
            replaceFragment(new HomeFragment());
        } else if (itemId == R.id.calendar) {
            replaceFragment(new CalendarFragment());
        } else if (itemId == R.id.planner) {
            replaceFragment(new PlannerFragment());
        } else if (itemId == R.id.insights) {
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

    private void showBottomDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottomsheetlayout);

        LinearLayout eventLayout = dialog.findViewById(R.id.layoutEvent);
        LinearLayout taskLayout = dialog.findViewById(R.id.layoutTask);
        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);

        eventLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"Upload event lmao",Toast.LENGTH_SHORT).show();

            }
        });

        taskLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                Toast.makeText(MainActivity.this,"task task atsdk aajfkwbjkav",Toast.LENGTH_SHORT).show();

            }
        });


        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }
}