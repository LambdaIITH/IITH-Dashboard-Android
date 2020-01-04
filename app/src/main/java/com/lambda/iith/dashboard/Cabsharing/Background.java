package com.lambda.iith.dashboard.Cabsharing;

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
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.lambda.iith.dashboard.MainActivity;
import com.lambda.iith.dashboard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.prefs.PreferenceChangeEvent;

public class Background extends Worker {

    private  JSONArray JA;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    public Background(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        JA = new JSONArray();

        try {
             JA = new JSONArray(getInputData().getString("OLD"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String url4 = "https://iith.dev/query";
        queue = Volley.newRequestQueue(getApplicationContext());
         sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray JA1 = null;
                        JSONArray JA2 = null;

                        final String startTime = sharedPreferences.getString("startTime", "    NA      NA  ");
                        final String endTime = sharedPreferences.getString("endTime", "    NA      NA  ");
                        final int CabID = sharedPreferences.getInt("Route", 100);

                        try {
                            JA1 = new JSONArray(response);
                            JA2 = new JSONArray();

                            for (int i = 0; i < JA1.length(); i++) {
                                JSONObject JO = (JSONObject) JA1.get(i);


                                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                                java.util.Date T1 = format1.parse(startTime.substring(0, 10) + ":" + startTime.substring(11, 16));

                                java.util.Date T2 = format1.parse(endTime.substring(0, 10) + ":" + endTime.substring(11, 16));
                                java.util.Date T3 = format1.parse(JO.getString("StartTime").substring(0, 10) + ":" + JO.getString("StartTime").substring(11, 16));
                                java.util.Date T4 = format1.parse(JO.getString("EndTime").substring(0, 10) + ":" + JO.getString("EndTime").substring(11, 16));

                                if ((JO.getInt("RouteID") == CabID) && !((JO.getString("Email")).equals(sharedPreferences.getString("UserEmail" , "email"))) && ((T3.compareTo(T1) >= 0 && T3.compareTo(T2) <= 0) || (T4.compareTo(T1) >= 0 && T4.compareTo(T2) <= 0))) {
                                    JA2.put(JO);


                                }

                            }


                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("CabShares", JA2.toString());

                            edit.commit();


                            for(int j=0 ; j<JA2.length() ; j++){
                                if(!check(JA ,JA2.getString(j))){
                                    sendNotification(JA2.getJSONObject(j).getString("Email") , j);
                                }
                            }




                        } catch (ParseException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(stringRequest);
        return null;
    }

    private  void  sendNotification(String str , int i){
        Intent intentToRepeat = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent =
                PendingIntent.getActivity(getApplicationContext(), 100, intentToRepeat, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext(), "CabSharingAlerts")
                        .setContentIntent(pendingIntent)
                        .setSmallIcon(R.drawable.ic_notification)
                        .setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.mipmap.application_icon_foreground));


        builder.setContentTitle("New Cab Share Found");

        builder.setContentText("Contact :" + str)


                .setAutoCancel(true);




        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());


        notificationManager.notify(100023 + i , builder.build());


    }

    private boolean check(JSONArray JA , String str) throws JSONException {
        for(int i=0; i<JA.length() ; i++){
            if(JA.getString(i) == str){
                return true;
            }
        }
        return false;

    }
}
