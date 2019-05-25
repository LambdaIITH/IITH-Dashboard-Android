package com.lambda.iith.dashboard;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MessMenu extends Fragment  {

    Button play,play1;
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.mess_menu,container,false);
        play =(Button)rootview.findViewById(R.id.button1);
        play1 = (Button)rootview.findViewById(R.id.button2);
        final MediaPlayer sound = MediaPlayer.create(getActivity(),R.raw.buttonclick);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                   sound.start();
                   openActivity();






            }

        });
        play1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sound.start();
                openActivity();
            }
        });




        return rootview;
    }
    public void openActivity(){
        Intent in = new Intent(getActivity(),MessMenuDatePicker.class);
        startActivity(in);

    }







}

