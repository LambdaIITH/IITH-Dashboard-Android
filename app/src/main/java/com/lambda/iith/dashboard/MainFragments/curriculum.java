package com.lambda.iith.dashboard.MainFragments;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.lambda.iith.dashboard.R;

public class curriculum extends AppCompatActivity {

    private String branch;
    Spinner spinner;
    private RadioGroup c1;
    private Button show;
    private String year = "";
    private String pdf_link = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curriculum);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        c1 = findViewById(R.id.checkBox3);
        show = findViewById(R.id.curr_fetch);
        spinner = findViewById(R.id.spinnerslot);


        c1.clearCheck();

        show.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick (View v){
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
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                branch = parent.getItemAtPosition(position).toString();







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


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radio1:
                if (checked)
                    year = "2020";
                    break;
            case R.id.radio2:
                if (checked)
                    year = "2019";
                    break;

            case R.id.radio3:
                if (checked)
                    year = "2018";
                break;
            case R.id.radio4:
                if (checked)
                    year = "2017";
                break;

        }


    }
}