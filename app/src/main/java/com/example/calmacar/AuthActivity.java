package com.example.calmacar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

public class AuthActivity extends AppCompatActivity {

    TextView title;
    String userType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // References
        title = findViewById(R.id.textView_title);

        // Set the title of the activity based on user type
        userType = getIntent().getStringExtra("EXTRA_USER_TYPE");
        if (userType != null)
            title.setText(userType.toUpperCase());
        else
            title.setText("UNKNOWN");

        // displaying login fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_auth_form, LoginFormFragment.newInstance("",""))
                .commit();
    }

    public void LoadRegisterForm(){
        // if Register form fragment is already loaded, then return
        if (getSupportFragmentManager().findFragmentById(R.id.frag_auth_form) instanceof RegisterFormFragment)
            return;

        // Load Register form fragment
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frag_auth_form, RegisterFormFragment.newInstance("",""))
                .commit();
    }

    public void LoadLoginForm(){
        // if Login form fragment is already loaded, then return
        if (getSupportFragmentManager().findFragmentById(R.id.frag_auth_form) instanceof LoginFormFragment)
            return;

        // Load Login form fragment
        getSupportFragmentManager().beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.frag_auth_form, LoginFormFragment.newInstance("",""))
                .commit();
    }



}