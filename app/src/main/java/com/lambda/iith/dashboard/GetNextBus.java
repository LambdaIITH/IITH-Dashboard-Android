package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
        String string;
        String temp;
       t1 = textViews[0];
         t2 = textViews[1];
         t3 = textViews[2];
        t4 = textViews[3];

        try {
            JSONObject JO;
            if (k == 1) {
                JO = new JSONObject(sharedPref.getString("ToIITH", "{\"LAB\":\"02:00 ,02:30 ,03:15 ,04:00 ,04:45 ,05:30 ,06:15 ,07:00 ,07:30 ,08:00 ,08:30 ,13:15 ,14:30 ,15:00 ,19:45 ,20:30,\",\"LINGAMPALLY\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODF\":\"08:20 , 09:30 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,19:35 ,20:30 ,21:00 ,22:00 ,23:00,\",\"SANGAREDDY\":\"08:45 ,13:45 ,18:00 ,18:40 ,22:00,\",\"LINGAMPALLYW\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODFS\":\"08:00 ,09:15 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,18:30 ,19:45 ,20:30 ,21:30 ,23:00,\"}"));
            } else {
                JO = new JSONObject(sharedPref.getString("FromIITH", "{  \"LAB\": \"01:45 ,02:15 ,03:00 ,03:45 ,04:30 ,05:15 ,06:00 ,06:45 ,07:15 ,07:45 ,08:15 ,13:00 ,14:15 ,14:45 ,19:30 ,20:15,\",  \"LINGAMPALLY\": \"11:30 ,13:15 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODF\": \"09:00 ,10:30 ,12:10 ,13:10 ,14:45 ,17:45 ,18:00 ,19:00 ,20:15 ,21:00 ,22:00 ,23:00,\",  \"SANGAREDDY\": \"08:30 ,13:30 ,17:45 ,18:25 ,20:40,\",  \"LINGAMPALLYW\": \"10:30 ,12:30 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODFS\": \"08:40 ,10:15 ,12:10 ,13:15 ,14:45 ,16:10 ,17:45 ,19:10 ,20:30 ,21:10 ,22:10 ,23:30,\"}"));
            }
            Date date = Calendar.getInstance().getTime();

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
            String da = dateFormat.format(Calendar.getInstance().getTime());
            try {
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 7 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                    string = JO.getString("LINGAMPALLYW");
                } else {
                    string = JO.getString("LINGAMPALLY");
                }

                temp = "";

                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
                        //t1.setText(format1.format(date));
                        java.util.Date T1 = format1.parse(da + ":" + temp.trim());

                        if (T1.compareTo(date) > 0) {
                           a = temp;
                            break;
                        } else {
                            a = " N/A ";
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }
            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                    string = JO.getString("ODFS");
                } else {
                    string = JO.getString("ODF");
                }
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
                        java.util.Date T1 = format1.parse(da + ":" + temp.trim());

                        if (T1.compareTo(date) > 0) {
                           b = temp;
                            break;
                        } else {
                            b=(" N/A ");
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                string = JO.getString("SANGAREDDY");
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:ddHH:mm");

                        d=(temp);

                        java.util.Date T1 = format1.parse(da + temp.trim());

                        if (T1.compareTo(date) > 0) {

                            break;
                        } else {
                            d=(" N/A ");
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {


                string = JO.getString("LAB");
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:ddHH:mm");

                        c=(temp);

                        java.util.Date T1 = format1.parse(da + temp.trim());

                        if (T1.compareTo(date) > 0) {

                            break;
                        } else {
                            c=(" N/A ");
                        }
                        temp = "";
                        continue;
                    }


                    temp += string.substring(i, i + 1);


                }


            } catch (ParseException e) {
                e.printStackTrace();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return null;
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
