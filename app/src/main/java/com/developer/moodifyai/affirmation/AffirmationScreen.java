package com.developer.moodifyai.affirmation;

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
import com.developer.moodifyai.mood_calender.MoodCalenderScreen;
import com.developer.moodifyai.quotes.QuoteDetailsScreen;
import com.developer.moodifyai.quotes.QuotesCategoryScreen;
import com.developer.moodifyai.quotes.QuotesScreen;
import com.developer.moodifyai.utils.MoodUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class AffirmationScreen extends AppCompatActivity {
    private static final String TAG = "AffirmationScreen";
    LinearLayout sectionsContainer;
    ImageView btn_back;
    FirebaseFirestore db;
    ProgressBar progressBar;
    ImageView img_angry, img_happy, img_neutral, img_sad, img_very_happy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_affirmation_screen);

        DefaultAffirmationData affirmationData = new DefaultAffirmationData(this);
        affirmationData.uploadAffirmationsData();
        MoodAffirmationsData moodAffirmationsData = new MoodAffirmationsData(this);
        moodAffirmationsData.uploadMoodAffirmationsData();

        progressBar = findViewById(R.id.progressBar);
        btn_back = findViewById(R.id.btn_back);
        sectionsContainer = findViewById(R.id.sections_container);
        db = FirebaseFirestore.getInstance();

        initializeMoods();

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(AffirmationScreen.this, DashboardScreen.class);
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
//                Intent intent = new Intent(AffirmationScreen.this, DashboardScreen.class);
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
                            fetchMoodBasedAffirmations(mood);
                        } else {
                            fetchCategoriesFromDatabase("default_affirmations");
                        }
                    } else {
                        fetchCategoriesFromDatabase("default_affirmations");
                    }
                })
                .addOnFailureListener(e -> {
                    View rootview = getRootView(AffirmationScreen.this);
                    showSnackbar(AffirmationScreen.this, rootview, "Failed to fetch mood. Using default affirmations.", true);
                    fetchCategoriesFromDatabase("default_affirmations");
                });
    }

    private void fetchMoodBasedAffirmations(String mood) {
        progressBar.setVisibility(View.VISIBLE);

        String moodCollection = mood;

        db.collection("affirmations")
                .document("mood_based_affirmations")
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
                        View rootview = getRootView(AffirmationScreen.this);
                        showSnackbar(AffirmationScreen.this, rootview, "Failed to load mood-based affirmations", true);
                        progressBar.setVisibility(View.GONE);

                        fetchCategoriesFromDatabase("default_quotes");
                    }
                });
    }

    private void fetchCategoriesFromDatabase(String collectionName) {
        progressBar.setVisibility(View.VISIBLE);

        if ("default_affirmations".equals(collectionName)) {
            db.collection("affirmations")
                    .document("default_affirmations")
                    .collection("categories")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sectionsContainer.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String categoryName = document.getId();
                                addCategoryToUI(categoryName, "default_affirmations");
                            }
                        } else {
                            View rootview = getRootView(AffirmationScreen.this);
                            showSnackbar(AffirmationScreen.this, rootview, "Failed to load default affirmations", true);
                            progressBar.setVisibility(View.GONE);
                        }
                    });
        } else {
            db.collection("affirmations")
                    .document("mood_based_affirmations")
                    .collection(collectionName)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            sectionsContainer.removeAllViews();

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String categoryName = document.getId();
                                addCategoryToUI(categoryName, "mood_based_affirmations");
                            }
                        } else {
                            View rootview = getRootView(AffirmationScreen.this);
                            showSnackbar(AffirmationScreen.this, rootview, "Failed to load mood-based affirmations", true);
                            progressBar.setVisibility(View.GONE);
                            fetchCategoriesFromDatabase("default_affirmations");
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
        if ("default_affirmations".equals(collectionType)) {
            path = "affirmations/default_affirmations/categories/" + categoryName;
        } else {
            path = "affirmations/mood_based_affirmations/" + collectionType + "/" + categoryName;
        }

        db.document(path)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<Map<String, Object>> affirmationList = (List<Map<String, Object>>) task.getResult().get("affirmations");

                        if (affirmationList != null) {
                            for (Map<String, Object> affirmationData : affirmationList) {
                                addAffirmationToSection(section_items_container, affirmationData);
                            }
                        }
                    } else {
                        View rootview = getRootView(AffirmationScreen.this);
                        showSnackbar(AffirmationScreen.this, rootview, "Error fetching affirmations for category: " + categoryName, true);
                    }
                });

        section_view_all.setOnClickListener(r -> {
            Intent intent = new Intent(this, AffirmationCategoryScreen.class);
            intent.putExtra("CATEGORY_NAME", categoryName);
            intent.putExtra("COLLECTION_TYPE", collectionType);
            startActivity(intent);
        });

        sectionsContainer.addView(categoryView);
    }

    private void addAffirmationToSection(LinearLayout sectionItemsContainer, Map<String, Object> affirmationData) {
        View affirmationView = LayoutInflater.from(this).inflate(R.layout.custom_quotes_layout, sectionItemsContainer, false);

        TextView itemTitle = affirmationView.findViewById(R.id.item_title);
        ImageView itemImage = affirmationView.findViewById(R.id.item_image);

        String title = (String) affirmationData.get("title");
        String affirmationText = (String) affirmationData.get("affirmation");
        String imageUrl = (String) affirmationData.get("image");

        itemTitle.setText(title);

        Glide.with(this)
                .load(imageUrl)
                .into(itemImage);

        progressBar.setVisibility(View.GONE);

        affirmationView.setOnClickListener(v -> {
            Intent intent = new Intent(this, AffirmationDetailsScreen.class);
            intent.putExtra("AFFIRMATION_TITLE", title);
            intent.putExtra("AFFIRMATION_TEXT", affirmationText);
            intent.putExtra("AFFIRMATION_IMAGE", imageUrl);
            startActivity(intent);
        });

        sectionItemsContainer.addView(affirmationView);
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