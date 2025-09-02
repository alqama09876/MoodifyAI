package com.developer.moodifyai.coping_strategies;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.CopingStrategiesAdapter;
import com.developer.moodifyai.model.CopingStrategy;
import com.developer.moodifyai.mood_calender.MoodCalenderScreen;
import com.developer.moodifyai.quotes.QuotesScreen;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class CopingStrategyScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CopingStrategiesAdapter adapter;
    private List<CopingStrategy> copingStrategies;
    ImageView btn_back;
    ProgressBar progressBar;
    FirebaseAuth auth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_coping_strategy_screen);

        CopingStrategiesData copingData = new CopingStrategiesData(this);
        copingData.uploadCopingStrategies();

        progressBar = findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        btn_back = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.rv_coping_strategies);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        copingStrategies = new ArrayList<>();
        adapter = new CopingStrategiesAdapter(this, copingStrategies);
        recyclerView.setAdapter(adapter);

        btn_back.setOnClickListener(v -> {
            Intent intent = new Intent(CopingStrategyScreen.this, DashboardScreen.class);
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
//                Intent intent = new Intent(CopingStrategyScreen.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "ExploreFragment");
//                startActivity(intent);
//                finish();
//            }
//        });

        fetchCopingStrategies();
    }

    private void fetchCopingStrategies() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);

        db.collection("coping_strategies")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            copingStrategies.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                CopingStrategy strategy = document.toObject(CopingStrategy.class);
                                copingStrategies.add(strategy);
                            }
                            recyclerView.setVisibility(View.VISIBLE);
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(CopingStrategyScreen.this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}