package com.lambda.iith.dashboard;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import Adapters.RecyclerViewAdapter_TT;
import Model.Lecture;

public class Timetable extends Fragment {


    List<Lecture> lectureList;
    private String UUID;
    private List<String> courseList;
    private List<String> courseSegmentList;
    private List<String> slotList;
    private HashMap<String, HashMap<String, String>> courseMap;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view =inflater.inflate(R.layout.recycler_view_timetable, container, false);

        lectureList = new ArrayList<>(72);
        Lecture lec = new Lecture();
        lec.setCourse("IDP");

        lec.setCourseId("ID1025");

        for (int i = 0; i < 72; i++) {

            lectureList.add(i,lec);
        }

        RecyclerView myRV = (RecyclerView) view.findViewById(R.id.timetable_rv);
        RecyclerViewAdapter_TT myAdapter = new RecyclerViewAdapter_TT(getContext(),lectureList);
        GridLayoutManager manager = new GridLayoutManager(getContext(),8,GridLayoutManager.HORIZONTAL,false );

        myRV.setAdapter(myAdapter);
        myRV.setLayoutManager(manager);
        return view;

    }

    private void getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!= null) {
            UUID =  user.getUid();

        }else {
            UUID =  null;
        }

    }

    private void fetchData() {
        getUID();

        DocumentReference users = FirebaseFirestore.getInstance().document("users/"+UUID);
        users.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    courseList = (List<String>) documentSnapshot.get("identifiedCourses");
                    courseSegmentList = (List<String>) documentSnapshot.get("identifiedSlots");
                    slotList = (List<String>) documentSnapshot.get("identifiedSlots");
                }
            }
        });


    }

    private void mapData() {

        int n = courseList.size();

        for (int i = 0; i < n; i++) {
            if (courseSegmentList.get(i).contains("12")) {
                courseMap.put("12",new HashMap<String, String>());
                Objects.requireNonNull(courseMap.get("12")).put(slotList.get(i), courseList.get(i));
            }
            if (courseSegmentList.get(i).contains("34")) {
                courseMap.put("34",new HashMap<String, String>());
                Objects.requireNonNull(courseMap.get("34")).put(slotList.get(i), courseList.get(i));
            }
            if (courseSegmentList.get(i).contains("56")) {
                courseMap.put("56",new HashMap<String, String>());
                Objects.requireNonNull(courseMap.get("56")).put(slotList.get(i), courseList.get(i));
            }
        }

    }







}

