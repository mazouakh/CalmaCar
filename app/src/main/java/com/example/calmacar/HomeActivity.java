package com.example.calmacar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    String userType;
    TextView tv_userType;
    Button btn_logout;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Firebase
        mAuth = FirebaseAuth.getInstance();

        // Hooks
        tv_userType = findViewById(R.id.tv_userType);
        btn_logout = findViewById(R.id.btn_logout);

        // Intent Extras
        userType = getIntent().getStringExtra("EXTRA_USERTYPE");

        // Initialize UI
        tv_userType.setText("Welcome to " + userType + " Dashboard");

        // Button Listeners
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout() {
        mAuth.signOut();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }


}