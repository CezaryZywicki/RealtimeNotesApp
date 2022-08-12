package com.example.realtimenotesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UserAccountActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView welcomeMessageTV;
    private Button logoutBtn, updatePasswordBtn, updateEmailBtn;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_account);

        welcomeMessageTV = findViewById(R.id.welcomeMessage_TV);
        logoutBtn = findViewById(R.id.logout_Btn);

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        welcomeMessageTV.setText("Hi, "+ user.getEmail());
        logoutBtn.setOnClickListener(this);


        context = this;

        updatePasswordBtn = findViewById(R.id.update_password_Btn);
        updatePasswordBtn.setOnClickListener(this);

        updateEmailBtn = findViewById(R.id.update_email_btn);
        updateEmailBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        int id  = view.getId();
        switch (id) {
            case R.id.logout_Btn:
                // Logout here
                showLogoutDialog();
                break;

            case R.id.update_password_Btn:
                showUpdatePasswordActivity();
                break;
            case R.id.update_email_btn:
                showUpdateEmailActivity();
                break;
        }
    }

    private void showUpdateEmailActivity() {

        // TODO: Show Update Email Activity

        Intent updateEmailActivity = new Intent(UserAccountActivity.this, UpdateEmailActivity.class);
        startActivity(updateEmailActivity);
    }

    private void showUpdatePasswordActivity() {
        Toast.makeText(context, "Update Password here!", Toast.LENGTH_SHORT).show();
        // Show Update Password Activity

        Intent updatePasswordActivity = new Intent(UserAccountActivity.this, UpdatePasswordActivity.class);
        startActivity(updatePasswordActivity);
    }

    private void showLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Are you sure you want to logout?").setPositiveButton("Yes, please logout", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                firebaseAuth.signOut();
                ((Activity)context).finish();
            }
        }).setNegativeButton("No!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}