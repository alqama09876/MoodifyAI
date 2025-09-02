package com.developer.moodifyai.quotes;

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

import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.QuotesAdapter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QuotesCategoryScreen extends AppCompatActivity {

    private static final String TAG = "QuotesCategoryScreen";
    GridView gridView;
    TextView titleText;
    ImageView btn_back;
    ProgressBar progressBar;
    QuotesAdapter quotesAdapter;
    FirebaseFirestore db;
    List<Map<String, Object>> quotesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quotes_category_screen);

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

        quotesList = new ArrayList<>();
        quotesAdapter = new QuotesAdapter(this, quotesList, progressBar);
        gridView.setAdapter(quotesAdapter);

        fetchQuotesForCategory(categoryName);
    }

    private void fetchQuotesForCategory(String categoryName) {
        progressBar.setVisibility(View.VISIBLE);

        String collectionType = getIntent().getStringExtra("COLLECTION_TYPE");
        if (collectionType == null) {
            collectionType = "default_quotes";
        }

        String path;
        if ("default_quotes".equals(collectionType)) {
            path = "quotes/default_quotes/categories/" + categoryName;
        } else {
            path = "quotes/mood_based_quotes/" + collectionType + "/" + categoryName;
        }

        db.document(path)
                .get()
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful() && task.getResult().exists()) {
                        quotesList.clear();
                        List<Map<String, Object>> quotes = (List<Map<String, Object>>) task.getResult().get("quotes");

                        if (quotes != null) {
                            quotesList.addAll(quotes);
                            quotesAdapter.notifyDataSetChanged();
                        }
                    } else {
                        progressBar.setVisibility(View.GONE);
                        Log.e(TAG, "Error fetching quotes", task.getException());
                        Toast.makeText(this, "Failed to load quotes", Toast.LENGTH_SHORT).show();
                    }
                });
    }

//    private void fetchQuotesForCategory(String categoryName) {
//        progressBar.setVisibility(View.VISIBLE);
//
//        db.collection("quotes")
//                .document("default_quotes")
//                .collection("categories")
//                .document(categoryName)
//                .get()
//                .addOnCompleteListener(task -> {
//                    progressBar.setVisibility(View.GONE);
//                    if (task.isSuccessful() && task.getResult().exists()) {
//                        quotesList.clear();
//                        List<Map<String, Object>> quotes = (List<Map<String, Object>>) task.getResult().get("quotes");
//
//                        if (quotes != null) {
//                            quotesList.addAll(quotes);
//                            quotesAdapter.notifyDataSetChanged();
//                        }
//                    } else {
//                        Log.e(TAG, "Error fetching quotes", task.getException());
//                        Toast.makeText(this, "Failed to load quotes", Toast.LENGTH_SHORT).show();
//                    }
//                });
//    }
}