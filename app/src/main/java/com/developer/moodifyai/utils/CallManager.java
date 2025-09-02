package com.developer.moodifyai.utils;

import android.content.Context;
import android.util.Log;
import android.view.SurfaceView;
import android.widget.FrameLayout;

import io.agora.rtc2.Constants;
import io.agora.rtc2.IRtcEngineEventHandler;
import io.agora.rtc2.RtcEngine;
import io.agora.rtc2.video.VideoCanvas;

public class CallManager {
    private static CallManager instance;
    private RtcEngine rtcEngine;
    private boolean isMicMuted = false;
    private boolean isCameraOn = true;
    private boolean isSpeakerOn = false;
    private IRtcEngineEventHandler eventHandler;
    public static final String AGORA_APP_ID = "4cdeabc86aac4be98ca1a28779f68e8e";

    private CallManager(Context context, IRtcEngineEventHandler handler) {
        this.eventHandler = handler;
        initRtcEngine(context);
    }

    public static synchronized CallManager getInstance(Context context, IRtcEngineEventHandler handler) {
        if (instance == null) {
            instance = new CallManager(context, handler);
        }
        return instance;
    }

    private void initRtcEngine(Context context) {
        try {
            rtcEngine = RtcEngine.create(context, AGORA_APP_ID, eventHandler);
            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
        } catch (Exception e) {
            Log.e("CallManager", "RTC Engine initialization failed", e);
        }
    }

    public void joinChannel(String token, String channelName, int uid) {
        if (rtcEngine != null && channelName != null && !channelName.isEmpty()) {
            rtcEngine.joinChannel(token, channelName, "Extra", uid);
        }
    }

    public void leaveChannel() {
        if (rtcEngine != null) rtcEngine.leaveChannel();
    }

    /**
     * Setup local video stream
     * @param container FrameLayout container to hold local video SurfaceView
     */
    public void setupLocalVideo(FrameLayout container) {
        if (rtcEngine == null || container == null) return;

        // Remove any existing views
        container.removeAllViews();

        // Create SurfaceView for local video rendering
        SurfaceView localView = RtcEngine.CreateRendererView(container.getContext());
        container.addView(localView);

        // Setup local video canvas with uid 0 for local user
        VideoCanvas localVideoCanvas = new VideoCanvas(localView, VideoCanvas.RENDER_MODE_HIDDEN, 0);
        rtcEngine.setupLocalVideo(localVideoCanvas);
        rtcEngine.startPreview();
    }

    /**
     * Setup remote video stream
     * @param uid Remote user's uid
     * @param container FrameLayout container to hold remote video SurfaceView
     */
    public void setupRemoteVideo(int uid, FrameLayout container) {
        if (rtcEngine == null || container == null) return;

        container.removeAllViews();

        // Create SurfaceView for remote video rendering
        SurfaceView remoteView = RtcEngine.CreateRendererView(container.getContext());
        container.addView(remoteView);

        // Setup remote video canvas with the remote uid
        VideoCanvas remoteVideoCanvas = new VideoCanvas(remoteView, VideoCanvas.RENDER_MODE_HIDDEN, uid);
        rtcEngine.setupRemoteVideo(remoteVideoCanvas);
    }

    public void removeRemoteVideo() {
        // Optional: you can clear remote video views if needed
    }

    /**
     * Mute/unmute microphone
     */
    public void toggleMic() {
        if (rtcEngine != null) {
            isMicMuted = !isMicMuted;
            rtcEngine.muteLocalAudioStream(isMicMuted);
        }
    }

    /**
     * Turn camera on/off
     */
    public void toggleCamera() {
        if (rtcEngine != null) {
            isCameraOn = !isCameraOn;
            rtcEngine.enableLocalVideo(isCameraOn);
        }
    }

    /**
     * Switch camera (front/rear)
     */
    public void switchCamera() {
        if (rtcEngine != null) {
            rtcEngine.switchCamera();
        }
    }

    /**
     * Enable/disable speakerphone
     */
    public void toggleSpeaker() {
        if (rtcEngine != null) {
            isSpeakerOn = !isSpeakerOn;
            rtcEngine.setEnableSpeakerphone(isSpeakerOn);
        }
    }

    /**
     * Enable or disable video mode
     * @param enable true to enable video, false to disable
     */
    public void enableVideo(boolean enable) {
        if (rtcEngine != null) {
            if (enable) rtcEngine.enableVideo();
            else rtcEngine.disableVideo();
        }
    }

    /**
     * Destroy the rtc engine instance
     */
    public void destroy() {
        if (rtcEngine != null) {
            RtcEngine.destroy();
            rtcEngine = null;
            instance = null;
        }
    }



    // Getter methods for mic, camera, and speaker states
    public boolean isMicMuted() {
        return isMicMuted;
    }

    public boolean isCameraOn() {
        return isCameraOn;
    }

    public boolean isSpeakerOn() {
        return isSpeakerOn;
    }
}


//package com.developer.moodifyai.utils;
//
//import android.content.Context;
//import android.util.Log;
//
//import io.agora.rtc2.Constants;
//import io.agora.rtc2.IRtcEngineEventHandler;
//import io.agora.rtc2.RtcEngine;
//import io.agora.rtc2.video.VideoCanvas;
//
//public class CallManager {
//    private static CallManager instance;
//    private RtcEngine rtcEngine;
//    private boolean isMicMuted = false;
//    private boolean isCameraOn = true;
//    private boolean isSpeakerOn = false;
//    private IRtcEngineEventHandler eventHandler;
//
//    private CallManager(Context context, IRtcEngineEventHandler handler) {
//        this.eventHandler = handler;
//        initRtcEngine(context);
//    }
//
//    public static synchronized CallManager getInstance(Context context, IRtcEngineEventHandler handler) {
//        if (instance == null) {
//            instance = new CallManager(context, handler);
//        }
//        return instance;
//    }
//
//    private void initRtcEngine(Context context) {
//        try {
//            rtcEngine = RtcEngine.create(context, "4cdeabc86aac4be98ca1a28779f68e8e", eventHandler);
//            rtcEngine.setChannelProfile(Constants.CHANNEL_PROFILE_COMMUNICATION);
//        } catch (Exception e) {
//            Log.e("CallManager", "RTC Engine initialization failed", e);
//        }
//    }
//
//    public void joinChannel(String token, String channelName, int uid) {
//        if (rtcEngine != null && channelName != null && !channelName.isEmpty()) {
//            rtcEngine.joinChannel(token, channelName, "Extra Data", uid);
//        } else {
//            Log.e("CallManager", "joinChannel failed: invalid channelName or rtcEngine is null");
//        }
//    }
//
//    public void leaveChannel() {
//        if (rtcEngine != null) {
//            rtcEngine.leaveChannel();
//        }
//    }
//
//    public void setupLocalVideo(VideoCanvas videoCanvas) {
//        if (rtcEngine != null) {
//            rtcEngine.setupLocalVideo(videoCanvas);
//        }
//    }
//
//    public void setupRemoteVideo(VideoCanvas videoCanvas) {
//        if (rtcEngine != null) {
//            rtcEngine.setupRemoteVideo(videoCanvas);
//        }
//    }
//
//    public void toggleMic() {
//        if (rtcEngine != null) {
//            isMicMuted = !isMicMuted;
//            rtcEngine.muteLocalAudioStream(isMicMuted);
//        }
//    }
//
//    public void setMicMuted(boolean muted) {
//        if (rtcEngine != null) {
//            isMicMuted = muted;
//            rtcEngine.muteLocalAudioStream(muted);
//        }
//    }
//
//    public void toggleCamera() {
//        if (rtcEngine != null) {
//            isCameraOn = !isCameraOn;
//            rtcEngine.enableLocalVideo(isCameraOn);
//        }
//    }
//
//    public void setCameraOn(boolean on) {
//        if (rtcEngine != null) {
//            isCameraOn = on;
//            rtcEngine.enableLocalVideo(on);
//        }
//    }
//
//    public void toggleSpeaker() {
//        if (rtcEngine != null) {
//            isSpeakerOn = !isSpeakerOn;
//            rtcEngine.setEnableSpeakerphone(isSpeakerOn);
//        }
//    }
//
//    public void setSpeakerOn(boolean on) {
//        if (rtcEngine != null) {
//            isSpeakerOn = on;
//            rtcEngine.setEnableSpeakerphone(on);
//        }
//    }
//
//    public void enableVideo(boolean enable) {
//        if (rtcEngine != null) {
//            if (enable) rtcEngine.enableVideo();
//            else rtcEngine.disableVideo();
//        }
//    }
//
//    public void destroy() {
//        if (rtcEngine != null) {
//            RtcEngine.destroy();
//            rtcEngine = null;
//            instance = null;
//            isMicMuted = false;
//            isCameraOn = true;
//            isSpeakerOn = false;
//            eventHandler = null;
//        }
//    }
//
//    public boolean isMicMuted() {
//        return isMicMuted;
//    }
//
//    public boolean isCameraOn() {
//        return isCameraOn;
//    }
//
//    public boolean isSpeakerOn() {
//        return isSpeakerOn;
//    }
//}