package com.example.calmacar.utils;

import android.widget.EditText;

/**
 * A validator used to check if user input respects some predefined rules.
 *
 * To use create an instance of the Validator class and for each method
 * pass the EditText for which input should be validated.
 *
 */
public class Validator {

    private static Validator instance;

    private Validator(){}

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

    public boolean isPhoneNumberValid(EditText et_phoneNumber){
        String value = et_phoneNumber.getText().toString().trim();
        String numberPattern = "^[+][0-9]{11}$";

        if (value.isEmpty()){
            et_phoneNumber.setError("Veuillez entrer un numéro de télephone");
            return false;
        }
        if (!value.matches(numberPattern)){
            et_phoneNumber.setError("numéro de télephone non valid");
            return false;
        }
        et_phoneNumber.setError(null);
        return true;
    }

    public boolean isCityNameValid(EditText et_city) {

        String value = et_city.getText().toString().trim();

        if (value.isEmpty()){
            et_city.setError("Veuillez entrer le nom de la ville");
            return false;
        }
        // TODO check if the city is in the list of accepted cities

        return true;
    }

    public boolean isPriceValid(EditText et_price) {
        String value = et_price.getText().toString().trim();

        if (value.isEmpty()){
            et_price.setError("Veuillez entrer un prix");
            return false;
        }

        // TODO validate price

        return true;
    }

    public boolean isDescriptionValid(EditText et_description) {
        // TODO validate description
        return true;
    }
}
