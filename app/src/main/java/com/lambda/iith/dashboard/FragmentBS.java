package com.lambda.iith.dashboard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import Adapters.SpinnerAdapter;

public class FragmentBS extends Fragment {
    Spinner spinner1, spinner2,spinner3;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bs_layout,container,false);
        spinner1 = (Spinner) view.findViewById(R.id.spinner_1);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_2);
        String[] days = {"Weekday","Weekend"};
        spinner2.setAdapter(new SpinnerAdapter(getActivity(),R.layout.custom_spinner,days)) ;


        return view;


    }
}
