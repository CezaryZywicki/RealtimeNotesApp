package com.example.realtimenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.appcheck.FirebaseAppCheck;
import com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    EditText emailET, passwordET;
    private Button loginBtn;
    private TextView signUpText;


    ValidateInput validateInput;
    private String email;
    private String password;
    private FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // AppCheck
        FirebaseApp.initializeApp(/*context=*/ this);
        FirebaseAppCheck firebaseAppCheck = FirebaseAppCheck.getInstance();
        firebaseAppCheck.installAppCheckProviderFactory(
                DebugAppCheckProviderFactory.getInstance());

        // [START] Login part

        validateInput = new ValidateInput(this);

        emailET = findViewById(R.id.email_ET_singup);
        passwordET = findViewById(R.id.password_ET);

        loginBtn = findViewById(R.id.login_btn);
        signUpText = findViewById(R.id.signUp_TV);

        loginBtn.setOnClickListener(this);
        signUpText.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        progressBar = findViewById(R.id.progressBar);

        // [END] Login part
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.login_btn:
                handleLoginBtnClick();
                break;

            case R.id.signUp_TV:
                handleSignUpClick();
                break;
        }
    }
    private void handleLoginBtnClick() {

        showProgressBar();
        email = emailET.getText().toString();
        password  = passwordET.getText().toString();
        //TODO: Validate the email and password fields input.

        if (validateInput.checkIfEmailIsValid(email) && validateInput.checkIfPasswordIsValid(password)) {

            // https://firebase.google.com/docs/auth/android/password-auth#sign_in_a_user_with_an_email_address_and_password
            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        hideProgressBar();
                        // Sign in success, update UI with the signed-in user's information
                        //Log.d(TAG, "signInWithEmail:success");
                        Toast.makeText(LoginActivity.this, "Logged in user"+ email,Toast.LENGTH_SHORT ).show();
                        // Show another screen on login. See how to logout
                        Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(homeIntent);
                        //FirebaseUser user = mAuth.getCurrentUser();
                        //updateUI(user);
                    } else {
                        hideProgressBar();
                        // If sign in fails, display a message to the user.
                        //Log.w(TAG, "signInWithEmail:failure", task.getException());
                        //Intent loginIntent = new Intent(MainActivity.this, SignUpActivity.class);
                       // startActivity(loginIntent);
                        Toast.makeText(LoginActivity.this, "Authentication failed with error : " +task.getException(),
                                Toast.LENGTH_SHORT).show();
                        //updateUI(null);
                    }
                }
            });
        }
        //TODO: Login the user with email and password here.
    }

    private void hideProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void showProgressBar() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void handleSignUpClick() {
        CharSequence signtext = "Signup Here!";
        Toast.makeText(this, signtext, Toast.LENGTH_SHORT).show();
        Intent signUpActivity = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(signUpActivity);

    }


}