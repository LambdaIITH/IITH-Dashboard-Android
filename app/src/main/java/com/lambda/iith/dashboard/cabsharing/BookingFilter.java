package com.lambda.iith.dashboard.cabsharing;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.ArrayMap;

import com.lambda.iith.dashboard.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Model.Filter;
import Model.Lecture;

public class BookingFilter extends AsyncTask<Filter, Void, Void> {


    @Override
    protected Void doInBackground(Filter... filters) {
        JSONArray JA = null;
        JSONArray JA2 = null;
        String response = filters[0].response;
        SharedPreferences sharedPreferences = filters[0].sharedPreferences;
        final String startTime = sharedPreferences.getString("startTime", "    NA      NA  ");
        final String endTime = sharedPreferences.getString("endTime", "    NA      NA  ");
        final int CabID = sharedPreferences.getInt("Route", 100);

        try {
            JA = new JSONArray(response);
            JA2 = new JSONArray();

            for (int i = 0; i < JA.length(); i++) {
                JSONObject JO = (JSONObject) JA.get(i);
                System.out.println("GGGG1233");

                SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd:HH:mm");
                java.util.Date T1 = format1.parse(startTime.substring(0, 10) + ":" + startTime.substring(11, 16));
                System.out.println(startTime.substring(0, 10) + ":" + startTime.substring(11, 16));
                java.util.Date T2 = format1.parse(endTime.substring(0, 10) + ":" + endTime.substring(11, 16));
                java.util.Date T3 = format1.parse(JO.getString("StartTime").substring(0, 10) + ":" + JO.getString("StartTime").substring(11, 16));
                java.util.Date T4 = format1.parse(JO.getString("EndTime").substring(0, 10) + ":" + JO.getString("EndTime").substring(11, 16));
                System.out.println(T1);
                System.out.println(T2);
                System.out.println(T3);
                System.out.println(T4);
                if ((JO.getInt("RouteID") == CabID) && !((JO.getString("Email")).equals(MainActivity.email)) && ((T3.compareTo(T1) >= 0 && T3.compareTo(T2) <= 0) || (T4.compareTo(T1) >= 0 && T4.compareTo(T2) <= 0))) {
                    System.out.println("GGGG1233" + JO);
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
        return null;
    }
}
