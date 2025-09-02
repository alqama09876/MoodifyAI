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

public class DefaultAffirmationData {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context;

    public DefaultAffirmationData(Context context) {
        this.context = context;
    }

    public void uploadAffirmationsData() {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.affirmation);
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
                        .document("default_affirmations")
                        .collection("categories")
                        .document(categoryName)
                        .set(categoryData)
                        .addOnSuccessListener(aVoid ->
                                System.out.println("Category added with affirmations: " + categoryName))
                        .addOnFailureListener(e ->
                                System.err.println("Error adding category document: " + e));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}