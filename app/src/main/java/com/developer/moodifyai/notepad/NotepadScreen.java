package com.developer.moodifyai.notepad;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.NotesAdapter;
import com.developer.moodifyai.model.Notes;
import com.developer.moodifyai.mood_calender.MoodCalenderScreen;
import com.developer.moodifyai.user_emotion.HappyScreen;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class NotepadScreen extends AppCompatActivity {
    ImageView img_add_notes, btn_back;
    RecyclerView rv_notes;
    ArrayList<Notes> notesList = new ArrayList<>();
    NotesAdapter notesAdapter;
    FirebaseFirestore db;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_notepad_screen);

        img_add_notes = findViewById(R.id.img_add_notes);
        btn_back = findViewById(R.id.btn_back);
        rv_notes = findViewById(R.id.rv_notes);
        progressBar = findViewById(R.id.progressBar);

        rv_notes.setLayoutManager(new LinearLayoutManager(this));
        notesAdapter = new NotesAdapter(this, notesList, this::showOptionsDialog);
        rv_notes.setAdapter(notesAdapter);

        db = FirebaseFirestore.getInstance();
        fetchNotes();

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(NotepadScreen.this, DashboardScreen.class);
            boolean cameFromHome = getIntent().getBooleanExtra("cameFromHome", false);
            if (cameFromHome) {
                intent.putExtra("navigate_to", "HomeFragment");
            } else {
                intent.putExtra("navigate_to", "ExploreFragment");
            }
            startActivity(intent);
            finish();
        });

//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(NotepadScreen.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "ExploreFragment");
//                startActivity(intent);
//                finish();
//            }
//        });

        img_add_notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotepadScreen.this, AddNotes.class));
                finish();
            }
        });
    }

    private void fetchNotes() {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("user_thoughts")
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        notesList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String uid = document.getId();
                            String thought = document.getString("thought");
                            String day = document.getString("day");
                            String time = document.getString("time");
                            String date = document.getString("date");

                            Notes note = new Notes(uid, thought, day, time, date);
                            notesList.add(note);
                        }
                        notesAdapter.notifyDataSetChanged();
                    } else {
                        showSnackbar(NotepadScreen.this, findViewById(R.id.rl_main), "Error fetching notes", true);
                    }
                });
    }

    private void showOptionsDialog(Notes note) {
        BottomSheetDialog dialog = new BottomSheetDialog(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.notes_dialog, null);

        dialog.setContentView(dialogView);
        dialog.show();

        LinearLayout llEdit = dialogView.findViewById(R.id.ll_edit);
        LinearLayout llShare = dialogView.findViewById(R.id.ll_share);
        LinearLayout llDelete = dialogView.findViewById(R.id.ll_delete);

        llEdit.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(NotepadScreen.this, AddNotes.class);
            intent.putExtra("noteId", note.getUid());
            intent.putExtra("thought", note.getThought());
            startActivity(intent);
            finish();
        });

        llShare.setOnClickListener(v -> {
            dialog.dismiss();
            shareThought(note.getThought());
        });

        llDelete.setOnClickListener(v -> {
            dialog.dismiss();
            deleteNoteFromFirebase(note.getUid());
        });
    }

    private void deleteNoteFromFirebase(String uid) {
        progressBar.setVisibility(View.VISIBLE);
        db.collection("user_thoughts").document(uid)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    progressBar.setVisibility(View.GONE);
                    fetchNotes();
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    showSnackbar(NotepadScreen.this, findViewById(R.id.rl_main), "Failed to delete note " + e.getMessage(), true);
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

    private void shareThought(String thought) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, thought);

        Intent chooser = Intent.createChooser(shareIntent, "Share via");
        startActivity(chooser);
    }
}