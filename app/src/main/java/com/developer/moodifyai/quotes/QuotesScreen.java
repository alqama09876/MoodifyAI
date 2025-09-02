package com.developer.moodifyai.quotes;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.utils.MoodUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class QuotesScreen extends AppCompatActivity {

    private static final String TAG = "QuotesScreen";
    LinearLayout sectionsContainer;
    ImageView btn_back;
    FirebaseFirestore db;
    ProgressBar progressBar;
    ImageView img_angry, img_happy, img_neutral, img_sad, img_very_happy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quotes_screen);

        DefaultQuotesData defaultQuotesData = new DefaultQuotesData(this);
        defaultQuotesData.uploadQuotesData();
        MoodQuotesData moodQuotesData = new MoodQuotesData(this);
        moodQuotesData.uploadMoodQuotesData();

        progressBar = findViewById(R.id.progressBar);
        btn_back = findViewById(R.id.btn_back);
        sectionsContainer = findViewById(R.id.sections_container);
        db = FirebaseFirestore.getInstance();

        initializeMoods();

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(QuotesScreen.this, DashboardScreen.class);
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
//                Intent intent = new Intent(QuotesScreen.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "ExploreFragment");
//                startActivity(intent);
//                finish();
//            }
//        });

        fetchUserMood();
    }

    private void fetchUserMood() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userID = auth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        String year = new SimpleDateFormat("yyyy").format(calendar.getTime());
        String month = new SimpleDateFormat("MMMM").format(calendar.getTime());
        String week = "Week " + calendar.get(Calendar.WEEK_OF_MONTH);
        String date = new SimpleDateFormat("dd").format(calendar.getTime());

        db.collection("mood_tracker")
                .document(userID)
                .collection(year)
                .document(month)
                .collection(week)
                .document(date)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String mood = documentSnapshot.getString("mood");
                        if (mood != null) {
                            fetchMoodBasedQuotes(mood);
                        } else {
                            fetchCategoriesFromDatabase("default_quotes");
                        }
                    } else {
                        fetchCategoriesFromDatabase("default_quotes");
                    }
                })
                .addOnFailureListener(e -> {
                    View rootview = getRootView(QuotesScreen.this);
                    showSnackbar(QuotesScreen.this, rootview, "Failed to fetch mood. Using default quotes.", true);
                    fetchCategoriesFromDatabase("default_quotes");
                });
    }

    private void fetchMoodBasedQuotes(String mood) {
        progressBar.setVisibility(View.VISIBLE);

        String moodCollection = mood;

        db.collection("quotes")
                .document("mood_based_quotes")
                .collection(moodCollection)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        sectionsContainer.removeAllViews();

                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String categoryName = document.getId();
                            addCategoryToUI(categoryName, moodCollection);
                        }
                    } else {
                        View rootview = getRootView(QuotesScreen.this);
                        showSnackbar(QuotesScreen.this, rootview, "Failed to load mood-based quotes", true);
                        progressBar.setVisibility(View.GONE);

                        fetchCategoriesFromDatabase("default_quotes");
                    }
                });
    }

    private void fetchCategoriesFromDatabase(String collectionName) {
        progressBar.setVisibility(View.VISIBLE);

        if ("default_quotes".equals(collectionName)) {
            db.collection("quotes")
                    .document("default_quotes")
                    .collection("categories")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sectionsContainer.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String categoryName = document.getId();
                                addCategoryToUI(categoryName, "default_quotes");
                            }
                        } else {
                            View rootview = getRootView(QuotesScreen.this);
                            showSnackbar(QuotesScreen.this, rootview, "Failed to load default quotes", true);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            db.collection("quotes")
                    .document("mood_based_quotes")
                    .collection(collectionName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sectionsContainer.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String categoryName = document.getId();
                                addCategoryToUI(categoryName, "mood_based_quotes");
                            }
                        } else {
                            View rootview = getRootView(QuotesScreen.this);
                            showSnackbar(QuotesScreen.this, rootview, "Failed to load mood-based quotes", true);
                            progressBar.setVisibility(View.GONE);
                            fetchCategoriesFromDatabase("default_quotes");
                        }
                    });
        }
    }

    private void addCategoryToUI(String categoryName, String collectionType) {
        View categoryView = LayoutInflater.from(this).inflate(R.layout.item_section_layout, sectionsContainer, false);

        TextView section_title = categoryView.findViewById(R.id.section_title);
        LinearLayout section_view_all = categoryView.findViewById(R.id.section_view_all);
        LinearLayout section_items_container = categoryView.findViewById(R.id.section_items_container);

        section_title.setText(categoryName);

        String path;
        if ("default_quotes".equals(collectionType)) {
            path = "quotes/default_quotes/categories/" + categoryName;
        } else {
            path = "quotes/mood_based_quotes/" + collectionType + "/" + categoryName;
        }

        db.document(path)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> quotesList = (List<Map<String, Object>>) task.getResult().get("quotes");

                        if (quotesList != null) {
                            for (Map<String, Object> quoteData : quotesList) {
                                addQuoteToSection(section_items_container, quoteData);
                            }
                        }
                    } else {
                        View rootview = getRootView(QuotesScreen.this);
                        showSnackbar(QuotesScreen.this, rootview, "Error fetching quotes for category: " + categoryName, true);
                    }
                });

        section_view_all.setOnClickListener(r -> {
            Intent intent = new Intent(this, QuotesCategoryScreen.class);
            intent.putExtra("CATEGORY_NAME", categoryName);
            intent.putExtra("COLLECTION_TYPE", collectionType);
            startActivity(intent);
        });

        sectionsContainer.addView(categoryView);
    }

    private void addQuoteToSection(LinearLayout sectionItemsContainer, Map<String, Object> quoteData) {
        View quoteView = LayoutInflater.from(this).inflate(R.layout.custom_quotes_layout, sectionItemsContainer, false);

        TextView itemTitle = quoteView.findViewById(R.id.item_title);
        ImageView itemImage = quoteView.findViewById(R.id.item_image);

        String title = (String) quoteData.get("title");
        String quoteText = (String) quoteData.get("quote");
        String author = (String) quoteData.get("author");
        String imageUrl = (String) quoteData.get("image");

        itemTitle.setText(title);

        Glide.with(this)
                .load(imageUrl)
                .into(itemImage);

        progressBar.setVisibility(View.GONE);

        quoteView.setOnClickListener(v -> {
            Intent intent = new Intent(this, QuoteDetailsScreen.class);
            intent.putExtra("QUOTE_TITLE", title);
            intent.putExtra("QUOTE_TEXT", quoteText);
            intent.putExtra("QUOTE_AUTHOR", author);
            intent.putExtra("QUOTE_IMAGE", imageUrl);
            startActivity(intent);
        });

        sectionItemsContainer.addView(quoteView);
    }

    private void showMoodDialog(String moodType) {
        Dialog moodDialog = new Dialog(this);
        moodDialog.setContentView(R.layout.mood_dialog);

        moodDialog.getWindow().setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );

        moodDialog.setCanceledOnTouchOutside(false);

        ImageView imgMoodType = moodDialog.findViewById(R.id.img_mood_type);
        TextView txtMoodType = moodDialog.findViewById(R.id.txt_mood_type);
        Button btnTrackMood = moodDialog.findViewById(R.id.btn_track_mood);
        Button btnCancel = moodDialog.findViewById(R.id.btn_cancel);

        if (moodType.equals("Angry")) {
            imgMoodType.setImageResource(R.drawable.very_unhappy);
            txtMoodType.setText("Angry");
        } else if (moodType.equals("Happy")) {
            imgMoodType.setImageResource(R.drawable.happy);
            txtMoodType.setText("Happy");
        } else if (moodType.equals("Neutral")) {
            imgMoodType.setImageResource(R.drawable.neutral);
            txtMoodType.setText("Neutral");
        } else if (moodType.equals("Sad")) {
            imgMoodType.setImageResource(R.drawable.unhappy);
            txtMoodType.setText("Sad");
        } else if (moodType.equals("Very Happy")) {
            imgMoodType.setImageResource(R.drawable.very_happy);
            txtMoodType.setText("Very Happy");
        }

        btnTrackMood.setOnClickListener(v -> {
            MoodUtils.saveMood(this, moodType);
            moodDialog.dismiss();
            fetchUserMood();
        });

        btnCancel.setOnClickListener(v -> {
            moodDialog.dismiss();
        });

        moodDialog.show();
    }

    private void initializeMoods() {

        img_angry = findViewById(R.id.img_angry);
        img_happy = findViewById(R.id.img_happy);
        img_neutral = findViewById(R.id.img_neutral);
        img_sad = findViewById(R.id.img_sad);
        img_very_happy = findViewById(R.id.img_very_happy);

        img_angry.setOnClickListener(v -> {
            showMoodDialog("Angry");
        });

        img_happy.setOnClickListener(v -> {
            showMoodDialog("Happy");
        });

        img_neutral.setOnClickListener(v -> {
            showMoodDialog("Neutral");
        });

        img_sad.setOnClickListener(v -> {
            showMoodDialog("Sad");
        });

        img_very_happy.setOnClickListener(v -> {
            showMoodDialog("Very Happy");
        });
    }

    private static View getRootView(Context context) {
        try {
            return ((android.app.Activity) context).findViewById(R.id.ll_main);
        } catch (Exception e) {
            Toast.makeText(context, "Unable to find root view", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public static void showSnackbar(Context context, View view, String message, boolean isError) {
        if (view == null) {
            view = getRootView(context);
        }

        if (view != null) {
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
        } else {
            Toast.makeText(context, message, Toast.LENGTH_LONG).show();
        }
    }
}