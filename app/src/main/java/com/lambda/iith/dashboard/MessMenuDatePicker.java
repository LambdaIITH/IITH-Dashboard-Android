package com.lambda.iith.dashboard;

import android.graphics.Color;
import android.media.MediaPlayer;

import android.os.Bundle;

import android.support.annotation.Nullable;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.view.View;

import android.widget.Button;

import java.util.Calendar;

public class MessMenuDatePicker extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mess_menu_date);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button[]bt = new Button[7];
        bt[0] = (Button)findViewById(R.id.Button_Sunday);
        bt[1] = (Button)findViewById(R.id.Button_Monday);
        bt[2] = (Button)findViewById(R.id.Button_Tuesday);
        bt[3] = (Button)findViewById(R.id.Button_Wednesday);
        bt[4] = (Button)findViewById(R.id.Button_Thrusday);
        bt[5] = (Button)findViewById(R.id.Button_Friday);
        bt[6] = (Button)findViewById(R.id.Button_Saturday);


        final MediaPlayer sound = MediaPlayer.create(this,R.raw.buttonclick);
        for(int i=0;i<7;i++){
            bt[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sound.start();


                }

            });
        }

        String daysArray[] = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int textColor = Color.parseColor("#0C0B0B");
        int backgroundColor = Color.parseColor("#03DAC6");
        bt[day-1].setText("Current Day : "+daysArray[day-1]);
        bt[day-1].setTextColor(textColor);
        bt[day-1].setBackgroundColor(backgroundColor);

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }



}
