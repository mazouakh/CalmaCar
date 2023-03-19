package com.example.calmacar.main.model;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import com.example.calmacar.R;
import com.example.calmacar.common.model.User;
import com.example.calmacar.driver.view.HomeActivity;
import com.example.calmacar.main.view.AuthActivity;
import com.example.calmacar.main.view.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AuthManager {


    FirebaseAuth mAuth;
    FirebaseDatabase mDB;
    DatabaseReference registeredUsersReference;

    public AuthManager(){
        // Firebase
        mAuth = FirebaseAuth.getInstance();
        mDB = FirebaseDatabase.getInstance();
        // Get the Registered Users node reference from the database
        registeredUsersReference = mDB.getReference("Registered Users");
    }

    static AuthManager instance;
    public static AuthManager getInstance(){
        if (instance == null){
            instance = new AuthManager();
        }
        return instance;
    }

    public void SignupUser(Context ctx, Bundle extras){
        Toast.makeText(ctx, "Inscription en cours...", Toast.LENGTH_SHORT).show();

        // extras
        String email = extras.getString("EXTRA_EMAIL");
        String password = extras.getString("EXTRA_PASSWORD");
        String lastname = extras.getString("EXTRA_LASTNAME");
        String firstname = extras.getString("EXTRA_FIRSTNAME");
        String phone = extras.getString("EXTRA_PHONE");

        // Creating a new user using the email & password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                // checking if there were any errors
                                if (!task.isSuccessful()){
                                    try {
                                        throw task.getException();
                                    } catch (FirebaseAuthUserCollisionException e){
                                        EditText et_email = ((AuthActivity) ctx).getSupportFragmentManager()
                                                .findFragmentById(R.id.frag_auth_form)
                                                .getView()
                                                .findViewById(R.id.et_email);
                                        et_email.setError("User already registered with this email. Use another email.");
                                        et_email.requestFocus();
                                    }
                                    catch (Exception e){
                                        Toast.makeText(ctx, "Registration failed with error : " + e, Toast.LENGTH_SHORT).show();
                                    }
                                    return;
                                }

                                // At this point the registration is successful
                                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                                // Adding User data from the form to the database

                                // Update user's display name
                                firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                                        .setDisplayName(firstname + " " + lastname)
                                        .build());

                                // Create a new instance of User
                                User newUser = new User(firebaseUser.getUid(), firstname, lastname, phone);

                                // Update the table with the new User instance and associate it to the FirebaseUser using the UID
                                registeredUsersReference.child(newUser.getUid()).setValue(newUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        // Check if registration was successful
                                        if (!task.isSuccessful()){
                                            try {
                                                throw task.getException();
                                            }catch (Exception e){
                                                Toast.makeText(ctx, "Registration in db failed with error : " + e, Toast.LENGTH_SHORT).show();
                                            }
                                            return;
                                        }
                                        // At this point the registration is successful
                                        Toast.makeText(ctx, "Inscription avec success. Veuillez verifier votre email.", Toast.LENGTH_SHORT).show();

                                        // Ask user to verify account in order to use app
                                        firebaseUser.sendEmailVerification();
                                        mAuth.signOut();
                                        showEmailNotVerifiedAlertDialogue(ctx);
                                    }
                                });
                            }
                        }
                );
    }

    public void LoginUser(Context ctx, Bundle extras){
        String email = extras.getString("EXTRA_EMAIL");
        String password = extras.getString("EXTRA_PASSWORD");
        String userType = extras.getString("EXTRA_USER_TYPE");

        // Firebase Login
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (!task.isSuccessful()){
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidUserException e){
                        EditText et_email = ((AuthActivity) ctx).getSupportFragmentManager()
                                .findFragmentById(R.id.frag_auth_form)
                                .getView()
                                .findViewById(R.id.et_email);
                        et_email.setError("No user found with this email");
                        et_email.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e){
                        EditText et_password = ((AuthActivity) ctx).getSupportFragmentManager()
                                .findFragmentById(R.id.frag_auth_form)
                                .getView()
                                .findViewById(R.id.et_password);
                        et_password.setError("The password you entered in not correct. Try again.");
                        et_password.requestFocus();
                    } catch (Exception e){
                        Toast.makeText(ctx, "Login failed with error : " + e, Toast.LENGTH_SHORT).show();
                    }
                    return;
                }
                // At this point login is successful

                // Getting current user
                FirebaseUser firebaseUser = mAuth.getCurrentUser();

                // Check that his email is verified
                if (!mAuth.getCurrentUser().isEmailVerified()) {
                    mAuth.getCurrentUser().sendEmailVerification();
                    mAuth.signOut();
                    showEmailNotVerifiedAlertDialogue(ctx);
                }

                // All went well
                Toast.makeText(ctx, "Connexion avec success", Toast.LENGTH_SHORT).show();
                redirectToApp(ctx, userType);
            }
        });
    }

    public void logout() {
        mAuth.signOut();
    }

    public boolean checkEmailIsVerified(Context ctx) {
        if (!mAuth.getCurrentUser().isEmailVerified()){
            mAuth.getCurrentUser().sendEmailVerification();
            mAuth.signOut();
            showEmailNotVerifiedAlertDialogue(ctx);
            return false;
        }
        return true;
    }

    private void showEmailNotVerifiedAlertDialogue(Context ctx) {
        // Crating the builder
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle("Email non verifié");
        builder.setMessage("Vous n'avez pas verifié votre adresse email. " +
                "Par mesure de securité seul les utilisateus verifirés peuvent utiliser l'application.\n" +
                "Nous venons de vous envoyer un mail de verification, veuillez suivre les inscrutions puis revenir une fois cela fait.\n\n" +
                "On vous attend!");

        // Open email app if user button
        builder.setPositiveButton("Continuer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.startActivity(Intent.createChooser(intent,"Open Email App: "));
//                userOpenedMailApp = true;
            }
        });

        // Return to main menu button
        builder.setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(ctx, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // TODO figure out why this activity is not closing and it still receives broadcast
                //  from fragment which results in calling the signup twice
                ((AuthActivity) ctx).finish();
                ctx.startActivity(intent);
            }
        });

        builder.setCancelable(false);

        // Create & show the AlertDialog
        builder.create().show();
    }

    public void redirectToApp(Context ctx, String userType){
        Intent intent = new Intent(ctx, userType.equals("Conducteur") ? HomeActivity.class : com.example.calmacar.passenger.view.HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXTRA_USERTYPE", userType);
        ((AuthActivity) ctx).finish();
        ctx.startActivity(intent);
    }
}
