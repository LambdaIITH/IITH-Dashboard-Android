package com.lambda.iith.dashboard;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.lambda.iith.dashboard.Timetable.NotificationInitiator;

import java.util.concurrent.TimeUnit;

public class Launch extends Activity {

    public static int height, width;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;

        createNotificationChannel();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            //the user has not been logged in
            startActivity(new Intent(Launch.this, NoLogin.class));

        } else {
            if (sharedPreferences.getBoolean("EnableLectureNotification", false)) {
                refreshNotificationProcess();
            }
            startActivity(new Intent(Launch.this, MainActivity.class));
        }

        finish();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("Timetable", "Timetable", importance);
            channel.setDescription("Lecture Reminders");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            int importance1 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel2 = new NotificationChannel("Alerts", "Important Alerts", importance1);
            channel2.setDescription("Lecture Reminders");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager2 = getSystemService(NotificationManager.class);
            notificationManager2.createNotificationChannel(channel2);

            int importance2 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel3 = new NotificationChannel("CabSharingAlerts", "Cab Sharing", importance2);
            channel3.setDescription("Cab Sharing Alerts");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager3 = getSystemService(NotificationManager.class);
            notificationManager3.createNotificationChannel(channel3);

			//notification channel for academic calendar reminders.
            int importance3 = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel acadChannel = new NotificationChannel("AcadEventAlerts", "Academic Reminders", importance2);
            acadChannel.setDescription("Academic Calendar Events");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager4 = getSystemService(NotificationManager.class);
            notificationManager4.createNotificationChannel(acadChannel);
        }
    }

    private void refreshNotificationProcess() {
        WorkManager.getInstance().cancelAllWorkByTag("LECTUREREMINDER");
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationInitiator.class, 6, TimeUnit.HOURS).build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }





}
