package com.lambda.iith.dashboard.MainFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.lambda.iith.dashboard.Launch;
import com.lambda.iith.dashboard.R;

public class acad_info extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_acad_info, container, false);

//        ImageView img = (ImageView) view.findViewById(R.id.img);
//        MaterialCardView card= view.findViewById(R.id.card);
//        card.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent link=new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/18J6zZm9jnjkY6le8Nbk9kQU9bUXsXSVC/view?usp=sharing"));
//                startActivity(link);
//            }
//        });

        final Button curriculum = (Button) view.findViewById(R.id.curriculum);
        ViewGroup.LayoutParams params = curriculum.getLayoutParams();
        params.width = Launch.width/3;
        curriculum.setLayoutParams(params);

        final Button handbooks = (Button) view.findViewById(R.id.handbooks);
        ViewGroup.LayoutParams params1 = handbooks.getLayoutParams();
        params1.width = Launch.width/3;
        handbooks.setLayoutParams(params1);

        final Button acad_cal = (Button) view.findViewById(R.id.acad_calender);
        ViewGroup.LayoutParams params2 = acad_cal.getLayoutParams();
        params2.width = Launch.width/3;
        acad_cal.setLayoutParams(params2);



        curriculum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent curri=new Intent(getActivity(), curriculum.class);
                startActivity(curri);
            }
        });

        acad_cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://calendar.google.com/calendar/u/0/r?cid=c_ll73em8s81nhu8nbjvggqhmbgc@group.calendar.google.com")));

            }
        });

//        handbooks.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://calendar.google.com/calendar/u/0/r?cid=c_ll73em8s81nhu8nbjvggqhmbgc@group.calendar.google.com")));
//
//            }
//        });




        return view;
    }
}