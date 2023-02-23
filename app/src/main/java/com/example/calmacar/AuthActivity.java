package com.example.calmacar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

        // Broadcast Receiver from fragments
        IntentFilter loginFilter = new IntentFilter("DATA_LOGIN");
        BroadcastReceiver loginReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                LoginUser(intent.getExtras());
            }
        };
        registerReceiver(loginReceiver, loginFilter);

        IntentFilter signupFilter = new IntentFilter("DATA_SIGNUP");
        BroadcastReceiver signupReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                SignupUser(intent.getExtras());
            }
        };
        registerReceiver(signupReceiver, signupFilter);
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

    public void SignupUser(Bundle extras){
        Toast.makeText(this, "Inscription en cours...", Toast.LENGTH_SHORT).show();

        // extras
        String email = extras.getString("EXTRA_EMAIL");
        String password = extras.getString("EXTRA_PASSWORD");
        String lastname = extras.getString("EXTRA_LASTNAME");
        String firstname = extras.getString("EXTRA_FIRSTNAME");


        // Getting FirebaseAuth instance
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        // Creating a new user using the email & password
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(
                    new OnCompleteListener<AuthResult>() {
                       @Override
                       public void onComplete(@NonNull Task<AuthResult> task) {
                           // checking if there were any errors
                           if (!task.isSuccessful()){
                               try {
                                   throw task.getException();
                               } catch (FirebaseAuthUserCollisionException e){
                                   EditText et_email = getSupportFragmentManager()
                                                       .findFragmentById(R.id.frag_auth_form)
                                                       .getView()
                                                       .findViewById(R.id.et_email);
                                   et_email.setError("User already registered with this email. Use another email.");
                                   et_email.requestFocus();
                               }
                               catch (Exception e){
                                   Toast.makeText(AuthActivity.this, "Registration failed with error : " + e, Toast.LENGTH_SHORT).show();
                               }
                               return;
                           }

                           // At this point the registration is successful
                           Toast.makeText(AuthActivity.this, "Registration successful!", Toast.LENGTH_SHORT).show();
                           FirebaseUser firebaseUser = mAuth.getCurrentUser();

                           // Adding User data from the form to the database

                           // Update user's display name
                           firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                   .setDisplayName(firstname + " " + lastname)
                                   .build());

                           // Create a new instance of User
                            User newUser = new User(firebaseUser.getUid(), firstname, lastname);

                           // Get the Registered Users table reference from the database
                           DatabaseReference usersReference = FirebaseDatabase
                                   .getInstance("https://calmacar-default-rtdb.europe-west1.firebasedatabase.app")
                                   .getReference("Registered Users");

                           // Update the table with the new User instance and associate it to the FirebaseUser using the UID
                           usersReference.child(firebaseUser.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                               @Override
                               public void onComplete(@NonNull Task<Void> task) {
                                   // Check if registration was successful
                                   if (!task.isSuccessful()){
                                       try {
                                           throw task.getException();
                                       }catch (Exception e){
                                           Toast.makeText(AuthActivity.this, "Registration in db failed with error : " + e, Toast.LENGTH_SHORT).show();
                                       }
                                       return;
                                    }
                                   // At this point the registration is successful
                                   // send a verification email
                                   firebaseUser.sendEmailVerification();
                                   Toast.makeText(AuthActivity.this, "Registration successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                                   // Redirect to user dashboard
                                   redirectToDashboard();
                               }
                           });
                       }
                   }
            );
    }

    //TODO
    public void LoginUser(Bundle extras){
        String email = extras.getString("EXTRA_EMAIL");
        String password = extras.getString("EXTRA_PASSWORD");
        
        // check if user already exists

        // compare input with the credentials in the database

        // login the new user
        Toast.makeText(this, "Connexion en cours...", Toast.LENGTH_SHORT).show();

        // redirect to Driver Dashboard
        redirectToDashboard();
    }

    private void redirectToDashboard(){
        Intent intent = new Intent(this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        this.startActivity(intent);
        finish();
    }

}