package com.example.calmacar.main.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.widget.TextView;

import com.example.calmacar.R;
import com.example.calmacar.main.model.AuthManager;
import com.google.firebase.auth.FirebaseAuth;

public class AuthActivity extends AppCompatActivity {

//    TextView title;
    String userType;
    AuthManager mAuthManager;

    // Receivers
    BroadcastReceiver loginReceiver, signupReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        // References
        mAuthManager = AuthManager.getInstance();

        // Set the title of the activity based on user type
        userType = getIntent().getStringExtra("EXTRA_USERTYPE");
//        if (userType != null)
//            title.setText(userType.toUpperCase());
//        else
//            title.setText("UNKNOWN");

        // displaying login fragment
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frag_auth_form, LoginFormFragment.newInstance("",""))
                .commit();

        // Broadcast Receiver from fragments
        IntentFilter loginFilter = new IntentFilter("DATA_LOGIN");
        loginReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                intent.putExtra("EXTRA_USER_TYPE", userType);
                mAuthManager.LoginUser(AuthActivity.this, intent.getExtras());
            }
        };

        registerReceiver(loginReceiver, loginFilter);

        IntentFilter signupFilter = new IntentFilter("DATA_SIGNUP");
        signupReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                mAuthManager.SignupUser(AuthActivity.this, intent.getExtras());
            }
        };
        registerReceiver(signupReceiver, signupFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();

        // check if there is a user logged in
        if (!mAuthManager.isUserLoggedIn())
            return;

        // Check that his email is verified
        if(!mAuthManager.checkEmailIsVerified(AuthActivity.this))
            return;

        // At this point the user is logged in and verified
        mAuthManager.redirectToApp(AuthActivity.this, userType);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // redirect user back to main activity if after he opened the mail app to verify account
        /*if(userOpenedMailApp){
            userOpenedMailApp = false;
            finish();
        }*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(loginReceiver);
        unregisterReceiver(signupReceiver);
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