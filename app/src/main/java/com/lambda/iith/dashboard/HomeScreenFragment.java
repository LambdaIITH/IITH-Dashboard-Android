package com.lambda.iith.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import Adapters.HomeTimeTableAdapter;
import Adapters.RecyclerViewAdapter_CSHOME;
import Model.Lecture;

public class HomeScreenFragment extends Fragment {

    //Fragment to be displayed on Home screen which will have 3 cards as layout.
    private CardView bus, mess, timetable, cab;
    private Space space1;
    private RecyclerView CabSharing;
    private TextView mess1;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private View view;
    private String email;
    private HashMap<String, HashMap<String, Lecture>> courseMap = new HashMap<>();
    private RecyclerView myRV;
    public static SharedPreferences sharedPreferences;
    public static ArrayList<String> courseList;
    private int k = 1;
    public static ArrayList<String> courseSegmentList;
    public static ArrayList<String> slotList;
    public static ArrayList<String> CourseName;
    private SharedPreferences sharedPref;
    private TextView t1, t2, t3, t4 , mealName;
    private MultiStateToggleButton toggleButton;

    private ArrayList<Lecture> lectures1 = new ArrayList<>();
    private ArrayList<String> T1 = new ArrayList<>();
    private ArrayList<String> T2 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_screen, container, false);

        mess =  view.findViewById(R.id.MessCard);
        myRV = view.findViewById(R.id.Timetable_Recycler);
        view.findViewById(R.id.MessScroll);
        mealName = view.findViewById(R.id.textView15);
        cab = view.findViewById(R.id.CabCard);
        mess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_mess);
            }
        });

        myRV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_acads);
            }
        });
        timetable = view.findViewById(R.id.TimetableCard);
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_acads);
            }
        });
        bus = view.findViewById(R.id.BusCard);
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new FragmentBS()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_bus);
            }
        });


        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CabSharing.class));

            }
        });


        t1 = view.findViewById(R.id.toLing);
        t2 = view.findViewById(R.id.toODF);
        toggleButton = view.findViewById(R.id.BusToggleHome);
        toggleButton.setValue(0);
        t3 = view.findViewById(R.id.toLing2);
        t4 = view.findViewById(R.id.toODF2);


        toggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                busmake(true);
            }
        });


        CabSharing = view.findViewById(R.id.cs_home_recycler);
        CabSharing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , CabSharing.class));
            }
        });
        mess1 = view.findViewById(R.id.menu_home);
        mess1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_mess);

            }
        });

        mealName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_mess);
            }
        });
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = sharedPref;
        System.out.println(sharedPref.getBoolean("cab", false));
        cabCardMake(sharedPref.getBoolean("cab", true));
        busmake(sharedPref.getBoolean("bus", true));
        messmake(sharedPref.getBoolean("mess", true));
        timetablemake(sharedPref.getBoolean("timetable" , true));


        return view;
    }

    private void busmake(boolean b) {
        if (b) {
            String string;
            String temp;
            bus.setVisibility(View.VISIBLE);
            try {
                JSONObject JO = null;
                if (toggleButton.getValue() == 1) {
                    JO = new JSONObject(sharedPref.getString("ToIITH", "{\"LAB\":\"02:00 ,02:30 ,03:15 ,04:00 ,04:45 ,05:30 ,06:15 ,07:00 ,07:30 ,08:00 ,08:30 ,13:15 ,14:30 ,15:00 ,19:45 ,20:30,\",\"LINGAMPALLY\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODF\":\"08:20 , 09:30 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,19:35 ,20:30 ,21:00 ,22:00 ,23:00,\",\"SANGAREDDY\":\"08:45 ,13:45 ,18:00 ,18:40 ,22:00,\",\"LINGAMPALLYW\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODFS\":\"08:00 ,09:15 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,18:30 ,19:45 ,20:30 ,21:30 ,23:00,\"}"));
                } else {
                    JO = new JSONObject(sharedPref.getString("FromIITH", "{  \"LAB\": \"01:45 ,02:15 ,03:00 ,03:45 ,04:30 ,05:15 ,06:00 ,06:45 ,07:15 ,07:45 ,08:15 ,13:00 ,14:15 ,14:45 ,19:30 ,20:15,\",  \"LINGAMPALLY\": \"11:30 ,13:15 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODF\": \"09:00 ,10:30 ,12:10 ,13:10 ,14:45 ,17:45 ,18:00 ,19:00 ,20:15 ,21:00 ,22:00 ,23:00,\",  \"SANGAREDDY\": \"08:30 ,13:30 ,17:45 ,18:25 ,20:40,\",  \"LINGAMPALLYW\": \"10:30 ,12:30 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODFS\": \"08:40 ,10:15 ,12:10 ,13:15 ,14:45 ,16:10 ,17:45 ,19:10 ,20:30 ,21:10 ,22:10 ,23:30,\"}"));
                }
                Date date = Calendar.getInstance().getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY:MM:dd");
                String da = dateFormat.format(Calendar.getInstance().getTime());
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
                            t1.setText(temp);
                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                    string = JO.getString("ODFS");
                } else {
                    string = JO.getString("ODF");
                }
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
                        ;
                        java.util.Date T1 = format1.parse(da + ":" + temp.trim());

                        if (T1.compareTo(date) > 0) {
                            t2.setText(temp);
                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }


                //String da = dateFormat.format(Calendar.getInstance().getTime());
                string = JO.getString("LAB");
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:ddHH:mm");
                        ;
                        java.util.Date T1 = format1.parse(da + temp.trim());

                        if (T1.compareTo(date) > 0) {
                            t3.setText(temp);
                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }
                string = JO.getString("SANGAREDDY");
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:ddHH:mm");
                        ;
                        t4.setText(temp);

                        java.util.Date T1 = format1.parse(da + temp.trim());

                        if (T1.compareTo(date) > 0) {

                            break;
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


        } else {
            bus.setVisibility(View.GONE);
        }
    }

    private void messmake(boolean b) {
        if (b) {
            mess.setVisibility(View.VISIBLE);

            String data;
            try {
                if (sharedPref.getInt("MESSDEF", 1) == 1) {
                    data = sharedPref.getString("UDH", MainActivity.UDH);
                } else {
                    data = sharedPref.getString("LDH", MainActivity.LDH);
                }

                JSONArray Data = new JSONArray(data);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


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
                JSONObject JO = Data.getJSONObject(day - 1);
                System.out.println(day);

                JSONObject JO2 = Data.getJSONObject(day % 7);

                if (timen.before(time1)) {
                    mealName.setText("Today's Breakfast");
                    mess1.setText(JO.getString("Breakfast"));

                } else if (timen.after(calendar1.getTime()) && timen.before(calendar2.getTime())) {
                    //checkes whether the current time is between 10:30:00 and 14:30:00.
                    mealName.setText("Today's Lunch");
                    mess1.setText(JO.getString("Lunch"));

                } else if (timen.after(calendar2.getTime()) && timen.before(calendar3.getTime())) {
                    mealName.setText("Today's Snacks");
                    mess1.setText(JO.getString("Snacks"));

                } else if (timen.after(calendar3.getTime()) && timen.before(calendar4.getTime())) {
                    mealName.setText("Today's Dinner");
                    mess1.setText(JO.getString("Dinner"));

                } else if (timen.after(time4)) {
                    mealName.setText("Tomorrow's Breakfast");

                    mess1.setText(JO2.getString("Breakfast"));


                }


            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            mess.setVisibility(View.GONE);
        }
    }

    private void timetablemake(boolean b) {
        if (b) {
            try {


                timetable.setVisibility(View.VISIBLE);
                courseList = getArrayList("CourseList");
                courseSegmentList = getArrayList("Segment");
                slotList = getArrayList("SlotList");
                CourseName = getArrayList("CourseName");
                if(courseList != null) {
                    mapData();
                    daily(sharedPref.getString("DefaultSegment" , "12"));
                }


            } catch (Exception e) {

            }
        }



         else {
            timetable.setVisibility(View.GONE);
        }
    }


    private void cabCardMake(boolean b) {
        if (b) {
            if(sharedPref.getBoolean("Registered", false)) {

                CabGet();
            }
            else{
                mEmails.clear();
                mEmails.add("You are not using this feature");
                cab.setVisibility(View.VISIBLE);
                CabSharing = view.findViewById(R.id.cs_home_recycler);
                RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mEmails);
                CabSharing.setAdapter(adapter);
                CabSharing.setLayoutManager(new LinearLayoutManager(getContext()));
                return;
            }



        } else {
            cab.setVisibility(View.GONE);
        }

    }


    private void CabGet(){
        final String startTime = sharedPref.getString("startTime", "    NA      NA  ");
        final String endTime = sharedPref.getString("endTime", "    NA      NA  ");
        final int CabID = sharedPref.getInt("Route", 100);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = "http://www.iith.dev/query";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            // Name, email address, and profile photo Url

            email = user.getEmail().toString();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        JSONArray JA = null;

                        try {
                            JA = new JSONArray(response);
                            mNames.clear();
                            mEmails.clear();
                            JSONArray JA2 = new JSONArray();
                            for (int i = 0; i < JA.length(); i++) {
                                JSONObject JO = (JSONObject) JA.get(i);


                                SimpleDateFormat format1 = new SimpleDateFormat("YYYY-mm-dd:HH:MM");
                                java.util.Date T1 = format1.parse(startTime.substring(0, 10) + ":" + startTime.substring(11, 16));
                                java.util.Date T2 = format1.parse(endTime.substring(0, 10) + ":" + endTime.substring(11, 16));
                                java.util.Date T3 = format1.parse(JO.getString("StartTime").substring(0, 10) + ":" + JO.getString("StartTime").substring(11, 16));
                                java.util.Date T4 = format1.parse(JO.getString("EndTime").substring(0, 10) + ":" + JO.getString("EndTime").substring(11, 16));
                                System.out.println(T3.toString() + T1.toString());
                                if ((JO.getInt("RouteID") == CabID && !((JO.getString("Email")).equals(email)) && (JO.getString("StartTime").substring(0, 10)).equals(startTime.substring(0, 10)) && ((T3.compareTo(T1) >= 0 && T3.compareTo(T2) <= 0) || (T4.compareTo(T1) >= 0 && T4.compareTo(T2) <= 0)))) {

                                    JA2.put(JO);
                                    //mNames.add(JO.getString(""));
                                    mEmails.add(JO.getString("Email"));

                                }

                            }
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("CabShares", JA2.toString());
                            editor.commit();
                            cab.setVisibility(View.VISIBLE);

                            CabSharing = view.findViewById(R.id.cs_home_recycler);
                            RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mEmails);
                            CabSharing.setAdapter(adapter);
                            CabSharing.setLayoutManager(new LinearLayoutManager(getContext()));
                        } catch (JSONException e) {
                            cab.setVisibility(View.VISIBLE);

                            e.printStackTrace();
                        } catch (ParseException e) {
                            cab.setVisibility(View.VISIBLE);
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                try {
                    mNames.clear();
                    mEmails.clear();
                    JSONArray JA4 = new JSONArray(sharedPref.getString("CabShares", "NULL"));
                    JSONObject JO = new JSONObject();
                    for (int k = 0; k < JA4.length(); k++) {
                        JO = JA4.getJSONObject(k);
                        mEmails.add(JO.getString("Email"));

                        CabSharing = view.findViewById(R.id.cs_home_recycler);
                        RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mEmails);
                        CabSharing.setAdapter(adapter);
                        CabSharing.setLayoutManager(new LinearLayoutManager(getContext()));

                    }
                } catch (JSONException e) {

                    e.printStackTrace();

                }
            }
        });

        cab.setVisibility(View.VISIBLE);
        queue.add(stringRequest);
    }
    @Override
    public void onResume() {
        super.onResume();
        cabCardMake(sharedPref.getBoolean("cab", false));
        busmake(sharedPref.getBoolean("bus", true));
        messmake(sharedPref.getBoolean("mess", true));
        timetablemake(sharedPref.getBoolean("timetable", true));
    }

    private ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = sharedPref;
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    private Lecture create(String id , String name){
        Lecture lecture = new Lecture();
        lecture.setCourseId(id);
        lecture.setCourse(name);
        return lecture;
    }
    private void mapData() {

        int n = courseList.size();
        HashMap <String , Lecture> HM  = new HashMap<>();
        HashMap <String , Lecture> HM2  = new HashMap<>();
        HashMap <String , Lecture> HM3  = new HashMap<>();
        HM.put("A" , create("",""));
        HM.put("B" ,create("",""));
        HM.put("C" , create("",""));
        HM.put("D" , create("",""));
        HM.put("E" , create("",""));
        HM.put("F" , create("",""));
        HM.put("G" , create("",""));
        HM.put("P" , create("",""));
        HM.put("Q" , create("",""));
        HM.put("R" , create("",""));
        HM.put("S" , create("",""));
        HM.put("W" , create("",""));
        HM.put("X" , create("",""));
        HM.put("Y" , create("",""));
        HM.put("Z" , create("",""));
        HM2.put("A" , create("",""));
        HM2.put("B" ,create("",""));
        HM2.put("C" , create("",""));
        HM2.put("D" , create("",""));
        HM2.put("E" , create("",""));
        HM2.put("F" , create("",""));
        HM2.put("G" , create("",""));
        HM2.put("P" , create("",""));
        HM2.put("Q" , create("",""));
        HM2.put("R" , create("",""));
        HM2.put("S" , create("",""));
        HM2.put("W" , create("",""));
        HM2.put("X" , create("",""));
        HM2.put("Y" , create("",""));
        HM2.put("Z" , create("",""));
        HM3.put("A" , create("",""));
        HM3.put("B" ,create("",""));
        HM3.put("C" , create("",""));
        HM3.put("D" , create("",""));
        HM3.put("E" , create("",""));
        HM3.put("F" , create("",""));
        HM3.put("G" , create("",""));
        HM3.put("P" , create("",""));
        HM3.put("Q" , create("",""));
        HM3.put("R" , create("",""));
        HM3.put("S" , create("",""));
        HM3.put("W" , create("",""));
        HM3.put("X" , create("",""));
        HM3.put("Y" , create("",""));
        HM3.put("Z" , create("",""));

        courseMap.put("12" ,HM);
        courseMap.put("34" ,HM2);
        courseMap.put("56" ,HM3);

        for (int i = 0; i < n; i++) {
            System.out.println(i);

            if (courseSegmentList.get(i).contains("12")) {
                //courseMap.put("12",new HashMap<String, String>());

                HashMap <String , Lecture> h1 = (courseMap.get("12"));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get("12")).get(slotList.get(i)).setCourseId(courseList.get(i));

            }
            if (courseSegmentList.get(i).contains("34")) {
                HashMap <String , Lecture> h1 = (courseMap.get("34"));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get("34")).get(slotList.get(i)).setCourseId(courseList.get(i));
            }
            if (courseSegmentList.get(i).contains("56")) {
                HashMap <String , Lecture> h1 = (courseMap.get("56"));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get("56")).get(slotList.get(i)).setCourseId(courseList.get(i));
            }
        }

    }

    private void daily(String segment){
        lectures1.clear();
        T1.clear();
        T2.clear();


        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
        System.out.println("DAY" + day);
        switch (day){
            case 2: {
                dailyCreate("ABCDPQWX"  , segment);

                return;

            }
            case 3: {
                dailyCreate("DEFGRSYZ", segment) ;

                return;
            }
            case 4:{
                dailyCreate("BCAGF", segment) ;

                return;
            }
            case 5:{
                dailyCreate("CABEQBWX", segment) ;

                return;
            }
            case 6:{
                dailyCreate("EFDGSRYZ", segment);

                return;
            }
            case 7:{
                dailyCreate("", segment);
                return;}



            case 1: {
                dailyCreate("", segment);
                return;


            }



        }



    }



    private void dailyCreate(String string , String segment ) {


        HashMap<String, Lecture> course = courseMap.get(segment);

        System.out.println("ADSD" + course);
        ArrayList<ArrayList<String>> time = new ArrayList<>();
        ArrayList<String> t = new ArrayList<>();
        ArrayList<String> t1 = new ArrayList<>();
        ArrayList<String> t2 = new ArrayList<>();
        ArrayList<String> t3 = new ArrayList<>();
        ArrayList<String> t4 = new ArrayList<>();
        ArrayList<String> t5 = new ArrayList<>();
        ArrayList<String> t6 = new ArrayList<>();
        ArrayList<String> t7 = new ArrayList<>();
        t.add("09:00");
        t.add("10:00");
        time.add(t);

        t1.add("10:00");
        t1.add("11:00");
        time.add(t1);

        t2.add("11:00");
        t2.add("12:00");
        time.add(t2);

        t3.add("12:00");
        t3.add("13:00");
        time.add(t3);

        t4.add("14:30");
        t4.add("16:00");
        time.add(t4);

        t5.add("16:00");
        t5.add("17:30");
        time.add(t5);

        t6.add("17:30");
        t6.add("19:00");
        time.add(t6);

        t7.add("19:00");
        t7.add("20:30");
        time.add(t7);



        System.out.println(string);

        int temp = 0;
        for (int i = 0; i < string.length(); i++) {

            System.out.println(string.substring(i, i + 1));
            System.out.println(course.get(string.substring(i, i + 1)));


            if (!course.get(string.substring(i, i + 1)).getCourseId().equals("")) {



                lectures1.add(course.get(string.substring(i, i + 1)));
                T1.add(time.get(i).get(0));
                T2.add(time.get(i).get(1));
                temp++;
            }
        }
        System.out.println("DD" + temp);
        if (temp==0){
            Lecture lecture = new Lecture();
            lecture.setCourse("NO LECTURES TODAY! Enjoy");

            lectures1.add(lecture);
            T1.add("");
            T2.add("");
        }

        HomeTimeTableAdapter adapter = new HomeTimeTableAdapter(getContext() , lectures1 , T1 , T2);
        myRV.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
        myRV.setLayoutManager(layout);
        return;
    }


}
