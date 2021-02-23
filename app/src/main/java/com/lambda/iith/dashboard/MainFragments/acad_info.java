package com.lambda.iith.dashboard.MainFragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.material.card.MaterialCardView;
import com.lambda.iith.dashboard.R;

public class acad_info extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_acad_info, container, false);

//        ImageView img = (ImageView) view.findViewById(R.id.img);
        MaterialCardView card= view.findViewById(R.id.card);
        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent link=new Intent(Intent.ACTION_VIEW, Uri.parse("https://drive.google.com/file/d/18J6zZm9jnjkY6le8Nbk9kQU9bUXsXSVC/view?usp=sharing"));
                startActivity(link);
            }
        });

        return view;
    }
}