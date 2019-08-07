package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
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

    private RequestQueue queue;
    NestedScrollView hscrollViewMain;
    TextView htext1,htext2,htext3,htext4,htext6;
    private JSONObject j1,j2,j3,j4,j5,j6,j7;

    private int day;

    private Spinner MessDay;
    private MultiStateToggleButton messToggle;

    private TextView breakfast , lunch ,snacks ,dinner;
    @RequiresApi(api = Build.VERSION_CODES.O)
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.mess_menu,container,false);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
    messToggle = rootview.findViewById(R.id.MessToggle);
    messToggle.setValue(sharedPreferences.getInt("MESSDEF" , 1));
    breakfast = rootview.findViewById(R.id.breakfast);
    lunch = rootview.findViewById(R.id.lunch);
    snacks = rootview.findViewById(R.id.snacks);
    dinner = rootview.findViewById(R.id.dinner);


        queue = Volley.newRequestQueue(getContext());


        MessDay = rootview.findViewById(R.id.MessDaySelect);


        if (messToggle.getValue() == 0){
            parse("LDH" , MainActivity.LDH);
        } else {
            parse("UDH" , MainActivity.UDH);
        }

        messToggle.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                if(value==0){
                    parse("LDH" ,MainActivity.LDH );
                }
                else{ parse("UDH" , MainActivity.UDH);}

                MessDay.setSelection(MessDay.getSelectedItemPosition());


            }
        });





        hscrollViewMain = rootview.findViewById(R.id.scrollViewMain);

         htext1 = (TextView)rootview. findViewById(R.id.gg1);
         htext2 = (TextView)rootview. findViewById(R.id.lunch);
         htext3 = (TextView)rootview. findViewById(R.id.evening);
         htext4 = (TextView)rootview. findViewById(R.id.textiewxyz);


        MessDay.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    if (position == 0) {

                        putData(j2);
                    }
                    if (position == 1) {

                        putData(j3);
                    }
                    if (position == 2) {

                        putData(j4);
                    }
                    if (position == 3) {

                        putData(j5);
                    }
                    if (position == 4) {

                        putData(j6);
                    }
                    if (position == 5) {

                        putData(j7);
                    }
                    if (position == 6) {

                        putData(j1);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        Calendar calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_WEEK);


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




    int day = calendar.get(Calendar.DAY_OF_WEEK);
    MessDay.setSelection((day-2)%7);





        return rootview;
    }



private void putData(JSONObject j2) throws JSONException {
    breakfast.setText(j2.getString("Breakfast"));
    lunch.setText(j2.getString("Lunch"));
    snacks.setText(j2.getString("Snacks"));
    dinner.setText(j2.getString("Dinner"));
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


