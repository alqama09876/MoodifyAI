package com.developer.moodifyai.chatbot;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
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
import com.developer.moodifyai.adapter.ChatAdapter;
import com.developer.moodifyai.api.ApiClient;
import com.developer.moodifyai.api.ChatbotApiService;
import com.developer.moodifyai.model.ChatMessage;
import com.developer.moodifyai.model.ChatRequest;
import com.developer.moodifyai.model.ChatResponse;
import com.developer.moodifyai.model.FirestoreChatMessage;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatbotActivity extends AppCompatActivity {

    private static final String TAG = "ChatbotActivity";
    private RecyclerView chatRecyclerView;
    private ChatAdapter chatAdapter;
    private List<ChatMessage> messageList = new ArrayList<>();
    private EditText editTextMessage;
    private ImageButton sendMessageBtn;
    private ChatbotApiService apiService;
    private ImageView btn_back;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore db;
    private String currentSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chatbot);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rl_header), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize Firebase instances
        firebaseAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        // Initialize views
        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        editTextMessage = findViewById(R.id.editTextMessage);
        sendMessageBtn = findViewById(R.id.sendMessageBtn);
        btn_back = findViewById(R.id.btn_back);

        // Setup RecyclerView
        chatAdapter = new ChatAdapter(messageList);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        chatRecyclerView.setLayoutManager(layoutManager);
        chatRecyclerView.setAdapter(chatAdapter);

        // Initialize Retrofit API service
        apiService = ApiClient.getClient().create(ChatbotApiService.class);

        // Start a new chat session
        startNewChatSession();

        // Add welcome message when activity starts
        String welcomeMessage = "Hello there! 😊 I'm MoodifyAI, your mental wellness companion.\nHow can I assist you today?";
        addBotMessage(welcomeMessage);

        // Send button click listener
        sendMessageBtn.setOnClickListener(v -> sendUserMessage());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatbotActivity.this, DashboardScreen.class);
                intent.putExtra("navigate_to", "HomeFragment");
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        });
    }

    private void startNewChatSession() {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) return;

        // Create a new chat session document
        Map<String, Object> sessionData = new HashMap<>();
        sessionData.put("userId", currentUser.getUid());
        sessionData.put("startTime", Timestamp.now());
        sessionData.put("status", "active");

        db.collection("chat_sessions")
                .add(sessionData)
                .addOnSuccessListener(documentReference -> {
                    currentSessionId = documentReference.getId();
                    Log.d(TAG, "New chat session started: " + currentSessionId);
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error creating chat session", e);
                });
    }

    private void saveMessageToFirestore(String content, String sender) {
        if (currentSessionId == null) {
            Log.w(TAG, "Cannot save message - no active session");
            return;
        }

        FirestoreChatMessage message = new FirestoreChatMessage(
                content,
                sender,
                Timestamp.now()
        );

        db.collection("chat_sessions")
                .document(currentSessionId)
                .collection("messages")
                .add(message)
                .addOnSuccessListener(documentReference -> {
                    Log.d(TAG, "Message saved to Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Error saving message to Firestore", e);
                });
    }

    private void sendUserMessage() {
        String message = editTextMessage.getText().toString().trim();
        if (!message.isEmpty()) {
            addUserMessage(message);
            editTextMessage.setText("");
            sendMessageToApi(message);

            // Save user message to Firestore
            saveMessageToFirestore(message, "user");
        }
    }

    private void addUserMessage(String message) {
        ChatMessage userMessage = new ChatMessage(message, ChatMessage.TYPE_USER);
        messageList.add(userMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void addBotMessage(String message) {
        ChatMessage botMessage = new ChatMessage(message, ChatMessage.TYPE_BOT);
        messageList.add(botMessage);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();

        // Save bot message to Firestore
        saveMessageToFirestore(message, "bot");
    }

    private void scrollToBottom() {
        if (messageList.size() > 0) {
            chatRecyclerView.post(() -> {
                chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            });
        }
    }

    private void sendMessageToApi(String userMessage) {
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Please login to use the chatbot", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();
        addTypingIndicator();

        ChatRequest request = new ChatRequest(userMessage, userId);
        apiService.getBotResponse(request).enqueue(new Callback<ChatResponse>() {
            @Override
            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
                removeTypingIndicator();
                if (response.isSuccessful() && response.body() != null) {
                    ChatResponse apiResponse = response.body();
                    addBotMessage(apiResponse.getReply());
                } else {
                    handleApiError();
                }
            }

            @Override
            public void onFailure(Call<ChatResponse> call, Throwable t) {
                removeTypingIndicator();
                handleNetworkError();
            }
        });
    }

    private void addTypingIndicator() {
        ChatMessage typingIndicator = new ChatMessage("typing", ChatMessage.TYPE_BOT);
        messageList.add(typingIndicator);
        chatAdapter.notifyItemInserted(messageList.size() - 1);
        scrollToBottom();
    }

    private void removeTypingIndicator() {
        if (!messageList.isEmpty() &&
                messageList.get(messageList.size() - 1).getContent().equals("typing")) {
            messageList.remove(messageList.size() - 1);
            chatAdapter.notifyItemRemoved(messageList.size());
        }
    }

    private void handleApiError() {
        addBotMessage("Sorry, I'm having trouble processing your request. Please try again.");
    }

    private void handleNetworkError() {
        addBotMessage("Connection error. Please check your internet connection.");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        markSessionAsCompleted();
    }

    private void markSessionAsCompleted() {
        if (currentSessionId != null) {
            Map<String, Object> updateData = new HashMap<>();
            updateData.put("endTime", Timestamp.now());
            updateData.put("status", "completed");
            updateData.put("messageCount", messageList.size());

            db.collection("chat_sessions")
                    .document(currentSessionId)
                    .set(updateData, SetOptions.merge())
                    .addOnSuccessListener(aVoid -> Log.d(TAG, "Session marked as completed"))
                    .addOnFailureListener(e -> Log.e(TAG, "Error updating session status", e));
        }
    }
}

//package com.developer.moodifyai.chatbot;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
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
//import com.developer.moodifyai.adapter.ChatAdapter;
//import com.developer.moodifyai.api.ApiClient;
//import com.developer.moodifyai.api.ChatbotApiService;
//import com.developer.moodifyai.model.ChatMessage;
//import com.developer.moodifyai.model.ChatRequest;
//import com.developer.moodifyai.model.ChatResponse;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class ChatbotActivity extends AppCompatActivity {
//
//    private RecyclerView chatRecyclerView;
//    private ChatAdapter chatAdapter;
//    private List<ChatMessage> messageList = new ArrayList<>();
//    private EditText editTextMessage;
//    private ImageButton sendMessageBtn;
//    private ChatbotApiService apiService;
//    private ImageView btn_back;
//    private FirebaseAuth firebaseAuth;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_chatbot);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.rl_header), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//        // Initialize Firebase Authentication
//        firebaseAuth = FirebaseAuth.getInstance();
//
//        // Initialize views
//        chatRecyclerView = findViewById(R.id.chatRecyclerView);
//        editTextMessage = findViewById(R.id.editTextMessage);
//        sendMessageBtn = findViewById(R.id.sendMessageBtn);
//        btn_back = findViewById(R.id.btn_back);
//
//        // Setup RecyclerView with improved scrolling
//        chatAdapter = new ChatAdapter(messageList);
//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
//        chatRecyclerView.setLayoutManager(layoutManager);
//        chatRecyclerView.setAdapter(chatAdapter);
//
////        chatAdapter = new ChatAdapter(messageList);
////        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
////        layoutManager.setStackFromEnd(true); // Important for chat UI
////        chatRecyclerView.setLayoutManager(layoutManager);
////        chatRecyclerView.setAdapter(chatAdapter);
//
//        // Initialize Retrofit API service
//        apiService = ApiClient.getClient().create(ChatbotApiService.class);
//
//        // Add welcome message when activity starts
//        addBotMessage("Hello there! 😊 I'm MoodifyAI, your mental wellness companion.\nHow can I assist you today?");
//
//        // Send button click listener
//        sendMessageBtn.setOnClickListener(v -> sendUserMessage());
//
//        // Back arrow functionality
////        btn_back.setOnClickListener(v -> finish());
//
//        btn_back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(ChatbotActivity.this, DashboardScreen.class);
//                intent.putExtra("navigate_to", "HomeFragment");
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//                finish();
//            }
//        });
//    }
//
//    private void sendUserMessage() {
//        String message = editTextMessage.getText().toString().trim();
//        if (!message.isEmpty()) {
//            addUserMessage(message);
//            editTextMessage.setText("");
//            sendMessageToApi(message);
//        }
//    }
//
//    private void addUserMessage(String message) {
//        ChatMessage userMessage = new ChatMessage(message, ChatMessage.TYPE_USER);
//        messageList.add(userMessage);
//        chatAdapter.notifyItemInserted(messageList.size() - 1);
//        scrollToBottom();
//    }
//
//    private void addBotMessage(String message) {
//        ChatMessage botMessage = new ChatMessage(message, ChatMessage.TYPE_BOT);
//        messageList.add(botMessage);
//        chatAdapter.notifyItemInserted(messageList.size() - 1);
//        scrollToBottom();
//    }
//
//    private void scrollToBottom() {
//        if (messageList.size() > 0) {
//            chatRecyclerView.post(() -> {
//                chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
//            });
//        }
//    }
//
////    private void scrollToBottom() {
////        chatRecyclerView.postDelayed(() -> {
////            if (messageList.size() > 0) {
////                chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
////            }
////        }, 100);
////    }
//
//    private void sendMessageToApi(String userMessage) {
//        // Get current user from Firebase
//        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
//
//        if (currentUser == null) {
//            // Handle case where user is not logged in
//            Toast.makeText(this, "Please login to use the chatbot", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String userId = currentUser.getUid(); // Get Firebase UID
//
//        // Show typing indicator
//        addTypingIndicator();
//
//        // Create API request with Firebase user ID
//        ChatRequest request = new ChatRequest(userMessage, userId);
//
//        // Make API call
//        apiService.getBotResponse(request).enqueue(new Callback<ChatResponse>() {
//            @Override
//            public void onResponse(Call<ChatResponse> call, Response<ChatResponse> response) {
//                // Remove typing indicator
//                removeTypingIndicator();
//
//                if (response.isSuccessful() && response.body() != null) {
//                    ChatResponse apiResponse = response.body();
//                    addBotMessage(apiResponse.getReply());
//                } else {
//                    handleApiError();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ChatResponse> call, Throwable t) {
//                // Remove typing indicator
//                removeTypingIndicator();
//                handleNetworkError();
//            }
//        });
//    }
//
//    private void addTypingIndicator() {
//        // Add typing indicator message
//        ChatMessage typingIndicator = new ChatMessage("typing", ChatMessage.TYPE_BOT);
//        messageList.add(typingIndicator);
//        chatAdapter.notifyItemInserted(messageList.size() - 1);
//        scrollToBottom();
//    }
//
//    private void removeTypingIndicator() {
//        // Remove typing indicator if it's the last message
//        if (!messageList.isEmpty() &&
//                messageList.get(messageList.size() - 1).getContent().equals("typing")) {
//
//            messageList.remove(messageList.size() - 1);
//            chatAdapter.notifyItemRemoved(messageList.size());
//        }
//    }
//
//    private void handleApiError() {
//        addBotMessage("Sorry, I'm having trouble processing your request. Please try again.");
//    }
//
//    private void handleNetworkError() {
//        addBotMessage("Connection error. Please check your internet connection.");
//    }
//}
