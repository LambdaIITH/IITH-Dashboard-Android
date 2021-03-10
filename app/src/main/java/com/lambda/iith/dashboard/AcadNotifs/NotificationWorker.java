package com.lambda.iith.dashboard.AcadNotifs;

import android.content.Context;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.lambda.iith.dashboard.R;

public class NotificationWorker extends Worker {

    public NotificationWorker(
            @NonNull Context context,
            @NonNull  WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String title = getInputData().getString("Title");
        String content = getInputData().getString("Content");
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "AcadEventAlerts")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                        R.mipmap.application_icon_foreground));

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        System.out.println("Notification processed");
        // notificationId is a unique int for each notification that you must define

        notificationManager.notify((int) (1572 + System.currentTimeMillis()), builder.build());

        return  Result.success();
    }
}


