package com.developer.moodifyai.chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.developer.moodifyai.TherapistDashboardScreen;
import com.developer.moodifyai.adapter.TherapistAdapter;
import com.developer.moodifyai.adapter.UserAdapter;
import com.developer.moodifyai.model.Therapist;
import com.developer.moodifyai.model.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserListActivity extends AppCompatActivity implements UserAdapter.OnChatClickListener{

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private List<User> userList;
    private FirebaseFirestore firestore;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_list);

        initializeViews();
        setupRecyclerView();
        fetchUsers();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btn_back);
        recyclerView = findViewById(R.id.recycler_patient);

        btnBack.setOnClickListener(v -> navigateToTherapistDashboard());
    }

    private void setupRecyclerView() {
        userList = new ArrayList<>();
        adapter = new UserAdapter(userList,this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    private void fetchUsers() {
        firestore = FirebaseFirestore.getInstance();
        firestore.collection("users")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    userList.clear();

                    int totalUsers = queryDocumentSnapshots.size();
                    if (totalUsers == 0) {
                        Log.d("FirestoreDebug", "No users found in 'users' collection");
                        return;
                    }

                    int[] loadedCount = {0};  // counter for loaded users

                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String uid = doc.getId();
                        String email = doc.getString("email");
                        String role = doc.getString("role");

                        Log.d("FirestoreDebug", "UID: " + uid + ", Email: " + email + ", Role: " + role);

                        firestore.collection("user_personal_data").document(uid)
                                .get()
                                .addOnSuccessListener(personalDoc -> {
                                    String userName = personalDoc.getString("userName");

                                    Log.d("FirestoreDebug", "UserName for UID " + uid + ": " + userName);

                                    User user = new User();
                                    user.setUid(uid);
                                    user.setEmail(email);
                                    user.setRole(role);
                                    user.setName(userName != null ? userName : "Unknown");

                                    userList.add(user);
                                    loadedCount[0]++;

                                    if (loadedCount[0] == totalUsers) {
                                        adapter.updateData(userList);
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Log.e("FirestoreDebug", "Failed to get user_personal_data for UID: " + uid, e);
                                    loadedCount[0]++;
                                    if (loadedCount[0] == totalUsers) {
                                        adapter.updateData(userList);
                                    }
                                });
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Failed to load users: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FirestoreDebug", "Error loading users", e);
                });
    }

    @Override
    public void onChatClick(User user) {
        startChatWithUser(user);
    }

    private void startChatWithUser(User user) {
        Intent intent = new Intent(this, com.developer.moodifyai.chat.ChatActivity.class);
        intent.putExtra("receiverId", user.getUid());
        intent.putExtra("userName", user.getName());
        startActivity(intent);
    }

    private void navigateToTherapistDashboard() {
        Intent intent = new Intent(this, TherapistDashboardScreen.class);
        intent.putExtra("navigate_to", "TherapistHomeFragment");
        startActivity(intent);
        finish();
    }
}