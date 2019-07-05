package com.lambda.iith.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.os.Bundle;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.TimePicker;


import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CabSharingRegister extends AppCompatActivity {
    private Button date , from, to;

    private Button Book ;
    public View view;
    private ImageButton img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cab_sharing_register);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        img = (ImageButton) findViewById(R.id.imageView3);


        date = (Button) findViewById(R.id.Reg_Date);

        from = (Button) findViewById(R.id.csr_from);
        to = (Button) findViewById(R.id.to);

        Book = (Button) findViewById(R.id.cs_book);

        Calendar calendar = Calendar.getInstance();



        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(calendar.getTime());
        date.setText(formattedDate);

        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
        String formattedDate2 = df2.format(calendar.getTime());
        from.setText(formattedDate2);
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        calendar.add(Calendar.HOUR_OF_DAY , 1);
        String formattedDate3 = df3.format(calendar.getTime());

        to.setText(formattedDate3);


        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });



        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getSupportFragmentManager();



            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerObj(from ,to, 0);




            }
        });




        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerObj( to,to, 1);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerObj(date);
            }
        });


    }

    public void TimePickerObj(final TextView textView , final TextView textView2 ,  final int k){
        final Calendar mcurrentTime = Calendar.getInstance();
        String Date = textView.getText().toString();
        int hour = Character.getNumericValue(Date.charAt(0))*10 +  Character.getNumericValue(Date.charAt(1));

        int minute = Character.getNumericValue(Date.charAt(3))*10 + Character.getNumericValue(Date.charAt(4));


        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if(k==0){
                textView.setText(String.format("%02d:%02d", hourOfDay, minutes));
                textView2.setText(String.format("%02d:%02d", hourOfDay+1, minutes));}

                else if (k==1){
                    textView.setText(String.format("%02d:%02d", hourOfDay, minutes));
                }
            }
        }, hour, minute, false);

        timePickerDialog.show();

    }

    public void DatePickerObj(final Button textView){
        final Calendar mcurrentTime = Calendar.getInstance();
        //int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        //final int month = mcurrentTime.get(Calendar.MONTH);
        //int year = mcurrentTime.get(Calendar.YEAR);

        String Date = textView.getText().toString();
        int day = Character.getNumericValue(Date.charAt(0))*10 +  Character.getNumericValue(Date.charAt(1));

        int year = Character.getNumericValue(Date.charAt(6))*1000 + Character.getNumericValue(Date.charAt(7))*100+ Character.getNumericValue(Date.charAt(8))*10 + Character.getNumericValue(Date.charAt(9));
        int month = Character.getNumericValue(Date.charAt(3))*10 + Character.getNumericValue(Date.charAt(4));



        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                textView.setText(String.format("%02d-%02d-%02d", dayOfMonth, month , year));
            }


        }, year, month, day);

        datePickerDialog.show();

    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
