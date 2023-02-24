package com.example.calmacar;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button btn_driver, btn_passenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // References
        btn_driver = findViewById(R.id.btn_driver);
        btn_passenger = findViewById(R.id.btn_passenger);

        // buttons listeners
        btn_driver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadAuthActivity("Conducteur");
            }
        });

        btn_passenger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoadAuthActivity("Passager");
            }
        });
    }

    /**
    * Load authentication activity
    *
    * @param userType The type of the user that is trying to login (Driver/Passenger)
    * */
    private void LoadAuthActivity(String userType) {
        Intent intent = new Intent(this, AuthActivity.class);
        intent.putExtra("EXTRA_USERTYPE", userType);
        this.startActivity(intent);
    }
}