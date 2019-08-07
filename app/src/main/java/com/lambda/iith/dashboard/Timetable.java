package com.lambda.iith.dashboard;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.model.Document;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import Adapters.RecyclerViewAdapter2_TT;
import Adapters.RecyclerViewAdapter_TT;
import Model.Lecture;

public class Timetable extends Fragment {


    List<Lecture> lectureList;
    private String UUID;
    private String Seg = "12";
    private MultiStateToggleButton timetableView;
    private HashMap<String, HashMap<String, Lecture>> courseMap = new HashMap<>();
    private HashMap<String , Lecture> Mapper;
    private RecyclerView myRV;
    public static SharedPreferences sharedPreferences;
    public static ArrayList<String> courseList;
    private int k = 1;
    public static ArrayList<String> courseSegmentList;
    public static ArrayList<String> slotList;
    public static ArrayList<String> CourseName;
    private ScrollView scrollView1 ;
    private HorizontalScrollView scrollView2;
    private Spinner segment;
    private ArrayList<Lecture> lectures1 = new ArrayList<>();
    private ArrayList<String> T1 = new ArrayList<>();
    private ArrayList<String> T2 = new ArrayList<>();

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View view =inflater.inflate(R.layout.recycler_view_timetable, container, false);
        myRV = (RecyclerView) view.findViewById(R.id.timetable_rv);
        timetableView = view.findViewById(R.id.timetableView);

        timetableView.setValue(1);

        lectureList = new ArrayList<>(72);
        Lecture lec = new Lecture();
        lec.setCourse("IDP");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        if(sharedPreferences.getBoolean("TimeTableLaunch" , true)){
            Toast.makeText(getContext() , "Longpress cards to put course name" , Toast.LENGTH_SHORT).show();
            sharedPreferences.edit().putBoolean("TimeTableLaunch" , false).commit();
        }

        lec.setCourseId("ID1025");


        courseList = getArrayList("CourseList");
        courseSegmentList = getArrayList("Segment");
        slotList = getArrayList("SlotList");
        CourseName = getArrayList("CourseName");
        if(courseList == null){
            courseList = new ArrayList<>();
            courseSegmentList = new ArrayList<>();
            slotList = new ArrayList<>();
            CourseName = new ArrayList<>();
            courseList.add(" ");
            courseSegmentList.add("12");
            slotList.add("Z");
            CourseName.add(" ");
        }
        segment = view.findViewById(R.id.segmentselect);


        //for (int i = 0; i < 72; i++) {

        //  lectureList.add(i,lec);
        // }   //         Lecture lecture = new Lecture();
        //lecture.setCourse("Name");
        //lecture.setCourseId("");

        mapData();

        String seg = sharedPreferences.getString("DefaultSegment" , "12");
        if(seg.equals("12")) {

            segment.setSelection(0);


        }
        else if(seg.equals("34")){

           segment.setSelection(1);

        }

        else if(seg.equals("56")){

            segment.setSelection(2);

        }


       segment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               if(parent.getItemAtPosition(position).toString().equals("1-2")){
                   Seg = "12";
                    }

              else if(parent.getItemAtPosition(position).toString().equals("3-4")){
                   Seg = "34";

               }
              else {
                   Seg = "56";

                }

              if (k==1){
                  arrgen(Seg);
              }
              else{
                  daily(Seg);
              }
           }


           @Override
           public void onNothingSelected(AdapterView<?> parent) {

            }
       });

        timetableView.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                k = value;
                if(value==0){

                    daily(Seg);
                }
                else{
                    arrgen(Seg);
                }
            }
        });

        return view;

    }
    public static ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = sharedPreferences;
        Gson gson = new Gson();
        String json = prefs.getString(key, null);
        Type type = new TypeToken<ArrayList<String>>() {}.getType();
        return gson.fromJson(json, type);
    }
    private String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!= null) {
            return user.getUid();

        }else {
            return null;
        }

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

    private void arrgen(String segment){
        HashMap<String , Lecture> course= courseMap.get(segment);
        lectureList.clear();

        lectureList.add(create("Day" , ""));
        lectureList.add(create("9" , ""));
        lectureList.add((create("10" , "")));
        lectureList.add(create("11" , ""));
        lectureList.add(create("12" , ""));
        lectureList.add(create("14:30" , ""));
        lectureList.add(create("16" , ""));
        lectureList.add(create("17:30" , ""));
        lectureList.add(create("19" , ""));

        lectureList.add(create("MON", ""));
        lectureList.add(course.get("A") );
        lectureList.add(course.get("B") );
        lectureList.add(course.get("C") );
        lectureList.add(course.get("D") );
        lectureList.add(course.get("P") );
        lectureList.add(course.get("Q") );
        lectureList.add(course.get("W") );
        lectureList.add(course.get("X") );


        lectureList.add(create("TUE", ""));
        lectureList.add(course.get("D") );
        lectureList.add(course.get("E") );
        lectureList.add(course.get("F") );
        lectureList.add(course.get("G") );
        lectureList.add(course.get("R") );
        lectureList.add(course.get("S") );
        lectureList.add(course.get("Y") );
        lectureList.add(course.get("Z") );


        lectureList.add(create("WED", ""));
        lectureList.add(course.get("B") );
        lectureList.add(course.get("C") );
        lectureList.add(course.get("A") );
        lectureList.add(course.get("G") );
        lectureList.add(course.get("F") );
        lectureList.add(create("" , "") );
        lectureList.add(create("" , "") );
        lectureList.add(create("" , "") );

        lectureList.add(create("THU", ""));
        lectureList.add(course.get("C") );
        lectureList.add(course.get("A") );
        lectureList.add(course.get("B") );
        lectureList.add(course.get("E") );
        lectureList.add(course.get("Q") );
        lectureList.add(course.get("P") );
        lectureList.add(course.get("W") );
        lectureList.add(course.get("X") );

        lectureList.add(create("FRI", ""));
        lectureList.add(course.get("E") );
        lectureList.add(course.get("F") );
        lectureList.add(course.get("D") );
        lectureList.add(course.get("G") );
        lectureList.add(course.get("S") );
        lectureList.add(course.get("R") );
        lectureList.add(course.get("Y") );
       lectureList.add(course.get("Z") );





        RecyclerViewAdapter_TT myAdapter = new RecyclerViewAdapter_TT(getContext(),lectureList);
        StaggeredGridLayoutManager manager = new StaggeredGridLayoutManager(9,StaggeredGridLayoutManager.HORIZONTAL);

        //manager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_NONE);
        myRV.setAdapter(myAdapter);
        myRV.setLayoutManager(manager);
        myRV.setHasFixedSize(false);






        //lectureList.add(create(course.get("A") , "Name"));
        //lectureList.add(create(course.get("A") , "Name"));
        //lectureList.add(create(course.get("A") , "Name"));
        //lectureList.add(create(course.get("A") , "Name"));






    }

    private Lecture create(String id , String name){
        Lecture lecture = new Lecture();
        lecture.setCourseId(id);
        lecture.setCourse(name);
        return lecture;
    }

    public static void edit(String CourseCode ,String Name){
        int n = courseList.size();
        saveArrayList(courseList , "CourseList");

        saveArrayList(courseSegmentList , "Segment");

        saveArrayList(slotList , "SlotList");
        saveArrayList2(CourseName , "CourseName");
        for(int i=0 ; i<n ; i++){
            if(courseList.get(i) == CourseCode ){
                CourseName.set(i ,Name );
            }

        }

        saveArrayList(courseList , "CourseList");

        saveArrayList(courseSegmentList , "Segment");

        saveArrayList(slotList , "SlotList");
        saveArrayList2(CourseName , "CourseName");



    }











    public static void addCourse(String name , String code , String slot , String segment){
        courseList.add(code);
        courseSegmentList.add(segment);
        slotList.add(slot);
        CourseName.add(name);

        saveArrayList(courseList , "CourseList");

        saveArrayList(courseSegmentList , "Segment");

        saveArrayList(slotList , "SlotList");
        saveArrayList2(CourseName , "CourseName");


    }



    public static void Delete(String CourseID){

        courseList = getArrayList("CourseList");
        courseSegmentList = getArrayList("Segment");
        slotList = getArrayList("SlotList");
        CourseName = getArrayList("CourseName");
        int n = courseList.size();
        for(int i=0 ; i<n-1 ; i++){
            System.out.println("@@" + i + n + courseList.get(i));
            if(courseList.get(i).toString().equals(CourseID) ){
                courseList.remove(i);
                courseSegmentList.remove(i);
                slotList.remove(i);
                CourseName.remove(i);
            }



        }

        saveArrayList(courseList , "CourseList");

        saveArrayList(courseSegmentList , "Segment");

        saveArrayList(slotList , "SlotList");
        saveArrayList2(CourseName , "CourseName");

    }
    public static void saveArrayList(List<String> list, String key){
        ;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }
    private static void saveArrayList2(ArrayList<String> list, String key){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
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
                dailyCreate("", segment);}


            case 1: {
                dailyCreate("", segment);


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


        for (int i = 0; i < string.length(); i++) {

            System.out.println(string.substring(i, i + 1));
            System.out.println(course.get(string.substring(i, i + 1)));


            if (!course.get(string.substring(i, i + 1)).getCourseId().equals("")) {



                lectures1.add(course.get(string.substring(i, i + 1)));
                T1.add(time.get(i).get(0));
                T2.add(time.get(i).get(1));
            }
        }
        RecyclerViewAdapter2_TT adapter = new RecyclerViewAdapter2_TT(getContext() , lectures1 , T1 , T2);
        myRV.setAdapter(adapter);
        LinearLayoutManager layout = new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false);
        myRV.setLayoutManager(layout);
        return;
    }
    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString("CourseList" , "NULL").equals("NULL"))
            Toast.makeText(getContext() ,"Use AIMS helper to load data" , Toast.LENGTH_SHORT).show();
    }
}
