package com.lambda.iith.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.constraint.Constraints;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lambda.iith.dashboard.Timetable.Timetable;

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

import Adapters.HomeTimeTableAdapter;
import Adapters.RecyclerViewAdapter_CSHOME;
import Model.Lecture;


public class HomeScreenFragment extends Fragment {

    public static SharedPreferences sharedPreferences;
    public static ArrayList<String> courseList;
    public static ArrayList<String> courseSegmentList;
    public static ArrayList<String> slotList;
    public static ArrayList<String> CourseName;
    //Fragment to be displayed on Home screen which will have 3 cards as layout.
    private CardView bus, mess, timetable, cab;
    private RecyclerView CabSharing;
    private TextView mess1;
    private ArrayList<String> mEmails = new ArrayList<>();
    private View view;
    private RecyclerView myRV;
    private SharedPreferences sharedPref;
    private TextView t1, t2, t3, t4, mealName , b1 , b2 , b3 , b4;
    private MultiStateToggleButton toggleButton;
    private ConstraintLayout constraintLayout;
    private ArrayList<Lecture> lectures1 = new ArrayList<>();
    private ArrayList<String> T1 = new ArrayList<>();
    private ArrayList<String> T2 = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_screen, container, false);
        constraintLayout = view.findViewById(R.id.busview);
        mess = view.findViewById(R.id.MessCard);
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
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_cab);

            }
        });

        final float scale = getResources().getDisplayMetrics().density;
        t1 = view.findViewById(R.id.toLing);
        t2 = view.findViewById(R.id.toODF);
        toggleButton = view.findViewById(R.id.BusToggleHome);
        toggleButton.setValue(0);
        t3 = view.findViewById(R.id.toLing2);
        t4 = view.findViewById(R.id.toODF2);
        b1 = view.findViewById(R.id.textView23);
        b2 = view.findViewById(R.id.textView24);
        b3 = view.findViewById(R.id.textView5);
        b4 = view.findViewById(R.id.textView7);

        double width2 = convertPxToDp(getContext(),Launch.width/4);
        ViewGroup.LayoutParams params = t1.getLayoutParams();
        params.width = (int) ((width2-5) * scale + 0.5f);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        t1.setLayoutParams(params);
        t2.setLayoutParams(params);
        t3.setLayoutParams(params);
        t4.setLayoutParams(params);
        b1.setLayoutParams(params);
        b2.setLayoutParams(params);
        b3.setLayoutParams(params);
        b4.setLayoutParams(params);
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
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_cab);
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

        cabCardMake(sharedPref.getBoolean("cab", true));
        busmake(sharedPref.getBoolean("bus", true));
        messmake(sharedPref.getBoolean("mess", true));
        timetablemake(sharedPref.getBoolean("timetable", true));


        return view;
    }
    public double convertPxToDp(Context context, double px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
    private void busmake(boolean b) {
        if (b) {
            bus.setVisibility(View.VISIBLE);
            new GetNextBus(sharedPreferences , toggleButton.getValue()).execute(t1 , t2 ,t3,t4);

        } else {
            bus.setVisibility(View.GONE);
        }
    }

    private void messmake(boolean b) {
        if (b) {
            mess.setVisibility(View.VISIBLE);
            JSONObject data = new JSONObject();
            JSONObject data1 = new JSONObject();
            JSONObject JO1 = new JSONObject();
            try {

                JSONObject JA;
                if(sharedPreferences.getString("MESSJSON", "NULL").equals("NULL")){
                    Init init = new Init();
                    JO1 = new JSONObject(Init.DEF);

                }
                else{
                    JO1 = new JSONObject(sharedPreferences.getString("MESSJSON" , "NULL"));}
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                if (sharedPref.getInt("MESSDEF", 1) == 1) {
                    data = JO1.getJSONObject("UDH");
                    data1 = JO1.getJSONObject("UDH Additional");
                } else {
                    data = JO1.getJSONObject("LDH");
                    data1 = JO1.getJSONObject("LDH Additional");
                }


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
                ArrayList<String> a1 = new ArrayList<>();
                a1.add("Sunday");
                a1.add("Monday");
                a1.add("Tuesday");
                a1.add("Wednesday");
                a1.add("Thursday");
                a1.add("Friday");
                a1.add("Saturday");


                JSONObject JO = data.getJSONObject(a1.get(day - 1));
                JSONObject JO11 = data1.getJSONObject(a1.get(day - 1));
                JSONObject JO3 = data1.getJSONObject(a1.get(day % 7));
                JSONObject JO2 = data.getJSONObject(a1.get(day % 7));

                if (timen.before(time1)) {
                    mealName.setText("Today's Breakfast");
                    JSONArray L = JO.getJSONArray("Breakfast");
                    JSONArray L1 = JO11.getJSONArray("Breakfast");


                    mess1.setText(parseMeal(L, L1));

                } else if (timen.after(calendar1.getTime()) && timen.before(calendar2.getTime())) {
                    //checkes whether the current time is between 10:30:00 and 14:30:00.
                    mealName.setText("Today's Lunch");
                    JSONArray L = JO.getJSONArray("Lunch");
                    JSONArray L1 = JO11.getJSONArray("Lunch");

                    mess1.setText(parseMeal(L, L1));

                } else if (timen.after(calendar2.getTime()) && timen.before(calendar3.getTime())) {
                    mealName.setText("Today's Snacks");
                    JSONArray L = JO.getJSONArray("Snacks");
                    JSONArray L1 = JO11.getJSONArray("Snacks");

                    mess1.setText(parseMeal(L, L1));

                } else if (timen.after(calendar3.getTime()) && timen.before(calendar4.getTime())) {
                    mealName.setText("Today's Dinner");
                    JSONArray L = JO.getJSONArray("Dinner");
                    JSONArray L1 = JO11.getJSONArray("Dinner");


                    mess1.setText(parseMeal(L, L1));

                } else if (timen.after(time4)) {
                    mealName.setText("Tomorrow's Breakfast");

                    JSONArray L = JO2.getJSONArray("Breakfast");
                    JSONArray L1 = JO3.getJSONArray("Breakfast");

                    mess1.setText(parseMeal(L, L1));


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

    private void timetablemake(boolean b) {
        if (b) {
            try {


                timetable.setVisibility(View.VISIBLE);
                courseList = getArrayList("CourseList");
                courseSegmentList = getArrayList("Segment");
                slotList = getArrayList("SlotList");
                CourseName = getArrayList("CourseName");
                if (courseList != null) {

                    daily(sharedPref.getString("DefaultSegment", "12"));
                }


            } catch (Exception e) {

            }
        } else {
            timetable.setVisibility(View.GONE);
        }
    }


    private void cabCardMake(boolean b) {
        if (b) {
            if (sharedPref.getBoolean("Registered", false)) {

                CabGet();
            } else {
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


    private void CabGet() {


        try {

            mEmails.clear();

            JSONArray JA3 = new JSONArray(sharedPref.getString("CabShares", null));
            JSONObject JO3;
            for (int j = 0; j < JA3.length(); j++) {
                JO3 = (JSONObject) JA3.get(j);
                mEmails.add(JO3.getString("Email"));
                CabSharing = view.findViewById(R.id.cs_home_recycler);
                RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mEmails);
                CabSharing.setAdapter(adapter);
                CabSharing.setLayoutManager(new LinearLayoutManager(getContext()));


            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

        cab.setVisibility(View.VISIBLE);

    }

    @Override
    public void onResume() {
        super.onResume();
        cabCardMake(sharedPref.getBoolean("cab", true));
        busmake(sharedPref.getBoolean("bus", true));
        messmake(sharedPref.getBoolean("mess", true));
        timetablemake(sharedPref.getBoolean("timetable", true));
    }

    private ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = sharedPref;
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    private void daily(String segment) {
        lectures1.clear();
        T1.clear();
        T2.clear();


        int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        switch (day) {
            case 2: {
                dailyCreate("ABCDPQWX", segment);

                return;

            }
            case 3: {
                dailyCreate("DEFGRSYZ", segment);

                return;
            }
            case 4: {
                dailyCreate("BCAGF", segment);

                return;
            }
            case 5: {
                dailyCreate("CABEQPWX", segment);

                return;
            }
            case 6: {
                dailyCreate("EFDGSRYZ", segment);

                return;
            }
            case 7:


            case 1: {
                dailyCreate("", segment);
                return;
            }


        }


    }


    private void dailyCreate(String string, String segment) {


        ArrayMap<String, Lecture> course = Timetable.courseMap.get((Integer.parseInt(segment.substring(0, 1)) - 1) / 2);


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


        int temp = 0;
        for (int i = 0; i < string.length(); i++) {


            if (!course.get(string.substring(i, i + 1)).getCourseId().equals("")) {


                lectures1.add(course.get(string.substring(i, i + 1)));
                T1.add(time.get(i).get(0));
                T2.add(time.get(i).get(1));
                temp++;
            }
        }

        if (temp == 0) {
            Lecture lecture = new Lecture();
            lecture.setCourse("NO LECTURES TODAY! Enjoy");

            lectures1.add(lecture);
            T1.add("");
            T2.add("");
        }

        HomeTimeTableAdapter adapter = new HomeTimeTableAdapter(getContext(), lectures1, T1, T2);
        myRV.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myRV.setLayoutManager(layout);
        return;
    }


}
