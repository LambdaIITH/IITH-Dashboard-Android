package com.lambda.iith.dashboard.MainFragments;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.lambda.iith.dashboard.R;
import com.lambda.iith.dashboard.Timetable.AddCourse;
import com.lambda.iith.dashboard.Timetable.Timetable;

public class curriculum extends AppCompatActivity {

    private String branch;
    Spinner spinner;
    private RadioButton c1;
    private Button show;
    private String year = "";
    private String pdf_link = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum);

        c1 = findViewById(R.id.checkBox3);
        spinner = findViewById(R.id.spinnerslot);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = parent.getItemAtPosition(position).toString();
                System.out.println("jai goyal" + branch);
                if (c1.isChecked()) {
                    year = "2020";
                }

                if(!branch.equals("..") && !year.equals(""))
                {


                    pdf_link = "https://iith.dev/curriculum/" + branch + "/" + branch  +"_UG_" + year + ".pdf";

                    Intent link=new Intent(Intent.ACTION_VIEW, Uri.parse(pdf_link));
                    startActivity(link);
                }

                else
                {
                    Toast.makeText(getBaseContext(), "All fields are mandatory", Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }


}