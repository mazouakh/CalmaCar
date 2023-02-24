package com.example.calmacar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    String userType;
    TextView tv_userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Hooks
        tv_userType = findViewById(R.id.tv_userType);

        // Intent Extras
        userType = getIntent().getStringExtra("EXTRA_USERTYPE");

        // Initialize UI
        tv_userType.setText("Welcome to " + userType + " Dashboard");

    }
}