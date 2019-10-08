package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MessMenu extends Fragment {
    NestedScrollView hscrollViewMain;
    TextView htext1, htext2, htext3, htext4;
    private SharedPreferences sharedPreferences;
    private RequestQueue queue;
    private JSONObject j1, j2, j3, j4, j5, j6, j7, j11, j21, j31, j41, j51, j61, j71;

    private int day;

    private Spinner MessDay;
    private MultiStateToggleButton messToggle;

    private TextView breakfast, lunch, snacks, dinner;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.mess_menu, container, false);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        messToggle = rootview.findViewById(R.id.MessToggle1);
        messToggle.setValue(sharedPreferences.getInt("MESSDEF", 1));
        breakfast = rootview.findViewById(R.id.breakfast);
        lunch = rootview.findViewById(R.id.lunch);
        snacks = rootview.findViewById(R.id.snacks);
        dinner = rootview.findViewById(R.id.dinner);


        queue = Volley.newRequestQueue(getContext());


        MessDay = rootview.findViewById(R.id.MessDaySelect);


        if (messToggle.getValue() == 0) {
            parse("LDH");
        } else {
            parse("UDH");
        }

        messToggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                if (value == 0) {

                    parse("LDH");


                    System.out.println("HelloWTF");

                } else {
                    parse("UDH");
                    System.out.println("HelloWTF1");
                }
                int position = MessDay.getSelectedItemPosition();
                try {
                    if (position == 0) {

                        putData(j2, j21);
                    }
                    if (position == 1) {

                        putData(j3, j31);
                    }
                    if (position == 2) {

                        putData(j4, j41);
                    }
                    if (position == 3) {

                        putData(j5, j51);
                    }
                    if (position == 4) {

                        putData(j6, j61);
                    }
                    if (position == 5) {

                        putData(j7, j71);
                    }
                    if (position == 6) {

                        putData(j1, j11);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });


        hscrollViewMain = rootview.findViewById(R.id.scrollViewMain);

        htext1 = rootview.findViewById(R.id.gg1);
        htext2 = rootview.findViewById(R.id.lunch);
        htext3 = rootview.findViewById(R.id.evening);
        htext4 = rootview.findViewById(R.id.textiewxyz);


        MessDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == 0) {

                        putData(j2, j21);
                    }
                    if (position == 1) {

                        putData(j3, j31);
                    }
                    if (position == 2) {

                        putData(j4, j41);
                    }
                    if (position == 3) {

                        putData(j5, j51);
                    }
                    if (position == 4) {

                        putData(j6, j61);
                    }
                    if (position == 5) {

                        putData(j7, j71);
                    }
                    if (position == 6) {

                        putData(j1, j11);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);


        hscrollViewMain.post(new Runnable() {
            @Override
            public void run() {
                try {
                    //Defining time ranges for showing particular card view at the top

                    String string1 = "10:30:00";
                    Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(time1);

                    String string2 = "14:30:00";
                    Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);

                    String string3 = "18:30:00";
                    Date time3 = new SimpleDateFormat("HH:mm:ss").parse(string3);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(time3);

                    String string4 = "22:30:00";
                    Date time4 = new SimpleDateFormat("HH:mm:ss").parse(string4);
                    Calendar calendar4 = Calendar.getInstance();
                    calendar4.setTime(time4);


                    Calendar calendarn = Calendar.getInstance();
                    Date d = calendarn.getTime();

                    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                    String formattedDate = dateFormat.format(d);
                    Date timen = new SimpleDateFormat("HH:mm:ss").parse(formattedDate);

                    if (timen.after(calendar1.getTime()) && timen.before(calendar2.getTime())) {
                        //checkes whether the current time is between 10:30:00 and 14:30:00.
                        hscrollViewMain.smoothScrollTo(0, htext2.getTop());
                    }
                    if (timen.after(calendar2.getTime()) && timen.before(calendar3.getTime())) {
                        //checkes whether the current time is between 10:30:00 and 14:30:00.
                        hscrollViewMain.smoothScrollTo(0, htext3.getTop());
                    }
                    if (timen.after(calendar3.getTime()) && timen.before(calendar4.getTime())) {
                        //checkes whether the current time is between 10:30:00 and 14:30:00.
                        hscrollViewMain.smoothScrollTo(0, htext4.getTop());
                    }
                    if (timen.after(calendar4.getTime()) && timen.before(calendar1.getTime())) {
                        //checkes whether the current time is between 10:30:00 and 14:30:00.
                        hscrollViewMain.smoothScrollTo(0, htext1.getTop());
                    }


                } catch (ParseException e) {
                    e.printStackTrace();
                }

            }

        });


        int day = calendar.get(Calendar.DAY_OF_WEEK);
        MessDay.setSelection((day + 5) % 7);
        System.out.println("H:" + (day - 2) % 7);


        return rootview;
    }


    private void putData(JSONObject j2, JSONObject j1) throws JSONException {
        JSONArray JA1 = j2.getJSONArray("Breakfast");
        JSONArray JA2 = j2.getJSONArray("Lunch");
        JSONArray JA3 = j2.getJSONArray("Snacks");
        JSONArray JA4 = j2.getJSONArray("Dinner");
        JSONArray JA5 = j1.getJSONArray("Breakfast");
        JSONArray JA6 = j1.getJSONArray("Lunch");
        JSONArray JA7 = j1.getJSONArray("Snacks");
        JSONArray JA8 = j1.getJSONArray("Dinner");
        breakfast.setText(parseMeal(JA1, JA5));
        lunch.setText(parseMeal(JA2, JA6));
        snacks.setText(parseMeal(JA3, JA7));
        dinner.setText(parseMeal(JA4, JA8));


    }

    private String parseMeal(JSONArray JA1, JSONArray JA2) {
        String string = "";
        try {

            for (int i = 0; i < JA1.length(); i++) {
                string += (i + 1) + ".\u00A0" + JA1.getString(i).replace(" ", "\u00A0") + "   ";

            }

            string += " \n\n";
            string += "Extras:";
            string += " \n";

            for (int i = 0; i < JA2.length(); i++) {
                string += (i + 1) + ".\u00A0" + JA2.getString(i).replace(" ", "\u00A0") + "   ";

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return string;
    }


    private void parse(String string) {
        try {
            JSONObject JA;
            if(sharedPreferences.getString("MESSJSON", "NULL").equals("NULL")){
                Init init = new Init();
                JA = new JSONObject(Init.DEF);

            }
            else{
            JA = new JSONObject(sharedPreferences.getString("MESSJSON", "NULL"));}
            System.out.println(JA);
            JSONObject JA1 = JA.getJSONObject(string);
            j1 = JA1.getJSONObject("Sunday");
            System.out.println("HHHH" + j1);
            j2 = JA1.getJSONObject("Monday");
            j3 = JA1.getJSONObject("Tuesday");
            j4 = JA1.getJSONObject("Wednesday");
            j5 = JA1.getJSONObject("Thursday");
            j6 = JA1.getJSONObject("Friday");
            j7 = JA1.getJSONObject("Saturday");

            JSONObject JA2 = JA.getJSONObject(string + " Additional");
            j11 = JA2.getJSONObject("Sunday");

            System.out.println("HHH!" + j11);
            j21 = JA2.getJSONObject("Monday");
            j31 = JA2.getJSONObject("Tuesday");
            j41 = JA2.getJSONObject("Wednesday");
            j51 = JA2.getJSONObject("Thursday");
            j61 = JA2.getJSONObject("Friday");
            j71 = JA2.getJSONObject("Saturday");


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}


