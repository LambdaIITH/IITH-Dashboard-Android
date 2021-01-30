package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Developer extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_developer);
        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        textView = findViewById(R.id.WorkTime);
        textView.setText(sharedPreferences.getString("TimeWork", "NULL"));
        findViewById(R.id.HistoryDelete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPreferences.edit().putString("TimeWork", "").commit();
                textView.setText(sharedPreferences.getString("TimeWork", "NULL"));


            }
        });
    }
}
