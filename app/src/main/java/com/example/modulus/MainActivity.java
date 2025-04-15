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

    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    ActionBarDrawerToggle toggle;
    Toolbar toolbar;
    private final String TAG = "MAIN";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
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
                    Log.d(TAG, "Logging out");
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

//    private void showBottomDialog() {
//        final Dialog dialog = new Dialog(this);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.bottomsheetlayout);
//        LinearLayout eventLayout = dialog.findViewById(R.id.layoutEvent);
//        LinearLayout taskLayout = dialog.findViewById(R.id.layoutTask);
//        ImageView cancelButton = dialog.findViewById(R.id.cancelButton);
//        eventLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"Upload event lmao",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        taskLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                Toast.makeText(MainActivity.this,"task task atsdk aajfkwbjkav",Toast.LENGTH_SHORT).show();
//
//            }
//        });
//        cancelButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        //dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
//        dialog.getWindow().setGravity(Gravity.BOTTOM);
//    }
}