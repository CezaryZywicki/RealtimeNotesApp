package com.example.realtimenotesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private TextView welcomeMessageTV;
    private Button createNoteBtn;
    private RecyclerView recyclerView;

    ArrayList<Note> noteArrayList = new ArrayList<>();

    NotesAdapter notesAdapter;
    Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mContext = this;

        // Get the name of the user in Welcome Message

        welcomeMessageTV = findViewById(R.id.welcomemessageInHome_TV);
        createNoteBtn = findViewById(R.id.createNotes_btn);

        // Get Name from Firebase

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference rootReference = firebaseDatabase.getReference();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference nameReference = rootReference.child("Users").child(currentUser.getUid()).child("name");


        //Reading Name from Firebase:
        // https://firebase.google.com/docs/database/android/read-and-write#read_data_with_persistent_listeners
        // Example of https://firebase.google.com/docs/database/android/read-and-write#read_once_using_a_listener
        nameReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                // Here DataSnapshot {name: "Cezary"}
                welcomeMessageTV.setText("Hi! Welcome " + snapshot.getValue().toString() + " !");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        createNoteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNoteActivity();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);

        // Setting up Linear Layout for Recycler View
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);



        // Reading notes from Firebase
        readNotesFromFirebaseRealtimeDatabase();
    }

    private void readNotesFromFirebaseRealtimeDatabase() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(currentUser.getUid()).child("Notes");

        // https://firebase.google.com/docs/database/android/read-and-write#read_data_with_persistent_listeners
        notesReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                noteArrayList.clear();
                Note note;

                for(DataSnapshot noteSnapshot : snapshot.getChildren()) {
                    note = noteSnapshot.getValue(Note.class);
                    note.setNoteID(noteSnapshot.getKey());
                    Toast.makeText(HomeActivity.this, "note : title : "+note.getNoteTitle() + "content : "+note.getNoteContent(), Toast.LENGTH_SHORT).show();
                    Log.i("mynote", "note : title : "+note.getNoteTitle() + "content : "+note.getNoteContent());

                    // Adding notes to arrayList of Notes class
                    noteArrayList.add(note);
                }

                // TODO: SetUp Layout
                notesAdapter = new NotesAdapter(noteArrayList, mContext);
                recyclerView.setAdapter(notesAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openCreateNoteActivity() {

        //TODO: Open Create Note Activity
        Intent createNoteIntent = new Intent(HomeActivity.this, CreateNoteActivity.class);
        startActivity(createNoteIntent);
    }

    public void deletNoteFromFirebase(String noteID) {
        //delete the note in Firebase;

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        DatabaseReference notesReference = firebaseDatabase.getReference().child("Users").child(currentUser.getUid()).child("Notes");

        DatabaseReference particularNote = notesReference.child(noteID);

        //delete method in Firebase

        particularNote.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    Toast.makeText(mContext, "Note is deleted, successfully!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Toast.makeText(mContext, "Some error occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.useraccount:

                Intent userAccountIntent = new Intent(HomeActivity.this, UserAccountActivity.class);
                startActivity(userAccountIntent);
                break;

            case R.id.logout:
                FirebaseAuth.getInstance().signOut();
                this.finish();
                Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(loginIntent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}