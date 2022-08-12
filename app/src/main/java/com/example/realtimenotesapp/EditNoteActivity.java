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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class EditNoteActivity extends AppCompatActivity {

    EditText noteTitleET, noteContentET;
    Button editNoteButton;


    String noteTitle, noteContent,noteID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        noteTitleET = findViewById(R.id.noteTitle_ET);
        noteContentET = findViewById(R.id.noteContent_ET);

        if (getIntent().getExtras()!=null) {
            noteTitle = getIntent().getStringExtra("noteTitle");
            noteContent = getIntent().getStringExtra("noteContent");
            noteID = getIntent().getStringExtra("noteID");

            noteTitleET.setText(noteTitle);
            noteContentET.setText(noteContent);

        }


        editNoteButton = findViewById(R.id.editNote_btn);

        editNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editNoteInFirebaseRealtimeDatabase();
            }
        });
    }

    private void editNoteInFirebaseRealtimeDatabase() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference rootReference = firebaseDatabase.getReference();
        DatabaseReference notesReference = rootReference.child("Users").child(currentUser.getUid()).child("Notes");
        DatabaseReference particularNoteReference = notesReference.child(noteID);


        // Updating
        // https://firebase.google.com/docs/database/android/read-and-write#update_specific_fields
        particularNoteReference.child("noteTitle").setValue(noteTitleET.getText().toString());
        particularNoteReference.child("noteContent").setValue(noteContentET.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(EditNoteActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(EditNoteActivity.this, "Error: " +task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}