package com.developer.moodifyai.chat;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.moodifyai.R;
import com.developer.moodifyai.utils.CallManager;
import com.google.firebase.firestore.FirebaseFirestore;

import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;

public class VoiceCallActivity extends AppCompatActivity {

    private static final String TAG = "VoiceCallActivity";

    private CallManager callManager;
    private String channelName;
    private String token;
    private int uid;
    private String callId;

    private TextView callStatusText;
    private ImageButton btnToggleMic, btnEndCall, btnToggleSpeaker;

    private final IRtcEngineEventHandler rtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            runOnUiThread(() -> callStatusText.setText("Call Connected"));
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            runOnUiThread(() -> {
                callStatusText.setText("User Left");
                endCall();
            });
        }

        @Override
        public void onLeaveChannel(RtcStats stats) {
            Log.i(TAG, "Left channel");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_call);

        channelName = getIntent().getStringExtra("channelName");
        token = getIntent().getStringExtra("token");
        uid = getIntent().getIntExtra("uid", 0);
        callId = getIntent().getStringExtra("callId");

        initUI();
        callManager = CallManager.getInstance(getApplicationContext(), rtcEventHandler);
        callManager.enableVideo(false); // Only voice call
        callManager.joinChannel(token, channelName, uid);
    }

    private void initUI() {
        callStatusText = findViewById(R.id.call_status_text);
        btnToggleMic = findViewById(R.id.btn_toggle_mic);
        btnEndCall = findViewById(R.id.btn_end_call);
        btnToggleSpeaker = findViewById(R.id.btn_toggle_speaker);

        btnToggleMic.setOnClickListener(v -> {
            callManager.toggleMic();
            btnToggleMic.setImageResource(callManager.isMicMuted() ? R.drawable.mic_off : R.drawable.mic_on);
        });

        btnToggleSpeaker.setOnClickListener(v -> {
            callManager.toggleSpeaker();
            btnToggleSpeaker.setImageResource(callManager.isSpeakerOn() ? R.drawable.speaker_on : R.drawable.speaker_off);
        });

        btnEndCall.setOnClickListener(v -> endCall());
    }

    private void endCall() {
        callManager.leaveChannel();
        callManager.destroy();

        if (callId != null && !callId.isEmpty()) {
            FirebaseFirestore.getInstance().collection("calls")
                    .document(callId)
                    .update("status", "ended", "timestampEnd", System.currentTimeMillis());
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        callManager.leaveChannel();
    }
}


//package com.developer.moodifyai.chat;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.activity.EdgeToEdge;
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.utils.CallManager;
//
//import io.agora.rtc2.IRtcEngineEventHandler;
//
//public class VoiceCallActivity extends AppCompatActivity {
//
//    private static final int PERMISSION_REQ_ID = 22;
//    private static final String[] REQUESTED_PERMISSIONS = {
//            Manifest.permission.RECORD_AUDIO
//    };
//
//    private CallManager callManager;
//    private boolean isCaller;
//    private String channelName;
//
//    private ImageButton endCallBtn, toggleMicBtn, toggleSpeakerBtn;
//    private TextView callStatusText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_voice_call);
//
//        isCaller = getIntent().getBooleanExtra("isCaller", false);
//        channelName = getIntent().getStringExtra("channelName");
//
//        initViews();
//        checkPermissions();
//    }
//
//    private void initViews() {
//        endCallBtn = findViewById(R.id.btn_end_call);
//        toggleMicBtn = findViewById(R.id.btn_toggle_mic);
//        toggleSpeakerBtn = findViewById(R.id.btn_toggle_speaker);
//        callStatusText = findViewById(R.id.call_status_text);
//
//        endCallBtn.setOnClickListener(v -> endCall());
//        toggleMicBtn.setOnClickListener(v -> toggleMic());
//        toggleSpeakerBtn.setOnClickListener(v -> toggleSpeaker());
//
//        callStatusText.setText(isCaller ? "Calling..." : "Incoming call...");
//    }
//
//    private void checkPermissions() {
//        if (checkSelfPermissions()) {
//            initCallManager();
//        } else {
//            ActivityCompat.requestPermissions(this, REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
//        }
//    }
//
//    private boolean checkSelfPermissions() {
//        for (String permission : REQUESTED_PERMISSIONS) {
//            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == PERMISSION_REQ_ID && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            initCallManager();
//        } else {
//            Toast.makeText(this, "Permissions needed to start call", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
//
//    private void initCallManager() {
//        callManager = CallManager.getInstance(this, new IRtcEngineEventHandler() {
//            @Override
//            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
//                runOnUiThread(() -> callStatusText.setText("Connected"));
//            }
//
//            @Override
//            public void onUserJoined(int uid, int elapsed) {
//                runOnUiThread(() -> callStatusText.setText("User joined"));
//            }
//
//            @Override
//            public void onUserOffline(int uid, int reason) {
//                runOnUiThread(() -> {
//                    callStatusText.setText("Call ended");
//                    endCall();
//                });
//            }
//        });
//
//        callManager.enableVideo(false); // Voice only
//        callManager.joinChannel(null, channelName, 0);
//    }
//
//    private void toggleMic() {
//        if (callManager != null) {
//            callManager.toggleMic();
//            toggleMicBtn.setImageResource(callManager.isMicMuted() ? R.drawable.mic_off : R.drawable.mic_on);
//        }
//    }
//
//    private void toggleSpeaker() {
//        if (callManager != null) {
//            callManager.toggleSpeaker();
//            toggleSpeakerBtn.setImageResource(callManager.isSpeakerOn() ? R.drawable.speaker_on : R.drawable.speaker_off);
//        }
//    }
//
//    private void endCall() {
//        if (callManager != null) {
//            callManager.leaveChannel();
//            callManager.destroy();
//        }
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (callManager != null) {
//            callManager.destroy();
//        }
//    }
//}