package com.developer.moodifyai.affirmation;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.AffirmationsAdapter;
import com.developer.moodifyai.adapter.QuotesAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AffirmationCategoryScreen extends AppCompatActivity {

    private static final String TAG = "AffirmationCategoryScreen";
    GridView gridView;
    TextView titleText;
    ImageView btn_back;
    ProgressBar progressBar;
    AffirmationsAdapter affirmationsAdapter;
    FirebaseFirestore db;
    List<Map<String, Object>> affirmationsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_affirmation_category_screen);

        gridView = findViewById(R.id.gridView);
        titleText = findViewById(R.id.titleText);
        btn_back = findViewById(R.id.btn_back);
        progressBar = findViewById(R.id.progressBar);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        db = FirebaseFirestore.getInstance();

        String categoryName = getIntent().getStringExtra("CATEGORY_NAME");
        titleText.setText(categoryName);

        affirmationsList = new ArrayList<>();
        affirmationsAdapter = new AffirmationsAdapter(this, affirmationsList, progressBar);
        gridView.setAdapter(affirmationsAdapter);

        fetchAffirmationsForCategory(categoryName);
    }

    private void fetchAffirmationsForCategory(String categoryName) {
        progressBar.setVisibility(View.VISIBLE);

        String collectionType = getIntent().getStringExtra("COLLECTION_TYPE");
        if (collectionType == null) {
            collectionType = "default_affirmations";
        }

        String path;
        if ("default_affirmations".equals(collectionType)) {
            path = "affirmations/default_affirmations/categories/" + categoryName;
        } else {
            path = "affirmations/mood_based_affirmations/" + collectionType + "/" + categoryName;
        }

        db.document(path)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful() && task.getResult().exists()) {
                        affirmationsList.clear();
                        List<Map<String, Object>> affirmations = (List<Map<String, Object>>) task.getResult().get("affirmations");

                        if (affirmations != null) {
                            affirmationsList.addAll(affirmations);
                            affirmationsAdapter.notifyDataSetChanged();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "Error fetching affirmations", task.getException());
                        Toast.makeText(this, "Failed to load affirmations", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}