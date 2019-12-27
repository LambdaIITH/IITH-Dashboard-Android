package com.lambda.iith.dashboard.Timetable;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lambda.iith.dashboard.MainActivity;
import com.lambda.iith.dashboard.R;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class NotificationWorker extends Worker {

    public NotificationWorker(
            @NonNull Context context,
            @NonNull WorkerParameters params) {
        super(context, params);
    }

    @Override
    public Result doWork() {
        String CourseName = getInputData().getString("LectureName");
        String CourseID = getInputData().getString("LectureId");
        int Hours = getInputData().getInt("Hours", 0);
        int Mins = getInputData().getInt("Mins", 0);
        System.out.println("START");
        Intent intentToRepeat = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 100, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        //Build notification
        Notification repeatedNotification = buildLocalNotification(getApplicationContext(), pendingIntent, CourseName, CourseID, Hours, Mins).build();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


        notificationManager.notify(2821 + Calendar.getInstance().get(Calendar.HOUR_OF_DAY), repeatedNotification);


        return Result.success();
    }


    public NotificationCompat.Builder buildLocalNotification(Context context, PendingIntent pendingIntent, String CourseName, String CourseID, int Hours, int Mins) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, Hours);
        calendar.set(Calendar.MINUTE, Mins);

        DateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String desc = dateFormat.format(calendar.getTime());
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(context, "Timetable")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                                R.mipmap.application_icon_foreground));

        if (CourseName.equals("Name")) {
            builder.setContentTitle("Lecture : " + CourseID);
        } else {
            builder.setContentTitle("Lecture : " + CourseName);
        }
        builder.setContentText(desc)


                .setAutoCancel(true);

        return builder;
    }
}
