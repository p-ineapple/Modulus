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
    private TextView forget;
    private EditText email, password;
    private RelativeLayout rl_login_button;

    private CheckBox rememberme;

    private DataBaseHelperLogin databasehelper;

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


        forget = findViewById(R.id.forget);
        forget.setMovementMethod(LinkMovementMethod.getInstance());
        email = findViewById(R.id.et_email_address);
        password = findViewById(R.id.et_password);
        rememberme = findViewById(R.id.remember_me);


        databasehelper = new DataBaseHelperLogin(this);
        try {
            databasehelper.createDatabase();
            Log.e(TAG, "Database created");
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Database copy failed", e);
        }
        SQLiteDatabase db = databasehelper.getReadableDatabase();
        Log.d(TAG, "Database opened: " + db.getPath());

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String savedEmail = mPreferences.getString(KEY_EMAIL,"");
        String savedPassword = mPreferences.getString(KEY_PASSWORD,"");
        //Check if there is saved data
        if ((!savedEmail.isEmpty())&& (!savedPassword.isEmpty())){
            email.setText(savedEmail);
            password.setText(savedPassword);
            rememberme.setChecked(true);
        }


        rl_login_button = findViewById(R.id.rl_login_button);
        rl_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String emailText = email.getText().toString().trim();
                String passwordText = password.getText().toString().trim();

                if (emailText.isEmpty() || passwordText.isEmpty()) {
                    Toast.makeText(ActivityLogIn.this, "All fields are mandatory", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Empty Fields");
                    return;
                }



                if (databasehelper.checkEmailPasswordValid(emailText, passwordText)){
                    Toast.makeText(ActivityLogIn.this, "Login Successfully!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login Successful");

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


                    Intent intent = new Intent(ActivityLogIn.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else{
                    Toast.makeText(ActivityLogIn.this, "Login Failed!", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Login Failed");
                }





            }
        });



    }

}
