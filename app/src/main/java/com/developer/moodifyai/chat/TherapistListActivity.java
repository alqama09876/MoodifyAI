package com.developer.moodifyai.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.TherapistAdapter;
import com.developer.moodifyai.model.Therapist;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class TherapistListActivity extends AppCompatActivity implements TherapistAdapter.OnChatClickListener {

    private RecyclerView recyclerView;
    private TherapistAdapter adapter;
    private List<Therapist> therapistList;
    private FirebaseFirestore firestore;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_therapist_list);

        initializeViews();
        setupRecyclerView();
        fetchTherapists();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_therapists);

        btnBack.setOnClickListener(v -> navigateToDashboard());
    }

    private void setupRecyclerView() {
        therapistList = new ArrayList<>();
        adapter = new TherapistAdapter(therapistList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchTherapists() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("therapists")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    therapistList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        Therapist therapist = doc.toObject(Therapist.class);
                        therapist.setUid(doc.getId());
                        therapistList.add(therapist);
                    }
                    adapter.updateData(therapistList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load therapists: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("TherapistList", "Error loading therapists", e);
                });
    }

    @Override
    public void onChatClick(Therapist therapist) {
        startChatWithTherapist(therapist);
    }

    private void startChatWithTherapist(Therapist therapist) {
        Intent intent = new Intent(this, com.developer.moodifyai.chat.ChatActivity.class);
        intent.putExtra("receiverId", therapist.getUid());
        intent.putExtra("therapistName", therapist.getName());
        startActivity(intent);
    }

    private void navigateToDashboard() {
        Intent intent = new Intent(this, DashboardScreen.class);
        intent.putExtra("navigate_to", "HomeFragment");
        startActivity(intent);
        finish();
    }
}


//package com.developer.moodifyai.chat;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.developer.moodifyai.DashboardScreen;
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.adapter.TherapistAdapter;
//import com.developer.moodifyai.model.Therapist;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TherapistListActivity extends AppCompatActivity implements TherapistAdapter.OnChatClickListener {
//
//    private RecyclerView recyclerView;
//    private TherapistAdapter adapter;
//    private List<Therapist> therapistList;
//    private FirebaseFirestore firestore;
//    private ImageView btnBack;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_therapist_list);
//
//        initializeViews();
//        setupRecyclerView();
//        fetchTherapists();
//    }
//
//    private void initializeViews() {
//        btnBack = findViewById(R.id.btn_back);
//        recyclerView = findViewById(R.id.recycler_therapists);
//
//        btnBack.setOnClickListener(v -> navigateToDashboard());
//    }
//
//    private void setupRecyclerView() {
//        therapistList = new ArrayList<>();
//        adapter = new TherapistAdapter(therapistList, this);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(adapter);
//    }
//
//    private void fetchTherapists() {
//        firestore = FirebaseFirestore.getInstance();
//        firestore.collection("therapists")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    therapistList.clear();
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        Therapist therapist = doc.toObject(Therapist.class);
//                        therapist.setUid(doc.getId()); // Ensure UID is set from document ID
//                        therapistList.add(therapist);
//                    }
//                    adapter.updateData(therapistList); // Use the new updateData method
//                })
//                .addOnFailureListener(e -> {
//                    Toast.makeText(this, "Failed to load therapists: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.e("TherapistList", "Error loading therapists", e);
//                });
//    }
//
//    @Override
//    public void onChatClick(Therapist therapist) {
//        if (therapist.isAvailable()) {
//            startChatWithTherapist(therapist);
//        } else {
//            Toast.makeText(this, therapist.getName() + " is currently unavailable", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    private void startChatWithTherapist(Therapist therapist) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra("receiverId", therapist.getUid()); // Using getUid() instead of getId()
//        intent.putExtra("therapistName", therapist.getName());
//        intent.putExtra("profileImageUrl", therapist.getProfileImageUrl()); // Pass profile image URL
//        startActivity(intent);
//    }
//
//    private void navigateToDashboard() {
//        Intent intent = new Intent(this, DashboardScreen.class);
//        intent.putExtra("navigate_to", "HomeFragment");
//        startActivity(intent);
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        // Clean up any listeners if needed
//    }
//}




//package com.developer.moodifyai.chat;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.developer.moodifyai.DashboardScreen;
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.adapter.TherapistAdapter;
//import com.developer.moodifyai.model.Therapist;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.QueryDocumentSnapshot;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class TherapistListActivity extends AppCompatActivity {
//
//    private RecyclerView recyclerView;
//    private TherapistAdapter adapter;
//    private List<Therapist> therapistList;
//    private FirebaseFirestore firestore;
//    private ImageView btn_back;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_therapist_list);
//
//        btn_back = findViewById(R.id.btn_back);
//        recyclerView = findViewById(R.id.recycler_therapists);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        therapistList = new ArrayList<>();
//        adapter = new TherapistAdapter(therapistList, this::startChatWithTherapist);
//        recyclerView.setAdapter(adapter);
//
//        firestore = FirebaseFirestore.getInstance();
//        fetchTherapists();
//
//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(TherapistListActivity.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "HomeFragment");
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    private void fetchTherapists() {
//        firestore.collection("therapists")
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    therapistList.clear();
//                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
//                        Therapist therapist = doc.toObject(Therapist.class);
//                        therapistList.add(therapist);
//                    }
//                    adapter.notifyDataSetChanged();
//                })
//                .addOnFailureListener(e ->
//                        Toast.makeText(this, "Failed to load therapists.", Toast.LENGTH_SHORT).show()
//                );
//    }
//
//    private void startChatWithTherapist(Therapist therapist) {
//        Intent intent = new Intent(this, ChatActivity.class);
//        intent.putExtra("therapistId", therapist.getUid());
//        intent.putExtra("therapistName", therapist.getName());
//        startActivity(intent);
//    }
//}