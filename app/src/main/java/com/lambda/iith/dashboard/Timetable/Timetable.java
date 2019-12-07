package com.lambda.iith.dashboard.Timetable;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lambda.iith.dashboard.Launch;
import com.lambda.iith.dashboard.R;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Adapters.RecyclerViewAdapter2_TT;
import Adapters.RecyclerViewAdapter_TT;
import Adapters.TimeTableLegendAdapter;
import Model.Lecture;

public class Timetable extends Fragment {


    public static Context mContext;
    public static ArrayList<ArrayMap<String, Lecture>> courseMap = new ArrayList<>();
    public static SharedPreferences sharedPreferences;
    public static ArrayList<String> courseList;
    public static ArrayList<String> courseSegmentList;
    public static ArrayList<String> slotList;
    public static ArrayList<String> CourseName;
    public static Spinner segment;
    private static FragmentManager fragmentManager;
    List<Lecture> lectureList;
    private String Seg = "12";
    private MultiStateToggleButton timetableView, DaySelect;
    //private ArrayMap<String, ArrayMap<String, Lecture>> courseMap = new ArrayMap<>();
    private LinearLayout Days, c1, c2, c3, c4, c5;
    private RecyclerView myRV, legend;
    private int k = 1;
    private RelativeLayout Parent;
    private ArrayList<Lecture> lectures1 = new ArrayList<>();
    private ArrayList<String> T1 = new ArrayList<>();
    private ArrayList<String> T2 = new ArrayList<>();
    private ArrayList<Integer> colour;

    public static ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = sharedPreferences;
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public static void edit(String CourseCode, String Name) {
        int n = courseList.size();


        for (int i = 0; i < n; i++) {

            if (courseList.get(i).equals(CourseCode)) {

                CourseName.set(i, Name);
                saveArrayList(courseList, "CourseList");

                saveArrayList(courseSegmentList, "Segment");
                sharedPreferences.edit().putBoolean("RequireReset", true).commit();
                saveArrayList(slotList, "SlotList");
                saveArrayList(CourseName, "CourseName");
                new timetableComp().execute(mContext);
                int i1 = segment.getSelectedItemPosition();

                final FragmentTransaction ft = fragmentManager.beginTransaction();

                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);
                ft.detach(fragment);
                ft.attach(fragment);

                ft.commit();
                segment.setSelection(i1);
            }

        }


    }

    public static void addCourse(String name, String code, String slot, String segment) {
        if (courseList.contains(code)) {
            Toast.makeText(mContext, "Course already exists", Toast.LENGTH_SHORT).show();
            return;
        } if(slotList.contains(slot)){

            for(int i=0 ; i<courseList.size() ; i++){
                if(slotList.get(i).equals(slot) && courseSegmentList.get(i).contains(segment)){
                    Toast.makeText(mContext, "Course Clashes", Toast.LENGTH_SHORT).show();
                    return;

                }

            }

        }


        courseList.add(code);
        courseSegmentList.add(segment);
        slotList.add(slot);
        CourseName.add(name);
        sharedPreferences.edit().putBoolean("RequireReset", true).commit();
        saveArrayList(courseList, "CourseList");

        saveArrayList(courseSegmentList, "Segment");

        saveArrayList(slotList, "SlotList");
        saveArrayList(CourseName, "CourseName");

        final FragmentTransaction ft = fragmentManager.beginTransaction();


    }

    public static void Delete(String CourseID) {

        courseList = getArrayList("CourseList");
        courseSegmentList = getArrayList("Segment");
        slotList = getArrayList("SlotList");
        CourseName = getArrayList("CourseName");
        int n = courseList.size();


        for (int i = 0; i < n; i++) {

            if (courseList.get(i).equals(CourseID)) {
                courseList.remove(i);
                courseSegmentList.remove(i);
                slotList.remove(i);
                CourseName.remove(i);

                saveArrayList(courseList, "CourseList");

                saveArrayList(courseSegmentList, "Segment");

                saveArrayList(slotList, "SlotList");
                saveArrayList(CourseName, "CourseName");

                break;


            }


        }
        sharedPreferences.edit().putBoolean("RequireReset", true).commit();
        final FragmentTransaction ft = fragmentManager.beginTransaction();
        timetableComp timetableComp = new timetableComp();
        timetableComp.execute(mContext);
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);
        ft.detach(fragment);
        ft.attach(fragment);
        ft.commit();
    }

    public static void saveArrayList(List<String> list, String key) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.commit();     // This line is IMPORTANT !!!


    }

    private void refreshNotificationProcess() {
        WorkManager.getInstance().cancelAllWork();
        //OneTimeWorkRequest oneTimeWorkRequest = new OneTimeWorkRequest.Builder(NotificationInitiator.class).setInitialDelay(1000 , TimeUnit.MILLISECONDS).build();
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationInitiator.class, 6, TimeUnit.HOURS)
                .build();
        WorkManager.getInstance()
                .enqueue(periodicWorkRequest);
    }


    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        final View view = inflater.inflate(R.layout.recycler_view_timetable, container, false);
        myRV = view.findViewById(R.id.timetable_rv);
        timetableView = view.findViewById(R.id.timetableView);
        legend = view.findViewById(R.id.Legend);
        Parent = view.findViewById(R.id.Timetableparent);
        timetableView.setValue(1);
        fragmentManager = getFragmentManager();
        lectureList = new ArrayList<>(72);
        mContext = getContext();

        final float scale = getContext().getResources().getDisplayMetrics().density;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        c1 = view.findViewById(R.id.DayCard1);
        c2 = view.findViewById(R.id.DayCard2);
        c3 = view.findViewById(R.id.DayCard3);
        c4 = view.findViewById(R.id.DayCard4);
        c5 = view.findViewById(R.id.DayCard5);
        Days = view.findViewById(R.id.days);
        int width = (int) Math.floor(convertPxToDp(getContext(), Launch.width / 5));
        ViewGroup.LayoutParams params1 = c1.getLayoutParams();
        params1.width = (int) ((width - 10) * scale + 0.5f);
        c1.setLayoutParams(params1);
        c2.setLayoutParams(params1);
        c3.setLayoutParams(params1);
        c4.setLayoutParams(params1);
        c5.setLayoutParams(params1);

        DaySelect = view.findViewById(R.id.DaySelect);
        courseList = getArrayList("CourseList");
        courseSegmentList = getArrayList("Segment");
        slotList = getArrayList("SlotList");
        CourseName = getArrayList("CourseName");
        int t = 0;


        if (courseList == null) {
            courseList = new ArrayList<>();
            courseSegmentList = new ArrayList<>();
            slotList = new ArrayList<>();
            CourseName = new ArrayList<>();

        }
        segment = view.findViewById(R.id.segmentselect);


        String seg = sharedPreferences.getString("DefaultSegment", "12");

        if (seg.equals("12")) {

            Timetable.segment.setSelection(0);


        } else if (seg.equals("34")) {

            Timetable.segment.setSelection(1);

        } else if (seg.equals("56")) {

            Timetable.segment.setSelection(2);

        }
        timetableView.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                k = value;
                if (value == 0) {

                    daily(Seg);
                } else {
                    arrgen(Seg);
                }
            }
        });


        segment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (parent.getItemAtPosition(position).toString().equals("1-2")) {
                    Seg = "12";
                } else if (parent.getItemAtPosition(position).toString().equals("3-4")) {
                    Seg = "34";
                } else {
                    Seg = "56";
                }
                if (k == 1) {
                    arrgen(Seg);

                } else {
                    daily(Seg);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;

            }
        });
        return view;
    }


    public double convertPxToDp(Context context, double px) {
        return px / context.getResources().getDisplayMetrics().density;
    }


    private void arrgen(String segment) {
        showLegend(segment);
        Days.setVisibility(View.VISIBLE);
        DaySelect.setVisibility(View.GONE);

        ArrayMap<String, Lecture> course = courseMap.get((Integer.parseInt(segment.substring(0, 1)) - 1) / 2);
        lectureList.clear();
        int def = getResources().getColor(R.color.brandedsurface);
        lectureList.add(create("9", "", def));
        lectureList.add((create("10", "", def)));
        lectureList.add(create("11", "", def));
        lectureList.add(create("12", "", def));
        lectureList.add(create("14:30", "", def));
        lectureList.add(create("16", "", def));
        lectureList.add(create("17:30", "", def));
        lectureList.add(create("19", "", def));
        lectureList.add(course.get("A"));
        lectureList.add(course.get("B"));
        lectureList.add(course.get("C"));
        lectureList.add(course.get("D"));
        lectureList.add(course.get("P"));
        lectureList.add(course.get("Q"));
        lectureList.add(course.get("W"));
        lectureList.add(course.get("X"));
        lectureList.add(course.get("D"));
        lectureList.add(course.get("E"));
        lectureList.add(course.get("F"));
        lectureList.add(course.get("G"));
        lectureList.add(course.get("R"));
        lectureList.add(course.get("S"));
        lectureList.add(course.get("Y"));
        lectureList.add(course.get("Z"));
        lectureList.add(course.get("B"));
        lectureList.add(course.get("C"));
        lectureList.add(course.get("A"));
        lectureList.add(course.get("G"));
        lectureList.add(course.get("F"));
        lectureList.add(create("", "", def));
        lectureList.add(create("", "", def));
        lectureList.add(create("", "", def));
        lectureList.add(course.get("C"));
        lectureList.add(course.get("A"));
        lectureList.add(course.get("B"));
        lectureList.add(course.get("E"));
        lectureList.add(course.get("Q"));
        lectureList.add(course.get("P"));
        lectureList.add(course.get("W"));
        lectureList.add(course.get("X"));
        lectureList.add(course.get("E"));
        lectureList.add(course.get("F"));
        lectureList.add(course.get("D"));
        lectureList.add(course.get("G"));
        lectureList.add(course.get("S"));
        lectureList.add(course.get("R"));
        lectureList.add(course.get("Y"));
        lectureList.add(course.get("Z"));
        RecyclerViewAdapter_TT myAdapter = new RecyclerViewAdapter_TT(getContext(), lectureList);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(8, StaggeredGridLayoutManager.HORIZONTAL);
        myRV.setAdapter(myAdapter);
        myRV.setLayoutManager(manager);
        myRV.setHasFixedSize(false);
    }

    private Lecture create(String id, String name, int color) {
        Lecture lecture = new Lecture();
        lecture.setCourseId(id);
        lecture.setCourse(name);
        lecture.setCourseColor(color);
        return lecture;
    }

    private void daily(final String segment) {

        DaySelect.setVisibility(View.VISIBLE);
        Days.setVisibility(View.GONE);
        legend.setVisibility(View.GONE);
        Parent.removeView(legend);
        DaySelect.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                matchDay(value + 1, segment);
            }
        });
        DaySelect.setValue(Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1);
    }


    private void matchDay(int day, String segment) {

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
            default:
                dailyCreate("", segment);
                return;
        }
    }



    private void dailyCreate(String string, String segment) {
        ArrayMap<String, Lecture> course = courseMap.get((Integer.parseInt(segment.substring(0, 1)) - 1) / 2);
        lectures1.clear();
        T1.clear();
        T2.clear();
        String[] start = {"09:00", "10:00", "11:00", "12:00", "14:30", "16:00", "17:30", "19:00"};
        String[] end = {"10:00", "11:00", "12:00", "13:00", "16:00", "17:30", "19:00", "20:30"};

        for (int i = 0; i < string.length(); i++) {
            if (!course.get(string.substring(i, i + 1)).getCourseId().equals("")) {
                lectures1.add(course.get(string.substring(i, i + 1)));
                T1.add(start[i]);
                T2.add(end[i]);

            }
        }

        RecyclerViewAdapter2_TT adapter = new RecyclerViewAdapter2_TT(getContext(), lectures1, T1, T2);
        myRV.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        myRV.setLayoutManager(layout);
        return;
    }


    @Override
    public void onResume() {
        super.onResume();


    }

    @Override
    public void onStart() {
        if (sharedPreferences.getBoolean("TimeTableLaunch1", true)) {
            sharedPreferences.edit().putBoolean("TimeTableLaunch1", false).commit();
            AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
            alert.setTitle("TIP");
            alert.setMessage("You can use AIMS Helper Chrome extension to load timetable directly from AIMS");
            alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(getContext(), "Longpress cards to put course name", Toast.LENGTH_SHORT).show();

                }
            });
            alert.show();
        }
        super.onStart();
    }


    private void showLegend(String segment) {

        if (!sharedPreferences.getBoolean("Cname", true)) {
            ArrayList<String> mCourseNames = new ArrayList<>();
            ArrayList<String> mCourseCodes = new ArrayList<>();
            int n = courseSegmentList.size();
            for (int j = 0; j < n; j++) {
                if (courseSegmentList.get(j).contains(segment)) {
                    mCourseNames.add(CourseName.get(j));
                    mCourseCodes.add(courseList.get(j));


                }
            }
            Parent.removeView(legend);
//            Parent.addView(legend);
            legend.setVisibility(View.VISIBLE);
            TimeTableLegendAdapter timeTableLegendAdapter = new TimeTableLegendAdapter(getContext(), mCourseNames, mCourseCodes);
            legend.setAdapter(timeTableLegendAdapter);
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);

            legend.setLayoutManager(layoutManager);
        } else {
            legend.setVisibility(View.GONE);
            Parent.removeView(legend);
        }


    }
}
