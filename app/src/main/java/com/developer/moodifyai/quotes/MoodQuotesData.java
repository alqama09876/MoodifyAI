package com.developer.moodifyai.quotes;

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

public class MoodQuotesData {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context;

    public MoodQuotesData(Context context) {
        this.context = context;
    }

    public void uploadMoodQuotesData() {
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
                JSONArray categoriesArray = jsonObject.getJSONArray("quotes");

                for (int i = 0; i < categoriesArray.length(); i++) {
                    JSONObject categoryObj = categoriesArray.getJSONObject(i);
                    String categoryName = categoryObj.getString("category");
                    JSONArray quotesArray = categoryObj.getJSONArray("quotes");

                    List<Map<String, Object>> quotesList = new ArrayList<>();
                    for (int j = 0; j < quotesArray.length(); j++) {
                        JSONObject quoteObj = quotesArray.getJSONObject(j);

                        Map<String, Object> data = new HashMap<>();
                        data.put("title", quoteObj.getString("title"));
                        data.put("quote", quoteObj.getString("quote"));
                        data.put("author", quoteObj.getString("author"));
                        data.put("image", quoteObj.getString("image"));

                        quotesList.add(data);
                    }

                    Map<String, Object> categoryData = new HashMap<>();
                    categoryData.put("quotes", quotesList);

                    db.collection("quotes")
                            .document("mood_based_quotes")
                            .collection(mood)
                            .document(categoryName)
                            .set(categoryData)
                            .addOnSuccessListener(aVoid ->
                                    System.out.println("Category added with quotes: " + categoryName + " for mood: " + mood))
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