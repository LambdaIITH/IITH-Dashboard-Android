package com.lambda.iith.dashboard;

import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MessMenu extends Fragment  {

    ImageButton im1,im2;
    ScrollView hscrollViewMain;
    TextView htext1,htext2,htext3,htext4,htext5,htext6;
    Button b1,b2;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.mess_menu,container,false);
        //im1 =(ImageButton) rootview.findViewById(R.id.imageView1);
        im2 = (ImageButton) rootview.findViewById(R.id.imageView2);
        final MediaPlayer sound = MediaPlayer.create(getActivity(),R.raw.buttonclick);

       hscrollViewMain = (ScrollView)rootview.findViewById(R.id.scrollViewMain);

         htext1 = (TextView)rootview. findViewById(R.id.gg1);
         htext2 = (TextView)rootview. findViewById(R.id.lunch);
         htext3 = (TextView)rootview. findViewById(R.id.evening);
         htext4 = (TextView)rootview. findViewById(R.id.dinner);
         htext5 = (TextView)rootview.findViewById(R.id.gg2);
         htext6 = (TextView)rootview.findViewById(R.id.gg4);



        final String[] daysArray = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};
        Calendar calendar = Calendar.getInstance();
        final int day = calendar.get(Calendar.DAY_OF_WEEK);
        htext6.setText(daysArray[day-1]);

        hscrollViewMain.post(new Runnable() {
             @Override
             public void run() {
     try {
    //Defining time ranges for showing particular card view at the top

    String string1 = "10:30:00";
    Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
    Calendar calendar1 = Calendar.getInstance();
    calendar1.setTime(time1);

    String string2 = "14:30:00";
    Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
    Calendar calendar2 = Calendar.getInstance();
    calendar2.setTime(time2);

    String string3 = "18:30:00";
    Date time3 = new SimpleDateFormat("HH:mm:ss").parse(string3);
    Calendar calendar3 = Calendar.getInstance();
    calendar3.setTime(time3);

    String string4 = "22:30:00";
    Date time4 = new SimpleDateFormat("HH:mm:ss").parse(string4);
    Calendar calendar4 = Calendar.getInstance();
    calendar4.setTime(time4);



    Calendar calendarn = Calendar.getInstance();
    Date d = calendarn.getTime();

    DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    String formattedDate=dateFormat.format(d);
    Date timen = new SimpleDateFormat("HH:mm:ss").parse(formattedDate);

    if (timen.after(calendar1.getTime()) && timen.before(calendar2.getTime())) {
        //checkes whether the current time is between 10:30:00 and 14:30:00.
        hscrollViewMain.smoothScrollTo(0,htext2.getTop());
    }
    if (timen.after(calendar2.getTime()) && timen.before(calendar3.getTime())) {
        //checkes whether the current time is between 10:30:00 and 14:30:00.
        hscrollViewMain.smoothScrollTo(0,htext3.getTop());
    }
    if (timen.after(calendar3.getTime()) && timen.before(calendar4.getTime())) {
        //checkes whether the current time is between 10:30:00 and 14:30:00.
        hscrollViewMain.smoothScrollTo(0,htext4.getTop());
    }
    if (timen.after(calendar4.getTime()) && timen.before(calendar1.getTime())) {
        //checkes whether the current time is between 10:30:00 and 14:30:00.
        hscrollViewMain.smoothScrollTo(0,htext1.getTop());
    }



        }catch (ParseException e){
                e.printStackTrace();
        }

             }

         });


        /*im1.setTag(1);
        im1.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View rootview) {

                    if(htext6.getText().equals("(Currently in UDH)" )){
                        htext6.setText("(Currently in LDH)");
                        im1.setTag(0);
                    }

            else{
                 htext6.setText("(Currently in UDH)");
                 im1.setTag(1);
             }



         }
        });*/
        im2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View rootview) {
                final String[] daysArray = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};
                Calendar calendar = Calendar.getInstance();
                final int day = calendar.get(Calendar.DAY_OF_WEEK);
                Spannable span = new SpannableString(daysArray[day-1]);
                span.setSpan(new ForegroundColorSpan(Color.RED), 0, span.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), im2);
                //popup.getMenuInflater().inflate(R.menu.mess_menu1,popup.getMenu());
                popup.inflate(R.menu.mess_menu1);
                MenuItem item = popup.getMenu().getItem(day-1);
                item.setTitle(span);

               popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getItemId()== R.id.Monday){
                          if(daysArray[day-1] != "Monday"){
                              htext5.setText("Monday's Menu");
                              htext6.setText("Monday");
                          }
                          else {
                              htext5.setText("Today's Menu");
                              htext6.setText(daysArray[day-1]);
                          }
                        }
                        if (item.getItemId()== R.id.Tuesday){
                            if(daysArray[day-1] != "Tuesday"){
                                htext5.setText("Tuesday's Menu");
                                htext6.setText("Tuesday");
                            }
                            else {
                                htext5.setText("Today's Menu");
                                htext6.setText(daysArray[day-1]);
                            }
                        }
                        if (item.getItemId()== R.id.Wednesday){
                            if(daysArray[day-1] != "Wednesday"){
                                htext5.setText("Wednesday's Menu");
                                htext6.setText("Wednesday");
                            }
                            else {
                                htext5.setText("Today's Menu");
                                htext6.setText(daysArray[day-1]);
                            }
                        }
                        if (item.getItemId()== R.id.Thrusday){
                            if(daysArray[day-1] != "Thrusday"){
                                htext5.setText("Thrusday's Menu");
                                htext6.setText("Thrusday");
                            }
                            else {
                                htext5.setText("Today's Menu");
                                htext6.setText(daysArray[day-1]);
                            }
                        }
                        if (item.getItemId()== R.id.Friday){
                            if(daysArray[day-1] != "Friday"){
                                htext5.setText("Friday's Menu");
                                htext6.setText("Friday");
                            }
                            else {
                                htext5.setText("Today's Menu");
                                htext6.setText(daysArray[day-1]);
                            }
                        }
                        if (item.getItemId()== R.id.Saturday){
                            if(daysArray[day-1] != "Saturday"){
                                htext5.setText("Saturday's Menu");
                                htext6.setText("Saturday");
                            }
                            else {
                                htext5.setText("Today's Menu");
                                htext6.setText(daysArray[day-1]);
                            }
                        }
                        if (item.getItemId()== R.id.Sunday){
                            if(daysArray[day-1] != "Sunday"){
                                htext5.setText("Sunday's Menu");
                                htext6.setText("Sunday");
                            }
                            else {
                                htext5.setText("Today's Menu");
                                htext6.setText(daysArray[day-1]);
                            }
                        }

                        return false;
                    }
                });

                popup.show();



            }
        });





        return rootview;
    }








}


