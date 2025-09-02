package com.developer.moodifyai.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.developer.moodifyai.R;
import com.developer.moodifyai.adapter.MessageAdapter;
import com.developer.moodifyai.model.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ChatActivity extends AppCompatActivity {

    private TextView usersName;
    private ImageView backButton, voiceCallBtn, videoCallBtn;
    private EditText messageInput;
    private ImageButton sendButton;
    private RecyclerView messageRecycler;

    private String currentUserId, receiverId, chatId;
    private FirebaseFirestore firestore;
    private ListenerRegistration chatListener;

    private MessageAdapter messageAdapter;
    private List<Message> messageList;

    private String userNameStr, therapistNameStr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);

        usersName = findViewById(R.id.usersName);
        backButton = findViewById(R.id.backButton);
        voiceCallBtn = findViewById(R.id.voiceCallBtn);
        videoCallBtn = findViewById(R.id.videoCallBtn);
        messageInput = findViewById(R.id.editTextMessage);
        sendButton = findViewById(R.id.sendMessageBtn);
        messageRecycler = findViewById(R.id.chatRecyclerView);

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        firestore = FirebaseFirestore.getInstance();

        receiverId = getIntent().getStringExtra("receiverId");
        userNameStr = getIntent().getStringExtra("userName");
        therapistNameStr = getIntent().getStringExtra("therapistName");

        usersName.setText(therapistNameStr != null ? therapistNameStr : userNameStr != null ? userNameStr : "Chat");

        backButton.setOnClickListener(v -> finish());

        chatId = generateChatId(currentUserId, receiverId);

        voiceCallBtn.setOnClickListener(v -> startCall(true));
        videoCallBtn.setOnClickListener(v -> startCall(false));

        messageList = new ArrayList<>();
        messageAdapter = new MessageAdapter(messageList, currentUserId);
        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
        messageRecycler.setAdapter(messageAdapter);

        sendButton.setOnClickListener(v -> sendMessage());
        listenForMessages();
    }

    private void sendMessage() {
        String msg = messageInput.getText().toString().trim();
        if (msg.isEmpty()) return;

        Message message = new Message(
                UUID.randomUUID().toString(),
                currentUserId,
                receiverId,
                msg,
                System.currentTimeMillis(),
                Message.MessageStatus.SENT
        );

        firestore.collection("chats").document(chatId)
                .collection("messages").document(message.getMessageId())
                .set(message);

        messageInput.setText("");
    }

    private void listenForMessages() {
        chatListener = firestore.collection("chats").document(chatId)
                .collection("messages")
                .orderBy("timestamp", Query.Direction.ASCENDING)
                .addSnapshotListener((value, error) -> {
                    if (error != null || value == null) return;
                    for (DocumentChange change : value.getDocumentChanges()) {
                        if (change.getType() == DocumentChange.Type.ADDED) {
                            Message message = change.getDocument().toObject(Message.class);
                            messageList.add(message);
                            messageAdapter.notifyItemInserted(messageList.size() - 1);
                            messageRecycler.scrollToPosition(messageList.size() - 1);
                        }
                    }
                });
    }

    private void startCall(boolean isVoice) {
        String callType = isVoice ? "voice" : "video";
        String callId = UUID.randomUUID().toString();

        // Store call log in Firestore
        HashMap<String, Object> callData = new HashMap<>();
        callData.put("callId", callId);
        callData.put("callerId", currentUserId);
        callData.put("receiverId", receiverId);
        callData.put("callType", callType);
        callData.put("channelName", chatId);
        callData.put("status", "started");
        callData.put("timestampStart", System.currentTimeMillis());

        firestore.collection("calls").document(callId)
                .set(callData);

        Intent intent = new Intent(this, isVoice ? VoiceCallActivity.class : VideoCallActivity.class);
        intent.putExtra("channelName", chatId);
        intent.putExtra("callId", callId); // pass callId for update
        intent.putExtra("userName", userNameStr);
        intent.putExtra("therapistName", therapistNameStr);
        startActivity(intent);
    }

    private String generateChatId(String uid1, String uid2) {
        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatListener != null) chatListener.remove();
    }
}


//package com.developer.moodifyai.chat;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.adapter.MessageAdapter;
//import com.developer.moodifyai.chat.TherapistListActivity;
//import com.developer.moodifyai.chat.UserListActivity;
//import com.developer.moodifyai.chat.VideoCallActivity;
//import com.developer.moodifyai.chat.VoiceCallActivity;
//import com.developer.moodifyai.model.Message;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.firestore.DocumentChange;
//import com.google.firebase.firestore.FirebaseFirestore;
//import com.google.firebase.firestore.ListenerRegistration;
//import com.google.firebase.firestore.Query;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//public class ChatActivity extends AppCompatActivity {
//
//    private TextView usersName;
//    private ImageView backButton;
//    private EditText messageInput;
//    private ImageButton sendButton;
//    private RecyclerView messageRecycler;
//
//    private ImageView voiceCallButton, videoCallButton;
//
//    private String currentUserId;
//    private String receiverId;
//    private String chatId;
//
//    private FirebaseFirestore firestore;
//    private ListenerRegistration chatListener;
//
//    private MessageAdapter messageAdapter;
//    private List<Message> messageList;
//
//    private String userNameStr, therapistNameStr;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_chat);
//
//        // Views
//        usersName = findViewById(R.id.usersName);
//        backButton = findViewById(R.id.backButton);
//        messageInput = findViewById(R.id.editTextMessage);
//        sendButton = findViewById(R.id.sendMessageBtn);
//        messageRecycler = findViewById(R.id.chatRecyclerView);
//        voiceCallButton = findViewById(R.id.voiceCallBtn);
//        videoCallButton = findViewById(R.id.videoCallBtn);
//
//        // Auth and Firestore
//        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
//        firestore = FirebaseFirestore.getInstance();
//
//        // Intent data
//        receiverId = getIntent().getStringExtra("receiverId");
//        userNameStr = getIntent().getStringExtra("userName");
//        therapistNameStr = getIntent().getStringExtra("therapistName");
//
//        // Set title
//        if (therapistNameStr != null) {
//            usersName.setText(therapistNameStr);
//        } else if (userNameStr != null) {
//            usersName.setText(userNameStr);
//        } else {
//            usersName.setText("Chat");
//        }
//
//        // Back button
//        backButton.setOnClickListener(v -> {
//            if (userNameStr != null) {
//                startActivity(new Intent(ChatActivity.this, UserListActivity.class));
//            } else {
//                startActivity(new Intent(ChatActivity.this, TherapistListActivity.class));
//            }
//            finish();
//        });
//
//        // Call buttons
//        voiceCallButton.setOnClickListener(v -> startVoiceCall());
//        videoCallButton.setOnClickListener(v -> startVideoCall());
//
//        // Chat ID
//        chatId = generateChatId(currentUserId, receiverId);
//
//        // RecyclerView setup
//        messageList = new ArrayList<>();
//        messageAdapter = new MessageAdapter(messageList, currentUserId);
//        messageRecycler.setLayoutManager(new LinearLayoutManager(this));
//        messageRecycler.setAdapter(messageAdapter);
//
//        // Send button
//        sendButton.setOnClickListener(v -> sendMessage());
//
//        // Load messages
//        listenForMessages();
//    }
//
//    private void sendMessage() {
//        String msg = messageInput.getText().toString().trim();
//        if (msg.isEmpty()) return;
//
//        Message message = new Message(
//                UUID.randomUUID().toString(),
//                currentUserId,
//                receiverId,
//                msg,
//                System.currentTimeMillis(),
//                Message.MessageStatus.SENT
//        );
//
//        firestore.collection("chats")
//                .document(chatId)
//                .collection("messages")
//                .document(message.getMessageId())
//                .set(message);
//
//        messageInput.setText("");
//    }
//
//    private void listenForMessages() {
//        chatListener = firestore.collection("chats")
//                .document(chatId)
//                .collection("messages")
//                .orderBy("timestamp", Query.Direction.ASCENDING)
//                .addSnapshotListener((value, error) -> {
//                    if (error != null || value == null) return;
//
//                    for (DocumentChange change : value.getDocumentChanges()) {
//                        if (change.getType() == DocumentChange.Type.ADDED) {
//                            Message message = change.getDocument().toObject(Message.class);
//                            messageList.add(message);
//                            messageAdapter.notifyItemInserted(messageList.size() - 1);
//                            messageRecycler.scrollToPosition(messageList.size() - 1);
//                        }
//                    }
//                });
//    }
//
//    private void startVoiceCall() {
//        Intent intent = new Intent(ChatActivity.this, VoiceCallActivity.class);
//        intent.putExtra("receiverId", receiverId);
//        intent.putExtra("userName", userNameStr);
//        intent.putExtra("therapistName", therapistNameStr);
//        startActivity(intent);
//    }
//
//    private void startVideoCall() {
//        Intent intent = new Intent(ChatActivity.this, VideoCallActivity.class);
//        intent.putExtra("receiverId", receiverId);
//        intent.putExtra("userName", userNameStr);
//        intent.putExtra("therapistName", therapistNameStr);
//        startActivity(intent);
//    }
//
//    @NonNull
//    private String generateChatId(String uid1, String uid2) {
//        return uid1.compareTo(uid2) < 0 ? uid1 + "_" + uid2 : uid2 + "_" + uid1;
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (chatListener != null) chatListener.remove();
//    }
//}