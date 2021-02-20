package com.lambda.iith.dashboard.Timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.ArrayMap;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.TimeUnit;

import Model.Lecture;

public class NotificationInitiator extends Worker {

    public NotificationInitiator(@NonNull Context context, @NonNull WorkerParameters workerParams) {

        super(context, workerParams);

    }

    @NonNull
    @Override
    public Result doWork() {



        WorkManager.getInstance().cancelAllWorkByTag("LECTUREREMINDER");
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        if (sharedPreferences.getString("TimeWork", "").length() > 50000) {
            sharedPreferences.edit().putString("TimeWork", " ").commit();
        }
        sharedPreferences.edit().putString("TimeWork", Calendar.getInstance().getTime().toString() + "\n" + sharedPreferences.getString("TimeWork", "NULL")).apply();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("TimetableMapping", null);
        Type type = new TypeToken<ArrayList<ArrayMap<String, Lecture>>>() {
        }.getType();
        ArrayList<ArrayMap<String, Lecture>> CourseMap = gson.fromJson(json, type);
        String seg = sharedPreferences.getString("DefaultSegment", "12");
        ArrayMap<String, Lecture> CourseSegment = CourseMap.get(Integer.parseInt(seg.substring(0, 1)) / 2);
        int Interval = sharedPreferences.getInt("NotificationTime", 30);
        int Day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        String segments = "";

        switch (Day) {

            case Calendar.MONDAY:
                segments = "ABCDPQWX";
                break;




            case Calendar.TUESDAY:
                segments = "DEFGRSYZ";
                break;
            case Calendar.WEDNESDAY:
                segments = "BCAGF";
                break;
            case Calendar.THURSDAY:
                segments = "CABEQPWX";
                break;
            case Calendar.FRIDAY:
                segments = "EFDGSRYZ";
                break;
        }

        int[] hours = {9, 10, 11, 12, 14, 16,17 , 19};
        int[] mins = {0, 0, 0, 0, 30, 0, 30, 0};
        Boolean ring = sharedPreferences.getBoolean("LectureNotificationRing" , false);

        for (int i = 0; i < segments.length(); i++) {
            String slot = Character.toString(segments.charAt(i));
            Lecture lecture = CourseSegment.get(slot);
            if (!lecture.getCourseId().equals("")) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, mins[i]);
                calendar.set(Calendar.HOUR_OF_DAY, hours[i]);

                if (calendar.getTimeInMillis() - System.currentTimeMillis() - Interval * 60 * 1000 > 0) {


                    Data.Builder data = new Data.Builder().putString("LectureName", lecture.getCourse()).putString("LectureId", lecture.getCourseId()).putInt("Hours", hours[i]).putInt("Mins", mins[i]).putBoolean("Ring" , ring);


                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationWorker.class)
                            .setInitialDelay( calendar.getTimeInMillis() - System.currentTimeMillis() - Interval * 60 * 1000 , TimeUnit.MILLISECONDS)
                            .addTag("LECTUREREMINDER")
                            .setInputData(data.build())
                            .build();
                    WorkManager.getInstance()
                            .enqueue(oneTimeWorkRequest);
                }
            }
        }


        return Result.success();
    }

    @Override
    public void onStopped() {
        WorkManager.getInstance().cancelAllWork();
        super.onStopped();





    }
}
