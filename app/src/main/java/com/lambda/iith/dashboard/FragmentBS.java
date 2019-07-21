package com.lambda.iith.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.Spinner;
import android.widget.Toast;



public class FragmentBS extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner2;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bs_layout,container,false);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_2);
        spinner2.setOnItemSelectedListener(this);


        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getActivity(),parent.getItemAtPosition(position).toString() + " selected",Toast.LENGTH_SHORT).show();

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}