package com.lambda.iith.dashboard.Timetable;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.ArrayMap;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lambda.iith.dashboard.R;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Random;

import Model.Lecture;

public class timetableComp extends AsyncTask<Context, Void, ArrayList<ArrayMap<String, Lecture>>> {
    public static ArrayList<String> courseList;
    public static ArrayList<String> courseSegmentList;
    public static ArrayList<String> slotList;
    public static ArrayList<String> CourseName;
    public static ArrayList<Integer> colorArray = new ArrayList<>();
    //private ArrayMap<String, ArrayMap<String, Lecture>> courseMap = new ArrayMap<>();
    private SharedPreferences sharedPreferences1;
    private String Seg = "12";
    private Context mContext;
    private MultiStateToggleButton timetableView, DaySelect;
    private ArrayList<ArrayMap<String, Lecture>> courseMap = new ArrayList<>();
    private RecyclerView myRV, legend;
    private ArrayList<Integer> colour;

    @Override
    protected ArrayList<ArrayMap<String, Lecture>> doInBackground(Context... contexts) {
        mContext = contexts[0];

        sharedPreferences1 = PreferenceManager.getDefaultSharedPreferences(contexts[0]);
        ArrayMap<String, Lecture> HM = new ArrayMap<>();
        ArrayMap<String, Lecture> HM2 = new ArrayMap<>();
        ArrayMap<String, Lecture> HM3 = new ArrayMap<>();
        courseList = getArrayList("CourseList");
        courseSegmentList = getArrayList("Segment");
        slotList = getArrayList("SlotList");
        CourseName = getArrayList("CourseName");

        colorArray.add(mContext.getResources().getColor(R.color.timetable_blue));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_brown));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_blue3));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_red));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_orange));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_blue2));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_green));
        colorArray.add(mContext.getResources().getColor(R.color.timetable_pink));

        if (courseList == null) {
            courseList = new ArrayList<>();
            courseSegmentList = new ArrayList<>();
            slotList = new ArrayList<>();
            CourseName = new ArrayList<>();

        }
        mapData();

        return courseMap;

    }

    private ArrayList<String> getArrayList(String key) {
        SharedPreferences prefs = sharedPreferences1;
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    private String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            return user.getUid();

        } else {
            return null;
        }

    }

    private void mapData() {

        int n = courseList.size();
        int def = mContext.getResources().getColor(R.color.brandedsurface);
        ArrayMap<String, Lecture> HM = new ArrayMap<>();
        ArrayMap<String, Lecture> HM2 = new ArrayMap<>();
        ArrayMap<String, Lecture> HM3 = new ArrayMap<>();
        HM.put("A", create("", "", def));
        HM.put("B", create("", "", def));
        HM.put("C", create("", "", def));
        HM.put("D", create("", "", def));
        HM.put("E", create("", "", def));
        HM.put("F", create("", "", def));
        HM.put("G", create("", "", def));
        HM.put("P", create("", "", def));
        HM.put("Q", create("", "", def));
        HM.put("R", create("", "", def));
        HM.put("S", create("", "", def));
        HM.put("W", create("", "", def));
        HM.put("X", create("", "", def));
        HM.put("Y", create("", "", def));
        HM.put("Z", create("", "", def));
        HM2.put("A", create("", "", def));
        HM2.put("B", create("", "", def));
        HM2.put("C", create("", "", def));
        HM2.put("D", create("", "", def));
        HM2.put("E", create("", "", def));
        HM2.put("F", create("", "", def));
        HM2.put("G", create("", "", def));
        HM2.put("P", create("", "", def));
        HM2.put("Q", create("", "", def));
        HM2.put("R", create("", "", def));
        HM2.put("S", create("", "", def));
        HM2.put("W", create("", "", def));
        HM2.put("X", create("", "", def));
        HM2.put("Y", create("", "", def));
        HM2.put("Z", create("", "", def));
        HM3.put("A", create("", "", def));
        HM3.put("B", create("", "", def));
        HM3.put("C", create("", "", def));
        HM3.put("D", create("", "", def));
        HM3.put("E", create("", "", def));
        HM3.put("F", create("", "", def));
        HM3.put("G", create("", "", def));
        HM3.put("P", create("", "", def));
        HM3.put("Q", create("", "", def));
        HM3.put("R", create("", "", def));
        HM3.put("S", create("", "", def));
        HM3.put("W", create("", "", def));
        HM3.put("X", create("", "", def));
        HM3.put("Y", create("", "", def));
        HM3.put("Z", create("", "", def));

        courseMap.add(HM);
        courseMap.add(HM2);
        courseMap.add(HM3);
        Random r = new Random();
        if (sharedPreferences1.getInt("seg1_begin", -1) == -1) {
            sharedPreferences1.edit().putInt("seg1_begin", r.nextInt((colorArray.size() - 1) + 1)).commit();
            sharedPreferences1.edit().putInt("seg2_begin", r.nextInt((colorArray.size() - 1) + 1)).commit();
            sharedPreferences1.edit().putInt("seg3_begin", r.nextInt((colorArray.size() - 1) + 1)).commit();
        }

        int a = sharedPreferences1.getInt("seg1_begin", 0);
        int b = sharedPreferences1.getInt("seg2_begin", 0);
        int c = sharedPreferences1.getInt("seg3_begin", 0);
        for (int i = 0; i < n; i++) {


            if (courseSegmentList.get(i).contains("12")) {
                //courseMap.put("12",new HashMap<String, String>());

                ArrayMap<String, Lecture> h1 = (courseMap.get(0));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get(0)).get(slotList.get(i)).setCourseId(courseList.get(i));

                (courseMap.get(0)).get(slotList.get(i)).setCourseColor(colorArray.get(a % colorArray.size()));
                a++;


            }
            if (courseSegmentList.get(i).contains("34")) {
                ArrayMap<String, Lecture> h1 = (courseMap.get(1));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get(1)).get(slotList.get(i)).setCourseId(courseList.get(i));
                (courseMap.get(1)).get(slotList.get(i)).setCourseColor(colorArray.get(b % colorArray.size()));
                b++;
            }
            if (courseSegmentList.get(i).contains("56")) {
                ArrayMap<String, Lecture> h1 = (courseMap.get(2));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get(2)).get(slotList.get(i)).setCourseId(courseList.get(i));
                (courseMap.get(2)).get(slotList.get(i)).setCourseColor(colorArray.get(c % colorArray.size()));
                c++;
            }
        }

    }


    private Lecture create(String id, String name, int color) {
        Lecture lecture = new Lecture();
        lecture.setCourseId(id);
        lecture.setCourse(name);
        lecture.setCourseColor(color);
        return lecture;

    }


    @Override
    protected void onPostExecute(ArrayList<ArrayMap<String, Lecture>> arrayMaps) {
        super.onPostExecute(arrayMaps);
        Timetable.courseMap = courseMap;


    }
}
