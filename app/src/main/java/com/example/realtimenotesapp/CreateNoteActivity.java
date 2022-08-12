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

public class CreateNoteActivity extends AppCompatActivity {

    private EditText noteTitle, noteContent;
    private Button createNoteButton;
    private CreateNoteActivity mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_note);

        noteTitle = findViewById(R.id.noteTitle_ET);
        noteContent = findViewById(R.id.noteContent_ET);
        createNoteButton = findViewById(R.id.createNotes_btn);

        createNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                putNoteInFirebaseRealtimeDatabase();
            }
        });
        mContext = CreateNoteActivity.this;
    }

    private void putNoteInFirebaseRealtimeDatabase() {

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference notesReference = rootReference.child("Users").child(currentUser.getUid()).child("Notes");

        DatabaseReference newNoteReference = notesReference.push();

        Note newNote = new Note(noteTitle.getText().toString(), noteContent.getText().toString());
        newNoteReference.setValue(newNote).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            if (task.isSuccessful()){
                Toast.makeText(CreateNoteActivity.this,"Note saved!", Toast.LENGTH_SHORT).show();
                mContext.finish();
            } else {
                Toast.makeText(CreateNoteActivity.this,"Error: " +task.getException(), Toast.LENGTH_SHORT).show();
            }
            }
        });
    }
}