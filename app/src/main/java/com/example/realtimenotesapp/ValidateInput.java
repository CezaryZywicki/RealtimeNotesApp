package com.example.realtimenotesapp;

import android.content.Context;
import android.util.Patterns;
import android.widget.Toast;

public class ValidateInput {

    Context context;
    ValidateInput(Context context) {
        this.context = context;
    }

    // Method 1: Validate the email

    boolean checkIfEmailIsValid (String email) {


        CharSequence text1 = "Please enter your email";
        if (email.length()==0) {
            Toast.makeText(context, text1, Toast.LENGTH_SHORT ).show();
            return false;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            CharSequence text2 = "Please enter a valid email";
            Toast.makeText(context, text2 , Toast.LENGTH_SHORT ).show();
            return false;
        }
        else {
            return true; //email is valid
        }

    }
    boolean checkIfPasswordIsValid (String password) {
        if(password.length()==0) {
            CharSequence text3 = "Please enter a password!";
            Toast.makeText(context, text3, Toast.LENGTH_SHORT ).show();
            return false;
        }
        else if (password.length()<6) {
            CharSequence text4 = "Please enter a password of at least 6 characters";
            Toast.makeText(context, text4 , Toast.LENGTH_SHORT ).show();
            return false;
        }
        else {
            return true;
        }
    }
}
