package com.developer.moodifyai.affirmation;

import android.content.Context;

import com.developer.moodifyai.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoodAffirmationsData {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context;

    public MoodAffirmationsData(Context context) {
        this.context = context;
    }

    public void uploadMoodAffirmationsData() {
        Map<String, Integer> moodFiles = new HashMap<>();
        moodFiles.put("Happy", R.raw.happy);
        moodFiles.put("Sad", R.raw.sad);
        moodFiles.put("Angry", R.raw.angry);
        moodFiles.put("Neutral", R.raw.neutral);
        moodFiles.put("Very Happy", R.raw.very_happy);

        for (Map.Entry<String, Integer> entry : moodFiles.entrySet()) {
            String mood = entry.getKey();
            int resourceId = entry.getValue();

            try {
                InputStream is = context.getResources().openRawResource(resourceId);
                byte[] buffer = new byte[is.available()];
                is.read(buffer);
                is.close();

                String json = new String(buffer, StandardCharsets.UTF_8);
                JSONObject jsonObject = new JSONObject(json);
                JSONArray categoriesArray = jsonObject.getJSONArray("affirmations");

                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject categoryObj = categoriesArray.getJSONObject(i);
                    String categoryName = categoryObj.getString("category");
                    JSONArray affirmationsArray = categoryObj.getJSONArray("affirmations");

                    List<Map<String, Object>> affirmationsList = new ArrayList<>();
                    for (int j = 0; j < affirmationsArray.length(); j++) {
                        JSONObject affirmationObj = affirmationsArray.getJSONObject(j);

                        Map<String, Object> data = new HashMap<>();
                        data.put("title", affirmationObj.getString("title"));
                        data.put("affirmation", affirmationObj.getString("affirmation"));
                        data.put("image", affirmationObj.getString("image"));

                        affirmationsList.add(data);
                    }

                    Map<String, Object> categoryData = new HashMap<>();
                    categoryData.put("affirmations", affirmationsList);

                    db.collection("affirmations")
                            .document("mood_based_affirmations")
                            .collection(mood)
                            .document(categoryName)
                            .set(categoryData)
                            .addOnSuccessListener(aVoid ->
                                    System.out.println("Category added with affirmations: " + categoryName + " for mood: " + mood))
                            .addOnFailureListener(e ->
                                    System.err.println("Error adding category document for mood: " + mood + ": " + e));
                }
            } catch (Exception e) {
                System.err.println("Error processing mood: " + mood);
                e.printStackTrace();
            }
        }
    }
}