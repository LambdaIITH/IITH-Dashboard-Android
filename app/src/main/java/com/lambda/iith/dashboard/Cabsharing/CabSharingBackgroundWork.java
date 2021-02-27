package com.lambda.iith.dashboard.Cabsharing;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.preference.PreferenceManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lambda.iith.dashboard.MainActivity;
import com.lambda.iith.dashboard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import Model.Filter;

public class CabSharingBackgroundWork extends Worker {

    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private Context context1;
    private  boolean Switch;
    private JSONArray JA;
    public CabSharingBackgroundWork(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);


    }


    @NonNull
    @Override
    public Result doWork() {
        queue = Volley.newRequestQueue(getApplicationContext());
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        context1 = getApplicationContext();
        checkForUpdates();
        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {


            @Override
            public void onRequestFinished(Request<Object> request) {

                final String emptyJA = new JSONArray().toString();
                if (Switch) {

                    JA = new JSONArray();
                    try {
                        JA = new JSONArray(sharedPreferences.getString("CabShares", emptyJA));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Data.Builder builder = new Data.Builder();
                    builder.putString("OLD" , JA.toString());
                    OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(Background.class).setInputData(builder.build()).addTag("CAB2").setInitialDelay(1 , TimeUnit.SECONDS).build();
                    WorkManager.getInstance().enqueue(oneTimeWorkRequest);
                }
            }
        });






        return Result.success();
    }



    public void updateShares(){
        final String url4 = "https://iith.dev/query";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray JA = null;
                        JSONArray JA2 = null;

                        final String startTime = sharedPreferences.getString("startTime", "    NA      NA  ");
                        final String endTime = sharedPreferences.getString("endTime", "    NA      NA  ");
                        final int CabID = sharedPreferences.getInt("Route", 100);

                        try {
                            JA = new JSONArray(response);
                            JA2 = new JSONArray();

                            for (int i = 0; i < JA.length(); i++) {
                                JSONObject JO = (JSONObject) JA.get(i);


                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                                java.util.Date T1 = format1.parse(startTime.substring(0, 10) + ":" + startTime.substring(11, 16));

                                java.util.Date T2 = format1.parse(endTime.substring(0, 10) + ":" + endTime.substring(11, 16));
                                java.util.Date T3 = format1.parse(JO.getString("StartTime").substring(0, 10) + ":" + JO.getString("StartTime").substring(11, 16));
                                java.util.Date T4 = format1.parse(JO.getString("EndTime").substring(0, 10) + ":" + JO.getString("EndTime").substring(11, 16));

                                if ((JO.getInt("RouteID") == CabID) && !((JO.getString("Email")).equals(MainActivity.email)) && ((T3.compareTo(T1) >= 0 && T3.compareTo(T2) <= 0) || (T4.compareTo(T1) >= 0 && T4.compareTo(T2) <= 0))) {
                                    JA2.put(JO);


                                }

                            }


                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("CabShares", JA2.toString());

                            edit.commit();


                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(stringRequest);
    }

    //Check if shares available
    private void checkForUpdates(){



        String URL9 = "https://iith.dev/update";
        JSONObject jsonBody = new JSONObject();


        final String start = sharedPreferences.getString("startTime", "    NA      NA  ");
        final String end = sharedPreferences.getString("endTime", "    NA      NA  ");
        final int route = sharedPreferences.getInt("Route", 100);


        try {
            jsonBody.put("QueryTimeStart", start);
            jsonBody.put("QueryTimeEnd", end);
            jsonBody.put("RouteID", route);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = jsonBody.toString();


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL9, jsonBody, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {


                try {
                    Switch = response.getBoolean("IsUpdateReqd");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });



        queue.add(stringRequest);
    }


}
