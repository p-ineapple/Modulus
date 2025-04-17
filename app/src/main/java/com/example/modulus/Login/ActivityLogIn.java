package com.example.modulus.Login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.modulus.MainActivity;
import com.example.modulus.R;

import java.io.IOException;

public class ActivityLogIn extends AppCompatActivity {

    //UI Components
    private TextView forget;
    private EditText email, password;
    private RelativeLayout rl_login_button;
    private CheckBox rememberme;

    //Database Helper instance
    private DataBaseHelperLogin databasehelper;

    //SharedPreferences components for storing user login info
    private final String sharedPrefFile = "com.example.android.mainsharedprefs" ;
    private static final String KEY_EMAIL = "Email";
    private static final String KEY_PASSWORD = "Password";
    private SharedPreferences mPreferences;

    private final String TAG = "LOGIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_in);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        //Bind UI elements
        forget = findViewById(R.id.forget);
        forget.setMovementMethod(LinkMovementMethod.getInstance()); // Make forget clickable
        email = findViewById(R.id.et_email_address);
        password = findViewById(R.id.et_password);
        rememberme = findViewById(R.id.remember_me);
        rl_login_button = findViewById(R.id.rl_login_button);

        //Initialise Database Helper instance and copy database if needed
        databasehelper = new DataBaseHelperLogin(this);
        try {
            databasehelper.createDatabase();
            Log.e(TAG, "Database created");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Database copy failed", e);
        }

        //Get readable instance of database
        SQLiteDatabase db = databasehelper.getReadableDatabase();
        Log.d(TAG, "Database opened: " + db.getPath());

        //Retrieve SharedPreferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String savedEmail = mPreferences.getString(KEY_EMAIL,"");
        String savedPassword = mPreferences.getString(KEY_PASSWORD,"");

        //If SharedPreferences exists, set email, password and check rememberme box
        if ((!savedEmail.isEmpty())&& (!savedPassword.isEmpty())){
            email.setText(savedEmail);
            password.setText(savedPassword);
            rememberme.setChecked(true);
        }

        //Handle login button click
        rl_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Get email and password text and trims to remove spaces
                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                //Check if fields are empty
                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(ActivityLogIn.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Empty Fields");
                    return;
                }

                //Debug Logcat
                Log.d(TAG, emailText+passwordText);


                //Check if credentials matches a valid user
                if (databasehelper.checkEmailPasswordValid(emailText, passwordText)){
                    Toast.makeText(ActivityLogIn.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Login Successful");

                    //Save or clear login info based on rememberme box
                    SharedPreferences.Editor editor  = mPreferences.edit();
                    if (rememberme.isChecked()){
                        editor.putString(KEY_EMAIL,emailText);
                        editor.putString(KEY_PASSWORD,passwordText);
                        editor.apply();
                    }else{
                        editor.remove(KEY_EMAIL);
                        editor.remove(KEY_PASSWORD);
                        editor.apply();
                    }

                    //Call intent to direct to MainActivity
                    Intent intent = new Intent(ActivityLogIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    //Show login failed toast
                    Toast.makeText(ActivityLogIn.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    Log.i(TAG, "Login Failed");
                }





            }
        });



    }

}
