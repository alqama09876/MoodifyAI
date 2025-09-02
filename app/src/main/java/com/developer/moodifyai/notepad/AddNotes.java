package com.developer.moodifyai.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AddNotes extends AppCompatActivity {

    ImageView btn_close;
    EditText edt_notes;
    AppCompatButton btn_save;
    FirebaseAuth auth;
    FirebaseFirestore db;
    String noteId;
    TextView txt_note;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_notes);

        btn_close = findViewById(R.id.btn_close);
        edt_notes = findViewById(R.id.edt_notes);
        btn_save = findViewById(R.id.btn_save);
        txt_note = findViewById(R.id.txt_note);
        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        if (intent != null) {
            noteId = intent.getStringExtra("noteId");
            String thought = intent.getStringExtra("thought");
            if (noteId != null && thought != null) {
                txt_note.setText("Edit notes");
                edt_notes.setText(thought);
                btn_save.setText("Edit");
            }
        }

        btn_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(AddNotes.this, NotepadScreen.class));
                finish();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String thought = edt_notes.getText().toString().trim();
                if (thought.isEmpty()) {
                    edt_notes.setError("Write Something");
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    if (noteId != null) {
                        updateNoteInDatabase(thought);
                    } else {
                        saveToDatabase(thought);
                    }
                }
            }
        });
    }

    private void saveToDatabase(String thought) {
        String uid = auth.getCurrentUser().getUid();

        java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat("EEEE");
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm a");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");

        java.util.Date currentDate = new java.util.Date();

        String currentDay = dayFormat.format(currentDate);
        String currentTime = timeFormat.format(currentDate);
        String currentDateFormatted = dateFormat.format(currentDate);

        Map<String, Object> user_notes = new HashMap<>();
        user_notes.put("thought", thought);
        user_notes.put("day", currentDay);
        user_notes.put("time", currentTime);
        user_notes.put("userId", uid);
        user_notes.put("date", currentDateFormatted);

        db.collection("user_thoughts").add(user_notes).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                progressBar.setVisibility(View.GONE);
                if (task.isSuccessful()) {
                    showSnackbar(AddNotes.this, findViewById(R.id.rl_main), "Note has been saved", false);
                    startActivity(new Intent(AddNotes.this, NotepadScreen.class));
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.GONE);
                showSnackbar(AddNotes.this, findViewById(R.id.rl_main), "Error: " + e.getMessage(), true);
            }
        });
    }

    private void updateNoteInDatabase(String thought) {
        java.text.SimpleDateFormat dayFormat = new java.text.SimpleDateFormat("EEEE");
        java.text.SimpleDateFormat timeFormat = new java.text.SimpleDateFormat("hh:mm a");
        java.text.SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("dd-MM-yyyy");

        java.util.Date currentDate = new java.util.Date();

        String currentDay = dayFormat.format(currentDate);
        String currentTime = timeFormat.format(currentDate);
        String currentDateFormatted = dateFormat.format(currentDate);

        Map<String, Object> updatedNote = new HashMap<>();
        updatedNote.put("thought", thought);
        updatedNote.put("day", currentDay);
        updatedNote.put("time", currentTime);
        updatedNote.put("date", currentDateFormatted);

        db.collection("user_thoughts").document(noteId)
                .update(updatedNote)
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(AddNotes.this, findViewById(R.id.rl_main), "Note has been updated", false);
                    startActivity(new Intent(AddNotes.this, NotepadScreen.class));
                    finish();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(AddNotes.this, findViewById(R.id.rl_main), "Error: " + e.getMessage(), true);
                });
    }

    public static void showSnackbar(Context context, View view, String message, boolean isError) {
        if (view == null) {
            view = ((android.app.Activity) context).findViewById(R.id.rl_main);
        }

        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);

        if (isError) {
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.red));
            snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            snackbar.setBackgroundTint(ContextCompat.getColor(context, R.color.olive_green));
            snackbar.setTextColor(ContextCompat.getColor(context, R.color.white));
        }

        snackbar.setAction("DISMISS", v -> snackbar.dismiss());
        snackbar.setActionTextColor(ContextCompat.getColor(context, R.color.white));

        snackbar.show();
    }
}