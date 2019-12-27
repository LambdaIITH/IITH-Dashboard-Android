package com.lambda.iith.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lambda.iith.dashboard.Timetable.NotificationInitiator;

import java.util.concurrent.TimeUnit;

public class Settings extends AppCompatActivity {


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private CheckBox cab, bus, mess, timetable;
    private RadioGroup mess_select;
    private Spinner SegDef;
    private Switch LectureNotification, courseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        courseName = findViewById(R.id.CoursenameSelect);
        mess_select = findViewById(R.id.Def);
        SegDef = findViewById(R.id.DefaultSeg);
        LectureNotification = findViewById(R.id.LectureNotificatonSwitch);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        if (sharedPreferences.getInt("MESSDEF", 1) == 0) {
            mess_select.check(R.id.MLDH);
        } else {
            mess_select.check(R.id.MUDH);
        }

        LectureNotification.setChecked(sharedPreferences.getBoolean("EnableLectureNotification", true));


        LectureNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferences.edit().putBoolean("EnableLectureNotification", true).commit();
                    sharedPreferences.edit().putBoolean("RequestAutostart", true).commit();
                    refreshNotificationProcess();

                    checkBatteryStatus();
                    //AutostartManager autostartManager = new AutostartManager(Settings.this);
                    //sharedPreferences.edit().putBoolean("RequestAutostart" , true);
                } else {
                    sharedPreferences.edit().putBoolean("EnableLectureNotification", false).commit();
                    WorkManager.getInstance().cancelAllWork();

                }

            }
        });


        courseName.setChecked(sharedPreferences.getBoolean("Cname", true));

        courseName.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (courseName.isChecked()) {
                    sharedPreferences.edit().putBoolean("Cname", true).commit();
                } else {
                    sharedPreferences.edit().putBoolean("Cname", false).commit();
                }
            }
        });

        mess_select.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {


            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.MUDH) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("MESSDEF", 1);
                    editor.commit();
                } else {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("MESSDEF", 0);
                    editor.commit();
                }
            }
        });


        edit = sharedPreferences.edit();
        timetable = findViewById(R.id.TimeTable);
        cab = findViewById(R.id.CabSharing);
        mess = findViewById(R.id.MessMenu);
        bus = findViewById(R.id.BusSchedule);

        String seg = sharedPreferences.getString("DefaultSegment", "12");
        if (seg.equals("12")) {

            SegDef.setSelection(0);


        } else if (seg.equals("34")) {

            SegDef.setSelection(1);

        } else if (seg.equals("56")) {

            SegDef.setSelection(2);

        }


        SegDef.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (SegDef.getItemAtPosition(position).equals("1-2")) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("DefaultSegment", "12");
                    edit.commit();
                } else if (SegDef.getItemAtPosition(position).equals("3-4")) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("DefaultSegment", "34");
                    edit.commit();
                } else if (SegDef.getItemAtPosition(position).equals("5-6")) {
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("DefaultSegment", "56");
                    edit.commit();
                }

                if (sharedPreferences.getBoolean("EnableLectureNotification", true)) {
                    refreshNotificationProcess();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cab.setChecked(sharedPreferences.getBoolean("cab", true));
        bus.setChecked(sharedPreferences.getBoolean("bus", true));
        mess.setChecked(sharedPreferences.getBoolean("mess", true));
        timetable.setChecked(sharedPreferences.getBoolean("timetable", true));

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
                } else {
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
                } else {
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
                } else {
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

    private void refreshNotificationProcess() {
        WorkManager.getInstance().cancelAllWorkByTag("LECTUREREMINDER");
        WorkManager.getInstance().cancelAllWorkByTag("TIMETABLE");
        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationInitiator.class, 6, TimeUnit.HOURS).addTag("TIMETABLE").build();
        WorkManager.getInstance().enqueue(periodicWorkRequest);
    }

    private void checkBatteryStatus() {
        System.out.println(getBaseContext().getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Please Disable Battery Optimization");
                alert.setMessage("Please exempt IITH Dashboard to receive uninterrupted notifications");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS), 0);
                    }
                });
                alert.show();


            }

        }
    }
}
