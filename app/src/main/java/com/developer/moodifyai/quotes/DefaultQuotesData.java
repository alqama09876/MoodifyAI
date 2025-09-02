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

public class DefaultQuotesData {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context;

    public DefaultQuotesData(Context context) {
        this.context = context;
    }

    public void uploadQuotesData() {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.quote);
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
                        .document("default_quotes")
                        .collection("categories")
                        .document(categoryName)
                        .set(categoryData)
                        .addOnSuccessListener(aVoid ->
                                System.out.println("Category added with quotes: " + categoryName))
                        .addOnFailureListener(e ->
                                System.err.println("Error adding category document: " + e));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


//package com.developer.moodifyai.quotes;
//
//import android.content.Context;
//import com.developer.moodifyai.R;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.WriteBatch;
//import org.json.JSONArray;
//import org.json.JSONObject;
//import java.io.InputStream;
//import java.nio.charset.StandardCharsets;
//import java.util.HashMap;
//import java.util.Map;
//
//public class DefaultQuotesData {
//    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
//    private final Context context;
//
//    public DefaultQuotesData(Context context) {
//        this.context = context;
//    }
//
//    public void uploadQuotesData() {
//        try {
//            String json = readJsonFromRawResource(R.raw.quote);
//            if (json == null) return;
//
//            JSONArray categoriesArray = new JSONObject(json).getJSONArray("quotes");
//            WriteBatch batch = db.batch();
//
//            for (int i = 0; i < categoriesArray.length(); i++) {
//                JSONObject categoryObj = categoriesArray.getJSONObject(i);
//                String categoryName = categoryObj.getString("category");
//                JSONArray quotesArray = categoryObj.getJSONArray("quotes");
//
//                batch.set(
//                        db.collection("quotes")
//                                .document("default_quotes")
//                                .collection("categories")
//                                .document(categoryName),
//                        createCategoryData(quotesArray)
//                );
//            }
//
//            batch.commit()
//                    .addOnSuccessListener(aVoid -> System.out.println("Quotes uploaded successfully!"))
//                    .addOnFailureListener(e -> System.err.println("Error uploading quotes: " + e));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    private String readJsonFromRawResource(int resourceId) {
//        try (InputStream is = context.getResources().openRawResource(resourceId)) {
//            byte[] buffer = new byte[is.available()];
//            is.read(buffer);
//            return new String(buffer, StandardCharsets.UTF_8);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return null;
//        }
//    }
//
//    private Map<String, Object> createCategoryData(JSONArray quotesArray) {
//        try {
//            Map<String, Object> categoryData = new HashMap<>();
//            for (int j = 0; j < quotesArray.length(); j++) {
//                JSONObject quoteObj = quotesArray.getJSONObject(j);
//                categoryData.put("quote_" + j, Map.of(
//                        "title", quoteObj.getString("title"),
//                        "quote", quoteObj.getString("quote"),
//                        "author", quoteObj.getString("author"),
//                        "image", quoteObj.getString("image")
//                ));
//            }
//            return Map.of("quotes", categoryData);
//        } catch (Exception e) {
//            e.printStackTrace();
//            return new HashMap<>();
//        }
//    }
//}