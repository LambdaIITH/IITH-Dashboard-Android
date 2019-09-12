package com.lambda.iith.dashboard.Timetable;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import com.lambda.iith.dashboard.MainActivity;
import com.lambda.iith.dashboard.R;

public class AddCourse extends AppCompatActivity {
    Spinner spinner;
    private String slot;
    private Button add;
    private CheckBox c1,c2,c3;
    private EditText e1,e2;
    private String segment = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        Toolbar toolbar = findViewById(R.id.toolbar);
        c1 = findViewById(R.id.checkBox3);
        c2 = findViewById(R.id.checkBox5);
        c3 = findViewById(R.id.checkBox4);
        setSupportActionBar(toolbar);
        e1 = findViewById(R.id.cname);
        e2 = findViewById(R.id.cid);
        add = findViewById(R.id.ADDCOURSE);
        spinner = findViewById(R.id.spinnerslot);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                slot = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked()){
                    segment+="12";
                }
                if(c2.isChecked()){
                    segment+="34";
                }
                if(c3.isChecked()){
                    segment+="56";
                }

                Timetable.addCourse(e1.getText().toString() , e2.getText().toString() , slot , segment);
                startActivity(new Intent(AddCourse.this , MainActivity.class));
            }
        });




    }
    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

}
