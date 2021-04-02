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
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.lambda.iith.dashboard.Timetable.NotificationInitiator;

import java.util.concurrent.TimeUnit;

public class Settings extends AppCompatActivity {

    private TextView H1;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private CheckBox cab, bus, mess, timetable;
    private RadioGroup mess_select;
    private Spinner SegDef, LectureTime;
    private Switch LectureNotification, courseName , LectureNotificationType, acadReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        H1 = findViewById(R.id.textView8);

        courseName = findViewById(R.id.CoursenameSelect);
        mess_select = findViewById(R.id.Def);
        SegDef = findViewById(R.id.DefaultSeg);
        LectureTime = findViewById(R.id.ReminderTime);
        LectureNotification = findViewById(R.id.LectureNotificatonSwitch);
        LectureNotificationType = findViewById(R.id.lectureNotificatonTypeSwitch);
        acadReminder = findViewById(R.id.AcadReminderSwitch);


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);
        if (sharedPreferences.getInt("MESSDEF", 1) == 0) {
            mess_select.check(R.id.MLDH);
        } else {
            mess_select.check(R.id.MUDH);
        }

        LectureNotification.setChecked(sharedPreferences.getBoolean("EnableLectureNotification", true));
        H1.setEnabled(sharedPreferences.getBoolean("EnableLectureNotification", true));
        LectureNotificationType.setEnabled(sharedPreferences.getBoolean("EnableLectureNotification", true));
        LectureNotificationType.setChecked(sharedPreferences.getBoolean("LectureNotificationRing", false));
        LectureTime.setEnabled(sharedPreferences.getBoolean("EnableLectureNotification", true));
        //LectureTime.setClickable(sharedPreferences.getBoolean("EnableLectureNotification", true));
        LectureNotificationType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                sharedPreferences.edit().putBoolean("LectureNotificationRing" , isChecked).apply();
                refreshNotificationProcess();
            }
        });

        LectureNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                LectureNotification.setChecked(isChecked);
                H1.setEnabled(isChecked);
                LectureTime.setEnabled(isChecked);
                if (isChecked) {
                    sharedPreferences.edit().putBoolean("EnableLectureNotification", true).apply();
                    sharedPreferences.edit().putBoolean("RequestAutostart", true).apply();
                    refreshNotificationProcess();


                    checkBatteryStatus();
                    //AutostartManager autostartManager = new AutostartManager(Settings.this);
                    //sharedPreferences.edit().putBoolean("RequestAutostart" , true);
                } else {
                    sharedPreferences.edit().putBoolean("EnableLectureNotification", false).apply();
                    WorkManager.getInstance().cancelAllWorkByTag("LECTUREREMINDER");
                    WorkManager.getInstance().cancelAllWorkByTag("TIMETABLE");

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

        acadReminder.setChecked(sharedPreferences.getBoolean("EnableAcadNotification",true));
        acadReminder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (acadReminder.isChecked()) {
                    sharedPreferences.edit().putBoolean("EnableAcadNotification", true).commit();
                    refreshNotificationProcess();
                    checkBatteryStatus();
                } else {
                    sharedPreferences.edit().putBoolean("EnableAcadNotification", false).commit();
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

        int interval = sharedPreferences.getInt("NotificationTime", 30);

        if(interval==5){
            LectureTime.setSelection(0);
        }
        else if(interval==10){
            LectureTime.setSelection(1);
        }
        else if(interval==15){
            LectureTime.setSelection(2);
        }
        else if(interval==30){
            LectureTime.setSelection(3);
        }
        else if(interval==45){
            LectureTime.setSelection(4);
        }
        else if(interval==60){
            LectureTime.setSelection(5);
        }


        LectureTime.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                    if(position==0){
                        sharedPreferences.edit().putInt("NotificationTime" , 5).commit();}

                    if(position==1) {
                        sharedPreferences.edit().putInt("NotificationTime", 10).commit();
                    }
                    if(position==2) {
                        sharedPreferences.edit().putInt("NotificationTime", 15).commit();

                    }if(position==3){
                        sharedPreferences.edit().putInt("NotificationTime" , 30).commit();
                    }if(position==4){
                        sharedPreferences.edit().putInt("NotificationTime" , 45).commit();
                    }if(position==5){
                        sharedPreferences.edit().putInt("NotificationTime" , 60).commit();
                    }
                if (sharedPreferences.getBoolean("EnableLectureNotification", true)) {
                    refreshNotificationProcess();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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
                edit.putBoolean("timetable", timetable.isChecked());
                edit.commit();
            }
        });


        cab.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("cab", cab.isChecked());
                edit.commit();
            }
        });

        bus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("bus", bus.isChecked());
                edit.commit();
            }
        });

        mess.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                edit.putBoolean("mess", mess.isChecked());
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
        WorkManager.getInstance().cancelAllWorkByTag("ACADEVENTS");
        //SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(Settings.this);

        PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationInitiator.class, 6, TimeUnit.HOURS).addTag("TIMETABLE").build();
        if(sharedPreferences.getBoolean("EnableLectureNotification", true))
            WorkManager.getInstance().enqueue(periodicWorkRequest);

        PeriodicWorkRequest acadRequest = new PeriodicWorkRequest.Builder(com.lambda.iith.dashboard.AcadNotifs.NotificationInitiator.class, 1,TimeUnit.DAYS)
                .addTag("ACADEVENTS")
                .build();

        if(sharedPreferences.getBoolean("EnableAcadNotification", true))
            WorkManager.getInstance().enqueue(acadRequest);
    }

    private void checkBatteryStatus() {
        //System.out.println(getBaseContext().getPackageName());
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
