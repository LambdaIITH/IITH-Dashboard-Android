package com.lambda.iith.dashboard.BackgroundTasks;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.TextView;

import com.lambda.iith.dashboard.Init;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

public class GetNextBus extends AsyncTask <TextView , Void , Void> {
    private SharedPreferences sharedPref;
    private int k;
    private String a,b,c,d;
    private TextView t1,t2,t3,t4;


    public GetNextBus(SharedPreferences sharedPreferences , int k){
        this.sharedPref = sharedPreferences;
        this.k = k;

    }
    @Override
    protected Void doInBackground(TextView... textViews) {
        JSONArray BusTimes = new JSONArray();
        String temp;
        t1 = textViews[0];
        t2 = textViews[1];
        t3 = textViews[2];
        t4 = textViews[3];
        Init init = new Init();

        try {
            JSONObject JO;
            if (k == 1) {
                JO = new JSONObject(sharedPref.getString("ToIITH", Init.DEF1));
            } else {
                JO = new JSONObject(sharedPref.getString("FromIITH", Init.DEF2));
            }

            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 7 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                BusTimes = JO.getJSONArray("LINGAMPALLYW");
            } else {
                BusTimes = JO.getJSONArray("LINGAMPALLY");
            }


            a = TimeCalc(BusTimes);


            if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                BusTimes = JO.getJSONArray("ODF");
            } else {
                BusTimes = JO.getJSONArray("ODF");
            }

            b = TimeCalc(BusTimes);


            BusTimes = JO.getJSONArray("SANGAREDDY");
            d = TimeCalc(BusTimes);


            BusTimes = JO.getJSONArray("LAB");
            c = TimeCalc(BusTimes);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
    }

    private String TimeCalc(JSONArray BusTimes) {
        ArrayList<String> mArray = new ArrayList<>();
        try {

            Date date = Calendar.getInstance().getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
            String da = dateFormat.format(Calendar.getInstance().getTime());

            SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd:HH:mm");


            for (int i = 0; i < BusTimes.length(); i++) {

                if (BusTimes.getString(i).length() == 4) {
                    mArray.add("0" + BusTimes.getString(i));

                } else if (BusTimes.getString(i).length() > 4) {
                    mArray.add(BusTimes.getString(i));
                }
                Collections.sort(mArray);
            }

                for (int j = 0; j < mArray.size(); j++) {

                    java.util.Date T1 = format1.parse(da + ":" + mArray.get(j));
                    if (T1.compareTo(date) > 0) {

                        return mArray.get(j);
                    }

                }
                return mArray.get(0);





        } catch (ParseException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "/NA";
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        t1.setText(a);
        t2.setText(b);
        t3.setText(c);
        t4.setText(d);
    }
}
