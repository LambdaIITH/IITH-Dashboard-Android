package com.lambda.iith.dashboard.AcadNotifs;

import android.content.Context;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class NotificationInitiator extends Worker {

    public NotificationInitiator(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public Result doWork() {

        WorkManager.getInstance().cancelAllWorkByTag("ACADEVENTS");
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

        String str = sharedPrefs.getString("AcadCalendar", " ");
        try {
            JSONArray AcadCal = new JSONArray(str);

//
            //do not push notifications if the user does not want them.
            if (sharedPrefs.getBoolean("EnableAcadNotification", true) == false)
                return Result.success();

            for (int i = 0; i < AcadCal.length(); i++) {
                JSONObject curr = AcadCal.getJSONObject(i);
                Calendar nextEventDate = Calendar.getInstance();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
                Date curr_date = dateFormat.parse(curr.getString("StartDate"));
                nextEventDate.setTime(curr_date);
                Calendar tom = Calendar.getInstance();

                tom.add(Calendar.DAY_OF_YEAR, 1);
                if (nextEventDate.get(Calendar.DAY_OF_YEAR) == tom.get(Calendar.DAY_OF_YEAR)) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MINUTE, 00);
                    cal.set(Calendar.HOUR_OF_DAY, 20);
                    long diff1 = cal.getTimeInMillis() - System.currentTimeMillis();
                    if (diff1 < 0)
                        diff1 = 0;
                    System.out.println("Registering work event");
                    sharedPrefs.edit().putString("TimeWork", curr.getString("Title") + " - " + Calendar.getInstance().getTime().toString() + "\n" + sharedPrefs.getString("TimeWork", "")).apply();
                    Data.Builder data = new Data.Builder().putString("Title", curr.getString("Title")).putString("Content", "Tomorrow");
                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                            .addTag("ACADEVENTS")
                            .setInputData(data.build())
                            .setInitialDelay(diff1, TimeUnit.MILLISECONDS)
                            .build();

                    if (diff1 > 0) {
                        WorkManager.getInstance()
                                .enqueue(oneTimeWorkRequest);
                    }

                } else if (nextEventDate.get(Calendar.DAY_OF_YEAR) == Calendar.getInstance().get(Calendar.DAY_OF_YEAR)) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.MINUTE, 00);
                    cal.set(Calendar.HOUR_OF_DAY, 9);
                    long diff = cal.getTimeInMillis() - System.currentTimeMillis();
                    if (diff < 0)
                        diff = 0;
                    System.out.println(diff);
                    Data.Builder data = new Data.Builder().putString("Title", curr.getString("Title")).putString("Content", "Today");
                    sharedPrefs.edit().putString("TimeWork", curr.getString("Title") + " - " + Calendar.getInstance().getTime().toString() + "\n" + sharedPrefs.getString("TimeWork", "")).apply();
                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                            .addTag("ACADEVENTS")
                            .setInputData(data.build())
                            .setInitialDelay(diff, TimeUnit.MILLISECONDS)
                            .build();

                    if (diff > 0) {
                        WorkManager.getInstance()
                                .enqueue(oneTimeWorkRequest);

                    }
                }

            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }
        return Result.success();
    }
}