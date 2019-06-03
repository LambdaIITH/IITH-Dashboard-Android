package com.lambda.iith.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.design.widget.FloatingActionButton;


public class CabSharing extends Fragment {
    private FloatingActionButton register;
    public int flag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        flag = 0;
        final View view = inflater.inflate(R.layout.cab_sharing, container , false);

        register = (FloatingActionButton) view.findViewById(R.id.cab_sharing_register);


        register.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                register.hide();
                FragmentManager fm = getFragmentManager();
                CabSharingMenu cabSharingMenu = CabSharingMenu.newInstance("Menu");

                cabSharingMenu.show(fm , "BottomMenu");


            }
        });

        return view;

    }

    @Override
    public void onResume() {
        super.onResume();
        //getFragmentManager().beginTransaction().replace(R.id.fragmentlayout , new CabSharing()).commit();
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
