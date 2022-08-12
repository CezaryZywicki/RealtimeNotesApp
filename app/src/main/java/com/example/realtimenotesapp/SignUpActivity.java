package com.example.realtimenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText emailET, passwordET, passwordAgainET, nameET;

    private String email, password, passwordAgain, name;

    ValidateInput validateInput;

    Button signUpButton;

    private FirebaseAuth mAuth;  // private?
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // [START] Signup part



        emailET = findViewById(R.id.email_ET_singup);
        passwordET = findViewById(R.id.password_ET);
        passwordAgainET = findViewById(R.id.password_again);

        signUpButton = findViewById(R.id.signup_btn);

        validateInput = new ValidateInput(this);

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Signup user
                handleSignUpBtnClick();
            }
        });
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        nameET = findViewById(R.id.name_ET);
        // [END] Signup part
    }

    private void handleSignUpBtnClick() {

        // Fetching String values
        email = emailET.getText().toString();
        password = passwordET.getText().toString();
        passwordAgain = passwordAgainET.getText().toString();
        name = nameET.getText().toString();

        if(!name.isEmpty()) {
            if (validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) { // validate the input
                if (password.equals(passwordAgain)) { // Check if both passwords are the same
                    // Sign up the user
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            // This will trigger when the process is completed
                            if (task.isSuccessful()) {
                                // Get user info

                                FirebaseUser user = mAuth.getCurrentUser();
                                Toast.makeText(SignUpActivity.this, "SignUp is successful for : " +user.getEmail(),
                                        Toast.LENGTH_SHORT).show();
                                //Log.d(TAG, "SignUp is successful for : " +user.getEmail());
                                saveNameInFirebaseRealtimeDatabase(user);
                                // updateUI(user); ??
                            } else {
                                // If sign in fails, display a message to the user.
                                //Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(SignUpActivity.this, "Error occurred : " +task.getException(),
                                        Toast.LENGTH_SHORT).show();
                                // updateUI(null);
                            }
                        }
                    });
                }
                else {
                    CharSequence wrongPasswordText = "Passwords don't match! Please try again.";
                    Toast.makeText(this, wrongPasswordText, Toast.LENGTH_SHORT).show();
                }
            }
        } else
        {
            Toast.makeText(this,"Please enter a name!", Toast.LENGTH_SHORT).show();
        }




    }

    private void saveNameInFirebaseRealtimeDatabase(FirebaseUser user) {
        // https://firebase.google.com/docs/database/android/read-and-write#basic_write
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference();
        DatabaseReference nameReference = rootReference.child("Users").child(user.getUid()).child("name");
        nameReference.setValue(name);
    }
}