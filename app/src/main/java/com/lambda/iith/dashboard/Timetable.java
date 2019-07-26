package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import Adapters.RecyclerViewAdapter_TT;
import Model.Lecture;

public class Timetable extends Fragment {


    List<Lecture> lectureList;
    private String UUID;

    private HashMap<String, HashMap<String, Lecture>> courseMap = new HashMap<>();
    private HashMap<String , Lecture> Mapper;
    private RecyclerView myRV;
    public static SharedPreferences sharedPreferences;
    public static ArrayList<String> courseList;
    public static ArrayList<String> courseSegmentList;
    public static ArrayList<String> slotList;
    public static ArrayList<String> CourseName;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.recycler_view_timetable, container, false);
        myRV = (RecyclerView) view.findViewById(R.id.timetable_rv);

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



        //for (int i = 0; i < 72; i++) {

          //  lectureList.add(i,lec);
       // }   //         Lecture lecture = new Lecture();
            //lecture.setCourse("Name");
            //lecture.setCourseId("");
       mapData();
       arrgen("12");





        return view;

    }
    public ArrayList<String> getArrayList(String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
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
        HashMap <String , Lecture> HM= new HashMap<>();
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

        courseMap.put("12" ,HM);
        courseMap.put("34" ,HM);
        courseMap.put("56" ,HM);

        for (int i = 0; i < n; i++) {
            System.out.println(i);

            if (courseSegmentList.get(i).contains("12")) {
                //courseMap.put("12",new HashMap<String, String>());

                HashMap <String , Lecture> h1 = (courseMap.get("12"));
                h1.get(slotList.get(i)).setCourse(CourseName.get(i));
                (courseMap.get("12")).get(slotList.get(i)).setCourseId(courseList.get(i));

            }
            if (courseSegmentList.get(i).contains("34")) {
                //courseMap.put("34",new HashMap<String, String>());
                Objects.requireNonNull(courseMap.get("34")).put(slotList.get(i), create( courseList.get(i) , CourseName.get(i)));
            }
            if (courseSegmentList.get(i).contains("56")) {
                //courseMap.put("56",new HashMap<String, String>());
                Objects.requireNonNull(courseMap.get("56")).put(slotList.get(i), create( courseList.get(i) , CourseName.get(i)));
            }
        }

    }

    private void arrgen(String segment){
        HashMap<String , Lecture> course= courseMap.get(segment);

            lectureList.add(create("Day" , ""));
        lectureList.add(create("9-10" , ""));
        lectureList.add((create("10-11" , "")));
        lectureList.add(create("11-12" , ""));
        lectureList.add(create("12-13" , ""));
        lectureList.add(create("14:30-16" , ""));
        lectureList.add(create("16-17:30" , ""));
        lectureList.add(create("17:30-19:00" , ""));
        lectureList.add(create("19:00-20:30" , ""));

            lectureList.add(create("Monday", ""));
        lectureList.add(course.get("A") );
        lectureList.add(course.get("B") );
        lectureList.add(course.get("C") );
        lectureList.add(course.get("D") );
        lectureList.add(course.get("P") );
        lectureList.add(course.get("Q") );
        lectureList.add(course.get("W") );
        lectureList.add(course.get("X") );


            lectureList.add(create("Tuesday", ""));
        lectureList.add(course.get("D") );
        lectureList.add(course.get("E") );
        lectureList.add(course.get("F") );
        lectureList.add(course.get("G") );
        lectureList.add(course.get("R") );
        lectureList.add(course.get("S") );
        lectureList.add(course.get("Y") );
        lectureList.add(course.get("Z") );


            lectureList.add(create("Wednesday", ""));
        lectureList.add(course.get("B") );
        lectureList.add(course.get("C") );
        lectureList.add(course.get("A") );
        lectureList.add(course.get("G") );
        lectureList.add(course.get("F") );
        lectureList.add(create("" , "") );
        lectureList.add(create("" , "") );
        lectureList.add(create("" , "") );

            lectureList.add(create("Thursday", ""));
        lectureList.add(course.get("C") );
        lectureList.add(course.get("A") );
        lectureList.add(course.get("B") );
        lectureList.add(course.get("E") );
        lectureList.add(course.get("Q") );
        lectureList.add(course.get("P") );
        lectureList.add(course.get("W") );
        lectureList.add(course.get("X") );
            lectureList.add(create("Friday", ""));
        lectureList.add(course.get("E") );
        lectureList.add(course.get("F") );
        lectureList.add(course.get("D") );
        lectureList.add(course.get("G") );
        lectureList.add(course.get("S") );
        lectureList.add(course.get("R") );
        lectureList.add(course.get("Y") );
        lectureList.add(course.get("Z") );





        RecyclerViewAdapter_TT myAdapter = new RecyclerViewAdapter_TT(getContext(),lectureList);
        GridLayoutManager manager = new GridLayoutManager(getContext(),9,GridLayoutManager.HORIZONTAL,false );

        myRV.setAdapter(myAdapter);
        myRV.setLayoutManager(manager);






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
    int n = courseList.size();
    for(int i=0 ; i<n ; i++){
        if(courseList.get(i) == CourseID ){
            courseList.remove(i);
            courseSegmentList.remove(i);
            slotList.remove(i);
            CourseName.remove(i);
        }

    }

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

    @Override
    public void onResume() {
        super.onResume();
        if (sharedPreferences.getString("CourseList" , "NULL").equals("NULL"))
            Toast.makeText(getContext() ,"Use AIMS helper to load data" , Toast.LENGTH_SHORT).show();
    }
}

