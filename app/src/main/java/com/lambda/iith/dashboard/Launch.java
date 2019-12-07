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
            startActivity(new Intent(Launch.this, NoLogin.class));


        } else {
            if (sharedPreferences.getBoolean("EnableLectureNotification", true)) {
                refreshNotificationProcess();
            }
            startActivity(new Intent(Launch.this, MainActivity.class));
        }


        finish();

    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("Timetable", "Timetable", importance);
            channel.setDescription("Lecture Reminders");
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void refreshNotificationProcess() {
        WorkManager.getInstance().cancelAllWork();
        //OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationInitiator.class).setInitialDelay(1000 , TimeUnit.MILLISECONDS).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationInitiator.class, 6, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance()
                .enqueue(periodicWorkRequest);
    }





}
