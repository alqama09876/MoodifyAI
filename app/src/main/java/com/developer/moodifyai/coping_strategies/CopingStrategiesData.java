package com.developer.moodifyai.coping_strategies;

import android.content.Context;

import com.developer.moodifyai.R;
import com.developer.moodifyai.model.CopingStrategy;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CopingStrategiesData {
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final Context context;

    public CopingStrategiesData(Context context) {
        this.context = context;
    }

    public void uploadCopingStrategies() {
        try {
            InputStream is = context.getResources().openRawResource(R.raw.coping_strategies);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            JSONObject strategiesObject = jsonObject.getJSONObject("coping_strategies");

            Iterator<String> keys = strategiesObject.keys();
            while (keys.hasNext()) {
                String key = keys.next();
                JSONObject strategyObject = strategiesObject.getJSONObject(key);

                String title = strategyObject.getString("title");
                String description = strategyObject.getString("description");
                String subTitle = strategyObject.getString("subTitle");
                String image = strategyObject.getString("image");

                JSONArray subDescArray = strategyObject.getJSONArray("subDescription");
                List<String> subDescriptions = new ArrayList<>();
                for (int i = 0; i < subDescArray.length(); i++) {
                    subDescriptions.add(subDescArray.getString(i));
                }

                CopingStrategy copingStrategy = new CopingStrategy(title, description, subTitle, subDescriptions, image);

                db.collection("coping_strategies")
                        .document(key)
                        .set(copingStrategy)
                        .addOnSuccessListener(aVoid ->
                                System.out.println("Coping strategy added: " + key))
                        .addOnFailureListener(e ->
                                System.err.println("Error adding coping strategy: " + e));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}