package com.lambda.iith.dashboard;

import android.app.DatePickerDialog;

import android.app.TimePickerDialog;

import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CabSharingRegister extends Fragment {
    private Button date , from, to;
    private  int mDay , mMonth , mYear, mHour , mMinute;
    private Button Book , Cancel;
    public View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.cab_sharing_register, container , false);
        date = (Button) view.findViewById(R.id.Reg_Date);
        from = (Button) view.findViewById(R.id.csr_from);
        to = (Button) view.findViewById(R.id.to);
        Cancel = (Button) view.findViewById(R.id.cs_cancel) ;
        Book = (Button) view.findViewById(R.id.cs_book);

        Calendar calendar = Calendar.getInstance();
        mDay = calendar.DAY_OF_MONTH;
        mMonth = calendar.MONTH;
        mYear = calendar.YEAR;
        mHour = calendar.HOUR_OF_DAY;
        mMinute = calendar.MINUTE;


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

        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                fm.beginTransaction().replace(R.id.fragmentlayout, new CabSharing()).commit();

            }
        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerObj(from , 0);

            }
        });




        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerObj(to, 1);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerObj(date);
            }
        });

        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();

                fm.beginTransaction().replace(R.id.fragmentlayout, new CabSharing()).commit();

            }
        });





        return view;

    }

    public static CabSharingRegister newInstance(String title) {
        CabSharingRegister frag = new CabSharingRegister();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    public void TimePickerObj(final TextView textView , final int k){
        final Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        final int minute = mcurrentTime.get(Calendar.MINUTE);
        final TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {

                textView.setText(String.format("%02d:%02d", hourOfDay+k, minutes));
            }
        }, hour, minute, false);

        timePickerDialog.show();

    }

    public void DatePickerObj(final Button textView){
        final Calendar mcurrentTime = Calendar.getInstance();
        int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        final int month = mcurrentTime.get(Calendar.MONTH);
        int year = mcurrentTime.get(Calendar.YEAR);
        final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textView.setText(String.format("%02d-%02d-%02d", dayOfMonth, month , year));
            }


        }, year, month, day);

        datePickerDialog.show();

    }



}
