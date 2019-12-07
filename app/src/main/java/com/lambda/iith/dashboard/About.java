package com.lambda.iith.dashboard;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class About extends AppCompatActivity {
    private TextView github , vesrion;
    private ImageView imageView;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        String version = " ";
        imageView = findViewById(R.id.imageView);
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        github = findViewById(R.id.Github);
        vesrion = findViewById(R.id.Version);
        vesrion.setText("V "+ version);
        github.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/iith-dashboard"));
                startActivity(browserIntent);
            }
        });
        final Toast mToast = Toast.makeText(About.this, "4 more times for hidden options", Toast.LENGTH_SHORT);
        vesrion.setOnTouchListener(new View.OnTouchListener() {
            Handler handler = new Handler();

            int numberOfTaps = 0;
            long lastTapTimeMs = 0;
            long touchDownMs = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        touchDownMs = System.currentTimeMillis();
                        break;
                    case MotionEvent.ACTION_UP:
                        handler.removeCallbacksAndMessages(null);

                        if ((System.currentTimeMillis() - touchDownMs) > ViewConfiguration.getTapTimeout()) {
                            //it was not a tap

                            numberOfTaps = 0;
                            lastTapTimeMs = 0;
                            break;
                        }

                        if (numberOfTaps > 0
                                && (System.currentTimeMillis() - lastTapTimeMs) < ViewConfiguration.getDoubleTapTimeout()) {
                            numberOfTaps += 1;
                        } else {
                            numberOfTaps = 1;
                        }

                        lastTapTimeMs = System.currentTimeMillis();

                        if (numberOfTaps == 3) {
                            mToast.show();
                            //handle triple tap
                        } else if (numberOfTaps == 2) {
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    //handle double tap
                                    //Toast.makeText(getApplicationContext(), "double", Toast.LENGTH_SHORT).show();
                                }
                            }, ViewConfiguration.getDoubleTapTimeout());
                        } else if (numberOfTaps == 4) {

                        } else if (numberOfTaps == 5) {
                            mToast.cancel();
                            // Toast.makeText(getApplicationContext(), "2 more to go", Toast.LENGTH_SHORT).show();

                        } else if (numberOfTaps == 6) {
                            //Toast.makeText(getApplicationContext(), "1 more to go", Toast.LENGTH_SHORT).show();

                        } else if (numberOfTaps == 7) {
                            Toast.makeText(About.this, "You are there", Toast.LENGTH_SHORT).show();
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(About.this, "DEV", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                                    sharedPreferences.edit().putBoolean("DEVELOPER", true).commit();
                                    startActivity(new Intent(About.this, MainActivity.class));


                                }
                            });

                        }

                }

                return true;
            }
        });


    }


    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }
}
