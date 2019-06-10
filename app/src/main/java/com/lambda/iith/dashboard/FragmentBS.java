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

import java.util.ArrayList;

import Adapters.CustomAdapter;
import structs.BsItem;


public class FragmentBS extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner1;
    private ArrayList<BsItem> mBsItems;
    private CustomAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bs_layout,container,false);
        initList();
        spinner1 = (Spinner) view.findViewById(R.id.spinner_1);


        mAdapter = new CustomAdapter(getActivity(),mBsItems);
        spinner1.setAdapter(mAdapter);
        spinner1.setSelection(mAdapter.getCount());
        spinner1.setOnItemSelectedListener(this);




        return view;

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        BsItem clickedItem = (BsItem) parent.getItemAtPosition(position);
        String clickedString = clickedItem.getBsType();
        Toast.makeText(getActivity(),clickedString + " selected",Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void initList() {
        mBsItems = new ArrayList<>();
        mBsItems.add(new BsItem("Minibuses",R.mipmap.drop));
        mBsItems.add(new BsItem("Lingampally-IITH",R.mipmap.drop));
        mBsItems.add(new BsItem("Maingate-Hostel",R.mipmap.drop));
        mBsItems.add(new BsItem("ODF-Kandi",R.mipmap.drop));
        mBsItems.add(new BsItem("Select Type",R.mipmap.drop));

    }


}