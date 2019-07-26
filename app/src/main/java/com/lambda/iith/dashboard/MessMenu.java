package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class MessMenu extends Fragment  {
    private SharedPreferences sharedPreferences;

    ImageButton im1,im2;
    private RequestQueue queue;
    ScrollView hscrollViewMain;
    TextView htext1,htext2,htext3,htext4,htext5,htext6;
    private JSONObject j1,j2,j3,j4,j5,j6,j7;
    Button b1,b2;
    private int day;
    MenuItem itemSel;
    private ToggleButton messToggle;
    private String[] daysArray = {"Sunday","Monday","Tuesday", "Wednesday","Thursday","Friday", "Saturday"};
    private TextView breakfast , lunch ,snacks ,dinner;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.mess_menu,container,false);
        //im1 =(ImageButton) rootview.findViewById(R.id.imageView1);
        im2 = (ImageButton) rootview.findViewById(R.id.imageView2);

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
messToggle = rootview.findViewById(R.id.messToggle);

breakfast = rootview.findViewById(R.id.breakfast);
lunch = rootview.findViewById(R.id.lunch);
snacks = rootview.findViewById(R.id.snacks);
dinner = rootview.findViewById(R.id.dinner);


        queue = Volley.newRequestQueue(getContext());
        parse("UDH" , MainActivity.UDH);

        messToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(messToggle.isChecked()){
                    parse("LDH" ,MainActivity.LDH );
                }
                else{ parse("UDH" , MainActivity.UDH);}


                check(itemSel);
            }
        });




        hscrollViewMain = (ScrollView)rootview.findViewById(R.id.scrollViewMain);

         htext1 = (TextView)rootview. findViewById(R.id.gg1);
         htext2 = (TextView)rootview. findViewById(R.id.lunch);
         htext3 = (TextView)rootview. findViewById(R.id.evening);
         htext4 = (TextView)rootview. findViewById(R.id.textiewxyz);
         htext5 = (TextView)rootview.findViewById(R.id.gg2);
         htext6 = (TextView)rootview.findViewById(R.id.gg4);




        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);
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
                        check(item);
                        itemSel = item;

                        return false;
                    }
                });



                popup.show();



            }
        });



        if (messToggle.isChecked()){
            parse("LDH" , "NULL");
        } else {
            parse("UDH" , "NULL");
        }

        PopupMenu popup = new PopupMenu(getActivity().getApplicationContext(), im2);

        popup.getMenuInflater().inflate(R.menu.mess_menu1,popup.getMenu());
        popup.inflate(R.menu.mess_menu1);
        itemSel = popup.getMenu().getItem(day-1);
        check(itemSel);
        return rootview;
    }



private void putData(JSONObject j2) throws JSONException {
    breakfast.setText(j2.getString("Breakfast"));
    lunch.setText(j2.getString("Lunch"));
    snacks.setText(j2.getString("Snacks"));
    dinner.setText(j2.getString("Dinner"));
}
private void check(MenuItem item){

    try {
        if (item.getItemId() == R.id.Monday) {

                htext5.setText("Monday's Menu");
                htext6.setText("Monday");
                putData(j2);



        }
        if (item.getItemId() == R.id.Tuesday) {

                htext5.setText("Tuesday's Menu");
                htext6.setText("Tuesday");
                putData(j3);

        }
        if (item.getItemId() == R.id.Wednesday) {

                htext5.setText("Wednesday's Menu");
                htext6.setText("Wednesday");
                putData(j4);

        }
        if (item.getItemId() == R.id.Thursday) {
            System.out.println("item.getItemId()");

                htext5.setText("Thursday's Menu");
                htext6.setText("Thursday");
                putData(j5);

        }
        if (item.getItemId() == R.id.Friday) {

                htext5.setText("Friday's Menu");
                htext6.setText("Friday");
                putData(j6);

        }
        if (item.getItemId() == R.id.Saturday) {

                htext5.setText("Saturday's Menu");
                htext6.setText("Saturday");
                putData(j7);


        }
        if (item.getItemId() == R.id.Sunday) {

                htext5.setText("Sunday's Menu");
                htext6.setText("Sunday");
                putData(j1);

        }

    } catch (JSONException e) {
        e.printStackTrace();
    }
}

private void parse(String string , String def){
    try{
        JSONArray JA = new JSONArray(sharedPreferences.getString(string , def));
        j1 = JA.getJSONObject(0);
        j2 = JA.getJSONObject(1);
        j3 = JA.getJSONObject(2);
        j4 = JA.getJSONObject(3);
        j5 = JA.getJSONObject(4);
        j6 = JA.getJSONObject(5);
        j7 = JA.getJSONObject(6);


    } catch (JSONException e) {
        e.printStackTrace();
    }
}


}


