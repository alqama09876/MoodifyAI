package com.developer.moodifyai.notify;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.developer.moodifyai.DashboardScreen;
import com.developer.moodifyai.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class NotifyManager extends BroadcastReceiver {

    private static final String CHANNEL_ID = "moodify_daily_reminder";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent notificationIntent = new Intent(context, DashboardScreen.class);
        notificationIntent.putExtra("navigate_to", "HomeFragment");
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Uri notificationTone = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.custom_tone);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.logo)
                .setContentTitle("Daily Mood Reminder")
                .setContentText("Track your mood for today!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setSound(notificationTone)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "Moodify Daily Reminder",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.setDescription("Daily reminder to track your mood");
            channel.setSound(notificationTone, null);
            notificationManager.createNotificationChannel(channel);
        }

        notificationManager.notify(1, builder.build());
    }

    public static void scheduleDailyReminder(Context context) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        String userId = auth.getCurrentUser() != null ? auth.getCurrentUser().getUid() : null;
        if (userId == null) return;

        firestore.collection("notification_preferences")
                .document(userId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if (document != null && document.exists()) {
                            long hour = document.getLong("hour");
                            long minute = document.getLong("minute");
                            scheduleAlarm(context, (int) hour, (int) minute);
                        }
                    }
                });
    }

    public static void scheduleAlarm(Context context, int hour, int minute) {
        Intent intent = new Intent(context, NotifyManager.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        if (alarmManager != null) {
            alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY,
                    alarmIntent
            );
        }
    }
}

//package com.developer.moodifyai.notify;
//
//import android.app.AlarmManager;
//import android.app.NotificationChannel;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.Build;
//
//import androidx.core.app.NotificationCompat;
//
//import com.developer.moodifyai.DashboardScreen;
//import com.developer.moodifyai.R;
//
//import java.util.Calendar;
//
//public class NotifyManager extends BroadcastReceiver {
//
//    private static final String CHANNEL_ID = "moodify_daily_reminder";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        Intent notificationIntent = new Intent(context, DashboardScreen.class);
//        notificationIntent.putExtra("navigate_to", "HomeFragment");
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        Uri notificationTone = Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.custom_tone);
//
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
//                .setSmallIcon(R.drawable.logo)
//                .setContentTitle("Daily Mood Reminder")
//                .setContentText("Track your mood for today!")
//                .setPriority(NotificationCompat.PRIORITY_HIGH)
//                .setSound(notificationTone) // Set custom tone
//                .setContentIntent(pendingIntent)
//                .setAutoCancel(true);
//
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel(
//                    CHANNEL_ID,
//                    "Moodify Daily Reminder",
//                    NotificationManager.IMPORTANCE_HIGH
//            );
//            channel.setDescription("Daily reminder to track your mood");
//            channel.setSound(notificationTone, null);
//            notificationManager.createNotificationChannel(channel);
//        }
//
//        notificationManager.notify(1, builder.build());
//    }
//
//    public static void scheduleDailyReminder(Context context) {
//        Intent intent = new Intent(context, NotifyManager.class);
//        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
//
//        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
//        Calendar calendar = Calendar.getInstance();
//        calendar.set(Calendar.HOUR_OF_DAY, 9);
//        calendar.set(Calendar.MINUTE, 0);
//        calendar.set(Calendar.SECOND, 0);
//
//        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
//            calendar.add(Calendar.DAY_OF_YEAR, 1);
//        }
//
//        if (alarmManager != null) {
//            alarmManager.setRepeating(
//                    AlarmManager.RTC_WAKEUP,
//                    calendar.getTimeInMillis(),
//                    AlarmManager.INTERVAL_DAY,
//                    alarmIntent
//            );
//        }
//    }
//}