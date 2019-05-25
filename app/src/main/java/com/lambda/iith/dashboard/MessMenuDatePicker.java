package com.lambda.iith.dashboard;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.Calendar;

public class MessMenuDatePicker extends Fragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_mess_menu_date_picker , container , false);
        Button[]bt = new Button[7];
        bt[0] = (Button)view.findViewById(R.id.Button_Sunday);
        bt[1] = (Button)view.findViewById(R.id.Button_Monday);
        bt[2] = (Button)view.findViewById(R.id.Button_Tuesday);
        bt[3] = (Button)view.findViewById(R.id.Button_Wednesday);
        bt[4] = (Button)view.findViewById(R.id.Button_Thrusday);
        bt[5] = (Button)view.findViewById(R.id.Button_Friday);
        bt[6] = (Button)view.findViewById(R.id.Button_Saturday);


        /*final MediaPlayer sound = MediaPlayer.create(this,view.R.raw.buttonclick);
        for(int i=0;i<7;i++){
            bt[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    sound.start();


                }

            });
        }*/

        String daysArray[] = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        int textColor = Color.parseColor("#0C0B0B");
        int backgroundColor = Color.parseColor("#FFD600");
        bt[day-1].setText("Current Day : "+daysArray[day-1]);
        bt[day-1].setTextColor(textColor);
        bt[day-1].setBackgroundColor(backgroundColor);
        return view;
    }


}
