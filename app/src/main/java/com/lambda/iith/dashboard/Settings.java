package com.lambda.iith.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class Settings extends AppCompatActivity {

  private SharedPreferences sharedPreferences;
  private SharedPreferences.Editor edit;
  private CheckBox timetable ,cab , bus , mess;
  private RadioGroup mess_select;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mess_select = findViewById(R.id.Def);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        if (sharedPreferences.getInt("MESSDEF" , 1) == 0) {
            mess_select.check(R.id.MLDH);
        }else{mess_select.check(R.id.MUDH);}

        mess_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    if(checkedId == R.id.MUDH){
                        SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putInt("MESSDEF" , 1);
                        editor.commit();
                    }
                    else{SharedPreferences.Editor editor= sharedPreferences.edit();
                        editor.putInt("MESSDEF" , 0);
                        editor.commit();}
                }
            });


       edit =  sharedPreferences.edit();
       timetable = findViewById(R.id.TimeTable);
       cab = findViewById(R.id.CabSharing);
       mess = findViewById(R.id.MessMenu);
       bus = findViewById(R.id.BusSchedule);

           cab.setEnabled(sharedPreferences.getBoolean("Registered",false));

       cab.setChecked(sharedPreferences.getBoolean("cab" , false));
        bus.setChecked(sharedPreferences.getBoolean("bus" , true));
        mess.setChecked(sharedPreferences.getBoolean("mess" , true));
        timetable.setChecked(sharedPreferences.getBoolean("timetable" , true));

       timetable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (timetable.isChecked()) {
                   edit.putBoolean("timetable", true);
               } else {
                   edit.putBoolean("timetable", false);


               }
               edit.commit();
           }
       });


       cab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (cab.isChecked()) {
                   edit.putBoolean("cab", true);
               }else{
                   edit.putBoolean("cab", false);

               }
               edit.commit();
           }
       });

       bus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
               if (bus.isChecked()) {
                   edit.putBoolean("bus", true);
               }else{
                   edit.putBoolean("bus", false);

               }
               edit.commit();
           }
       });

        mess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mess.isChecked()) {
                    edit.putBoolean("mess", true);
                }else{
                    edit.putBoolean("mess", false);

                }
                edit.commit();
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {

        startActivity(new Intent(Settings.this, MainActivity.class));
        return true;
    }

}
