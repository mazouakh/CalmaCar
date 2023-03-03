package com.example.calmacar;

import android.widget.EditText;

/**
 * A validator used to check if user input respects some predefined rules.
 *
 * To use create an instance of the Validator class and for each method
 * pass the EditText for which input should be validated.
 *
 */
public class Validator {

    public Validator(){}

    static Validator instance;
    public static Validator getInstance(){
        if (instance == null){
            instance = new Validator();
        }
        return instance;
    }

    /**
     * Checks if the last name is valid, ie: not empty
     * @param et_lastname The EditText that contains the value
     * @return true if valid and false if not
     */
    public boolean isLastnameValid(EditText et_lastname){
        String value = et_lastname.getText().toString().trim();
        if (value.isEmpty()){
            et_lastname.setError("Veuillez entrer votre nom");
            return false;
        }
        et_lastname.setError(null);
        return true;
    }

    /**
     * Checks if the first name is valid, ie: not empty
     * @param et_firstname The EditText that contains the value
     * @return true if valid and false if not
     */
    public boolean isFirstnameValid(EditText et_firstname){
        String value = et_firstname.getText().toString().trim();
        if (value.isEmpty()){
            et_firstname.setError("Veuillez entrer votre Prenom");
            return false;
        }
        et_firstname.setError(null);
        return true;
    }

    /**
     * Checks if the email is valid, ie: not empty and respects an email pattern
     * @param et_email The EditText that contains the value
     * @return true if valid and false if not
     */
    public boolean isEmailValid(EditText et_email){
        String value = et_email.getText().toString().trim();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (value.isEmpty()){
            et_email.setError("Veuillez entrer votre Email");
            return false;
        }
        if (!value.matches(emailPattern)){
            et_email.setError("Email non valid");
            return false;
        }
        et_email.setError(null);
        return true;
    }

    /**
     * Checks if the et_password is valid, ie: not empty and respects a password pattern
     * @param et_password The EditText that contains the value
     * @return true if valid and false if not
     */
    public boolean isPasswordValid(EditText et_password){
        String value = et_password.getText().toString().trim();
        String passwordPattern = "^" +
                "(?=.*[a-zA-Z])" + // any letter
                "(?=.*[@#$%^&+=])" + // at least 1 special character
                "(?=\\S+$)" + // no white space
                ".{8,}" + // at least 8 characters
                "$";

        if (value.isEmpty()){
            et_password.setError("Veuillez entrer un Mot de passe");
            return false;
        }
        if (!value.matches(passwordPattern)){
            et_password.setError("Mot de passe non valid : \nMinimum 8 characters dont 1 character spacial");
            return false;
        }
        et_password.setError(null);
        return true;
    }
}
