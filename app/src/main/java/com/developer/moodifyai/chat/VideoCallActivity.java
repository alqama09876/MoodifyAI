package com.developer.moodifyai.chat;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.developer.moodifyai.R;
import com.developer.moodifyai.utils.CallManager;
import com.google.firebase.firestore.FirebaseFirestore;

import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.video.VideoCanvas;

public class VideoCallActivity extends AppCompatActivity {

    private static final String TAG = "VideoCallActivity";

    private CallManager callManager;
    private String channelName;
    private String token;
    private int uid;
    private String callId;

    private TextView callStatusText;
    private FrameLayout remoteVideoContainer, localVideoContainer;
    private ImageButton btnToggleMic, btnEndCall, btnToggleCamera, btnToggleSpeaker;

    private final IRtcEngineEventHandler rtcEventHandler = new IRtcEngineEventHandler() {
        @Override
        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
            Log.i(TAG, "Join channel success: " + channel + ", uid: " + uid);
            runOnUiThread(() -> callStatusText.setText("Call Connected"));
        }

        @Override
        public void onUserJoined(int uid, int elapsed) {
            Log.i(TAG, "User joined: " + uid);
            runOnUiThread(() -> {
                setupRemoteVideo(uid);
                callStatusText.setText("User Joined");
            });
        }

        @Override
        public void onUserOffline(int uid, int reason) {
            Log.i(TAG, "User offline: " + uid + ", reason: " + reason);
            runOnUiThread(() -> {
                removeRemoteVideo();
                callStatusText.setText("User Left");
                endCall();
            });
        }

//        @Override
//        public void onLeaveChannel() {
//            Log.i(TAG, "Left channel");
//            runOnUiThread(() -> callStatusText.setText("Left Channel"));
//        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_call);

        channelName = getIntent().getStringExtra("channelName");
        token = getIntent().getStringExtra("token");
        uid = getIntent().getIntExtra("uid", 0);
        callId = getIntent().getStringExtra("callId");

        initUI();

        callManager = CallManager.getInstance(getApplicationContext(), rtcEventHandler);
        callManager.enableVideo(true);
        callManager.setupLocalVideo(localVideoContainer);
        callManager.joinChannel(token, channelName, uid);

        // Log call start to Firestore
        if (callId != null && !callId.isEmpty()) {
            FirebaseFirestore.getInstance().collection("calls")
                    .document(callId)
                    .update("status", "started", "timestampStart", System.currentTimeMillis())
                    .addOnFailureListener(e -> {
                        // If document doesn't exist, create it
                        FirebaseFirestore.getInstance().collection("calls")
                                .document(callId)
                                .set(new java.util.HashMap<String, Object>() {{
                                    put("status", "started");
                                    put("timestampStart", System.currentTimeMillis());
                                    put("channelName", channelName);
                                }});
                    });
        }
    }

    private void initUI() {
        callStatusText = findViewById(R.id.call_status_text);
        remoteVideoContainer = findViewById(R.id.remote_video_view_container);
        localVideoContainer = findViewById(R.id.local_video_view_container);

        btnToggleMic = findViewById(R.id.btn_toggle_mic);
        btnToggleCamera = findViewById(R.id.btn_toggle_camera);
        btnEndCall = findViewById(R.id.btn_end_call);
//        btnToggleSpeaker = findViewById(R.id.btn_toggle_speaker);

        btnToggleMic.setOnClickListener(v -> {
            callManager.toggleMic();
            btnToggleMic.setImageResource(callManager.isMicMuted() ? R.drawable.mic_off : R.drawable.mic_on);
        });

        btnToggleCamera.setOnClickListener(v -> {
            callManager.toggleCamera();
            // Optional: Update icon for front/back camera
        });

//        btnToggleSpeaker.setOnClickListener(v -> {
//            callManager.toggleSpeaker();
//            btnToggleSpeaker.setImageResource(callManager.isSpeakerOn() ? R.drawable.speaker_on : R.drawable.speaker_off);
//        });

        btnEndCall.setOnClickListener(v -> endCall());
    }

    private void setupRemoteVideo(int remoteUid) {
        callManager.setupRemoteVideo(remoteUid, remoteVideoContainer);
    }

    private void removeRemoteVideo() {
        callManager.removeRemoteVideo();
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
        callManager.destroy();
    }
}


//package com.developer.moodifyai.chat;
//
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.FrameLayout;
//import android.widget.ImageButton;
//import android.widget.TextView;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.developer.moodifyai.R;
//import com.developer.moodifyai.utils.CallManager;
//import com.google.firebase.firestore.FirebaseFirestore;
//
//import io.agora.rtc2.IRtcEngineEventHandler;
//
//public class VideoCallActivity extends AppCompatActivity {
//
//    private static final String TAG = "VideoCallActivity";
//
//    private CallManager callManager;
//    private String channelName;
//    private String token;
//    private int uid;
//    private String callId;
//
//    private TextView callStatusText;
//    private FrameLayout remoteVideoContainer, localVideoContainer;
//    private ImageButton btnToggleMic, btnEndCall, btnToggleCamera, btnToggleSpeaker;
//
//    private final IRtcEngineEventHandler rtcEventHandler = new IRtcEngineEventHandler() {
//        @Override
//        public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
//            Log.i(TAG, "Join channel success: " + channel + ", uid: " + uid);
//            runOnUiThread(() -> callStatusText.setText("Call Connected"));
//        }
//
//        @Override
//        public void onUserJoined(int uid, int elapsed) {
//            Log.i(TAG, "User joined: " + uid);
//            runOnUiThread(() -> {
//                setupRemoteVideo(uid);
//                callStatusText.setText("User Joined");
//            });
//        }
//
//        @Override
//        public void onUserOffline(int uid, int reason) {
//            Log.i(TAG, "User offline: " + uid + ", reason: " + reason);
//            runOnUiThread(() -> {
//                removeRemoteVideo();
//                callStatusText.setText("User Left");
//                endCall();
//            });
//        }
//
//        @Override
//        public void onLeaveChannel() {
//            Log.i(TAG, "Left channel");
//            runOnUiThread(() -> callStatusText.setText("Left Channel"));
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_video_call);
//
//        channelName = getIntent().getStringExtra("channelName");
//        token = getIntent().getStringExtra("token");
//        uid = getIntent().getIntExtra("uid", 0);
//        callId = getIntent().getStringExtra("callId");
//
//        initUI();
//
//        callManager = CallManager.getInstance(getApplicationContext(), rtcEventHandler);
//        callManager.enableVideo(true);
//        callManager.setupLocalVideo(localVideoContainer);
//        callManager.joinChannel(token, channelName, uid);
//
//        // Log call start to Firestore
//        if (callId != null && !callId.isEmpty()) {
//            FirebaseFirestore.getInstance().collection("calls")
//                    .document(callId)
//                    .update("status", "started", "timestampStart", System.currentTimeMillis())
//                    .addOnFailureListener(e -> {
//                        // In case call doc doesn't exist, create it (backup)
//                        FirebaseFirestore.getInstance().collection("calls")
//                                .document(callId)
//                                .set(new java.util.HashMap<String, Object>() {{
//                                    put("status", "started");
//                                    put("timestampStart", System.currentTimeMillis());
//                                    put("channelName", channelName);
//                                }});
//                    });
//        }
//    }
//
//    private void initUI() {
//        callStatusText = findViewById(R.id.call_status_text);
//        remoteVideoContainer = findViewById(R.id.remote_video_view_container);
//        localVideoContainer = findViewById(R.id.local_video_view_container);
//
//        btnToggleMic = findViewById(R.id.btn_toggle_mic);
//        btnToggleCamera = findViewById(R.id.btn_toggle_camera);
//        btnEndCall = findViewById(R.id.btn_end_call);
//        btnToggleSpeaker = findViewById(R.id.btn_toggle_speaker);
//
//        btnToggleMic.setOnClickListener(v -> {
//            callManager.toggleMic();
//            btnToggleMic.setImageResource(callManager.isMicMuted() ? R.drawable.mic_off : R.drawable.mic_on);
//        });
//
//        btnToggleCamera.setOnClickListener(v -> {
//            callManager.switchCamera();
//            // You can toggle camera icon if you want here
//        });
//
//        btnToggleSpeaker.setOnClickListener(v -> {
//            callManager.toggleSpeaker();
//            btnToggleSpeaker.setImageResource(callManager.isSpeakerOn() ? R.drawable.speaker_on : R.drawable.speaker_off);
//        });
//
//        btnEndCall.setOnClickListener(v -> endCall());
//    }
//
//    private void setupRemoteVideo(int remoteUid) {
//        callManager.setupRemoteVideo(remoteUid, remoteVideoContainer);
//    }
//
//    private void removeRemoteVideo() {
//        if (remoteVideoContainer != null) {
//            remoteVideoContainer.removeAllViews();
//        }
//    }
//
//    private void endCall() {
//        callManager.leaveChannel();
//        callManager.destroy();
//
//        // Update Firestore call status as ended
//        if (callId != null && !callId.isEmpty()) {
//            FirebaseFirestore.getInstance().collection("calls")
//                    .document(callId)
//                    .update("status", "ended", "timestampEnd", System.currentTimeMillis());
//        }
//
//        finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        callManager.leaveChannel();
//        callManager.destroy();
//    }
//}


//package com.developer.moodifyai.chat;
//
//import android.Manifest;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.view.SurfaceView;
//import android.widget.FrameLayout;
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
//import io.agora.rtc2.video.VideoCanvas;
//
//public class VideoCallActivity extends AppCompatActivity {
//
//    private static final int PERMISSION_REQ_ID = 22;
//    private static final String[] REQUESTED_PERMISSIONS = {
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.CAMERA
//    };
//
//    private CallManager callManager;
//    private boolean isCaller;
//    private String channelName;
//
//    private FrameLayout localVideoContainer, remoteVideoContainer;
//    private ImageButton endCallBtn, toggleCameraBtn, toggleMicBtn;
//    private TextView callStatusText;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_video_call);
//
//        isCaller = getIntent().getBooleanExtra("isCaller", false);
//        channelName = getIntent().getStringExtra("channelName");
//
//        initViews();
//        checkPermissions();
//    }
//
//    private void initViews() {
//        localVideoContainer = findViewById(R.id.local_video_view_container);
//        remoteVideoContainer = findViewById(R.id.remote_video_view_container);
//        endCallBtn = findViewById(R.id.btn_end_call);
//        toggleCameraBtn = findViewById(R.id.btn_toggle_camera);
//        toggleMicBtn = findViewById(R.id.btn_toggle_mic);
//        callStatusText = findViewById(R.id.call_status_text);
//
//        endCallBtn.setOnClickListener(v -> endCall());
//        toggleCameraBtn.setOnClickListener(v -> toggleCamera());
//        toggleMicBtn.setOnClickListener(v -> toggleMic());
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
//        if (requestCode == PERMISSION_REQ_ID && grantResults.length == 2 &&
//                grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
//            initCallManager();
//        } else {
//            Toast.makeText(this, "Permissions needed to start video call", Toast.LENGTH_SHORT).show();
//            finish();
//        }
//    }
//
//    private void initCallManager() {
//        callManager = CallManager.getInstance(this, new IRtcEngineEventHandler() {
//            @Override
//            public void onJoinChannelSuccess(String channel, int uid, int elapsed) {
//                runOnUiThread(() -> {
//                    callStatusText.setText("Connected");
//                    setupLocalVideo(uid);
//                });
//            }
//
//            @Override
//            public void onUserJoined(int uid, int elapsed) {
//                runOnUiThread(() -> {
//                    callStatusText.setText("User joined");
//                    setupRemoteVideo(uid);
//                });
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
//        callManager.enableVideo(true);
//        callManager.joinChannel(null, channelName, 0);
//    }
//
//    private void setupLocalVideo(int uid) {
//        SurfaceView localView = new SurfaceView(this);
//        localView.setZOrderMediaOverlay(true);
//        localVideoContainer.addView(localView);
//        callManager.setupLocalVideo(new VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
//    }
//
//    private void setupRemoteVideo(int uid) {
//        SurfaceView remoteView = new SurfaceView(this);
//        remoteVideoContainer.addView(remoteView);
//        callManager.setupRemoteVideo(new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid));
//    }
//
//    private void toggleMic() {
//        if (callManager != null) {
//            callManager.toggleMic();
//            toggleMicBtn.setImageResource(callManager.isMicMuted() ? R.drawable.mic_off : R.drawable.mic_on);
//        }
//    }
//
//    private void toggleCamera() {
//        if (callManager != null) {
//            callManager.toggleCamera();
//            toggleCameraBtn.setImageResource(callManager.isCameraOn() ? R.drawable.camera_on : R.drawable.camera_off);
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