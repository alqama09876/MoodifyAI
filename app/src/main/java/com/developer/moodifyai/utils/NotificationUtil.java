package com.developer.moodifyai.utils;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.developer.moodifyai.R;
import com.developer.moodifyai.chat.ChatActivity;
import com.developer.moodifyai.chat.VoiceCallActivity;
import com.developer.moodifyai.chat.VideoCallActivity;

public class NotificationUtil {

    private static final String CHANNEL_ID_MESSAGES = "channel_messages";
    private static final String CHANNEL_ID_CALLS = "channel_calls";

    /**
     * Creates notification channels (required for Android O+).
     */
    public static void createNotificationChannels(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = context.getSystemService(NotificationManager.class);

            NotificationChannel messagesChannel = new NotificationChannel(
                    CHANNEL_ID_MESSAGES,
                    "Messages",
                    NotificationManager.IMPORTANCE_HIGH
            );
            messagesChannel.setDescription("Notifications for new messages");
            messagesChannel.enableLights(true);
            messagesChannel.setLightColor(Color.BLUE);
            messagesChannel.enableVibration(true);

            NotificationChannel callsChannel = new NotificationChannel(
                    CHANNEL_ID_CALLS,
                    "Calls",
                    NotificationManager.IMPORTANCE_HIGH
            );
            callsChannel.setDescription("Notifications for incoming calls");
            callsChannel.enableLights(true);
            callsChannel.setLightColor(Color.RED);
            callsChannel.enableVibration(true);

            nm.createNotificationChannel(messagesChannel);
            nm.createNotificationChannel(callsChannel);
        }
    }

    /**
     * Displays a message notification (chat).
     */
    public static void showMessageNotification(Context context, String title, String message, String chatId, String receiverId) {
        // Check for POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // Permission not granted, do not show notification
        }

        Intent intent = new Intent(context, ChatActivity.class);
        intent.putExtra("receiverId", receiverId);
        intent.putExtra("chatId", chatId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_MESSAGES)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);

        NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
    }

    /**
     * Displays a notification for incoming voice/video calls.
     */
    public static void showCallNotification(Context context, String callerId, String channelName, boolean isVideoCall) {
        // Check for POST_NOTIFICATIONS permission on Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                context.checkSelfPermission(android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // Permission not granted, do not show notification
        }

        Intent intent = isVideoCall
                ? new Intent(context, VideoCallActivity.class)
                : new Intent(context, VoiceCallActivity.class);

        intent.putExtra("channelName", channelName);
        intent.putExtra("isCaller", false);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID_CALLS)
                .setSmallIcon(R.drawable.ic_call_end)
                .setContentTitle("Incoming " + (isVideoCall ? "Video" : "Voice") + " Call")
                .setContentText("Call from: " + callerId)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setOngoing(true)
                .setOnlyAlertOnce(true);

        NotificationManagerCompat.from(context).notify(12345, builder.build()); // Use fixed ID for call
    }

    /**
     * Clears the incoming call notification.
     */
    public static void clearCallNotification(Context context) {
        NotificationManagerCompat.from(context).cancel(12345);
    }
}


//package com.developer.moodifyai.utils;
//
//import android.app.Notification;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.content.Context;
//import android.media.AudioAttributes;
//import android.net.Uri;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//
//import com.developer.moodifyai.R;
//
//public class NotificationUtil {
//
//    private static final String CHANNEL_ID = "moodifyai_channel";
//    private static final String CHANNEL_NAME = "MoodifyAI Notifications";
//
//    // Call this once in Application or Activity before sending notifications
//    public static void createNotificationChannel(Context context, Uri soundUri) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    CHANNEL_NAME,
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            channel.setDescription("MoodifyAI app notifications");
//
//            if (soundUri != null) {
//                AudioAttributes audioAttributes = new AudioAttributes.Builder()
//                        .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
//                        .build();
//                channel.setSound(soundUri, audioAttributes);
//            } else {
//                channel.setSound(null, null);  // Default sound or silent
//            }
//
//            channel.enableLights(true);
//            channel.enableVibration(true);
//
//            NotificationManager manager = context.getSystemService(NotificationManager.class);
//            if (manager != null) {
//                manager.createNotificationChannel(channel);
//            }
//        }
//    }
//
//    public static void showNotification(Context context, String title, String message, int iconResId, Uri soundUri, int notificationId) {
//        createNotificationChannel(context, soundUri);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(iconResId)
//                .setContentTitle(title)
//                .setContentText(message)
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setAutoCancel(true);
//
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
//            // For older devices, set sound directly on notification
//            if (soundUri != null) {
//                builder.setSound(soundUri);
//            }
//        }
//
//        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        if (manager != null) {
//            manager.notify(notificationId, builder.build());
//        }
//    }
//
//    // Helper to get sound URI from raw resource id
//    public static Uri getSoundUri(Context context, int rawResId) {
//        return Uri.parse("android.resource://" + context.getPackageName() + "/" + rawResId);
//    }
//}