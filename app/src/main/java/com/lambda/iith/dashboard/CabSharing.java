package com.lambda.iith.dashboard;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;



public class CabSharing extends Fragment {
    private FloatingActionButton register , Cancel , Snooze;
    private  FloatingActionMenu floatingActionMenu;
    public int flag;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        flag = 0;
        final View view = inflater.inflate(R.layout.cab_sharing, container , false);

        floatingActionMenu = (FloatingActionMenu) view.findViewById(R.id.menu);
        register = (FloatingActionButton) view.findViewById(R.id.menu_item) ;
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext() , CabType.class));
            }
        });

        return view;

    }


}
