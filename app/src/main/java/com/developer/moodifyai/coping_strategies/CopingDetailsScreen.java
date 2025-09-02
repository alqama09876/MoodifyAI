package com.developer.moodifyai.coping_strategies;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.developer.moodifyai.R;
import com.developer.moodifyai.model.CopingStrategy;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class CopingDetailsScreen extends AppCompatActivity {
    private ImageView btnBack, csImage;
    private TextView csTitle, csDescription, csSubtitle, csSubDescription;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coping_details_screen);

        btnBack = findViewById(R.id.btn_back);
        csImage = findViewById(R.id.cs_image);
        csTitle = findViewById(R.id.cs_title);
        csDescription = findViewById(R.id.cs_description);
        csSubtitle = findViewById(R.id.cs_subtitle);
        csSubDescription = findViewById(R.id.cs_subDescription);
        progressBar = findViewById(R.id.progressBar);

        String title = getIntent().getStringExtra("title");
        String description = getIntent().getStringExtra("description");
        String subTitle = getIntent().getStringExtra("subTitle");
        ArrayList<String> subDescription = getIntent().getStringArrayListExtra("subDescription");
        String image = getIntent().getStringExtra("image");

        csTitle.setText(title);
        csDescription.setText(description);
        csSubtitle.setText(subTitle);

        if (subDescription != null && !subDescription.isEmpty()) {
            StringBuilder subDescText = new StringBuilder();
            for (String desc : subDescription) {
                subDescText.append(desc).append("\n");
            }
            csSubDescription.setText(subDescText.toString());
        }

        Glide.with(this).load(image).into(csImage);

        fetchCopingStrategyDetails(title);

        btnBack.setOnClickListener(v -> finish());

    }

    private void fetchCopingStrategyDetails(String title) {
        progressBar.setVisibility(View.VISIBLE);
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("coping_strategies")
                .whereEqualTo("title", title)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        if (!task.getResult().isEmpty()) {
                            QueryDocumentSnapshot document = (QueryDocumentSnapshot) task.getResult().getDocuments().get(0);
                            CopingStrategy strategy = document.toObject(CopingStrategy.class);
                            csDescription.setText(strategy.getDescription());
                            csSubtitle.setText(strategy.getSubTitle());
                        }
                    } else {
                        Toast.makeText(CopingDetailsScreen.this, "Failed to load details", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}