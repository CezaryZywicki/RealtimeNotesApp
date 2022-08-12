package com.example.realtimenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdateEmailActivity extends AppCompatActivity {

    private EditText oldEmailET, newEmailET;
    private Button updateEmailBtn;

    private ValidateInput validateInput;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private static final String TAG = "UpdateEmailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_email);

        oldEmailET = findViewById(R.id.oldEmail_ET);
        newEmailET = findViewById(R.id.newEmail_ET);

        updateEmailBtn = findViewById(R.id.update_email_button);
        updateEmailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateEmailInFirebase();
            }
        });
        validateInput = new ValidateInput(this);
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();
    }

    private void updateEmailInFirebase() {
        // Update email in Firebase

        String oldEmail = oldEmailET.getText().toString();
        String newEmail = newEmailET.getText().toString();

        if (validateInput.checkIfEmailIsValid(newEmail)){
            if(oldEmail.equals(newEmail))  {
                Toast.makeText(this, "New email can't be the same! Please enter new email!", Toast.LENGTH_SHORT).show();
            }
            else {
                // Update Email
                // https://firebase.google.com/docs/auth/android/manage-users#set_a_users_email_address
                firebaseUser.updateEmail(newEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateEmailActivity.this, "Email address updated to : " +newEmail, Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "User email address updated.");
                        }
                        else {
                            Toast.makeText(UpdateEmailActivity.this, "Error: "+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}