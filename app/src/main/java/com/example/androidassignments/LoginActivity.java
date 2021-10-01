package com.example.androidassignments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.androidassignments.R;

public class LoginActivity extends AppCompatActivity {

    protected static final String ACTIVITY_NAME = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate");

        // Declare button, text field, and sharedPreferences variables
        final Button loginButton = findViewById(R.id.loginButton);
        final EditText loginField = findViewById(R.id.loginField);
        SharedPreferences sharedPreferences = getSharedPreferences("com.example.androidassignments.PREFERENCE_FILE_KEY",MODE_PRIVATE);

        // Set email text field to default email from sharedPreferences
        String preferenceEmail = sharedPreferences.getString("DefaultEmail", "email@domain.com");
        loginField.setText(preferenceEmail);

        // When loginButton is clicked, save email to sharedPreferences and go to MainActivity
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Save email to sharedPreferences
                SharedPreferences.Editor prefEdit = sharedPreferences.edit();
                prefEdit.putString("DefaultEmail", loginField.getText().toString());
                prefEdit.commit();

                // Launch MainActivity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    protected void onResume() {
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    protected void onStart() {
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    protected void onPause() {
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    protected void onStop() {
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    protected void onDestroy() {
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }

}