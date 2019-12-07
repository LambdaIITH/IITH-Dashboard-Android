package com.lambda.iith.dashboard.MainFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;
import com.lambda.iith.dashboard.Init;
import com.lambda.iith.dashboard.Launch;
import com.lambda.iith.dashboard.R;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import Adapters.BSAdapter;


public class FragmentBS extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner2;
    private DatabaseReference ref;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private JSONObject JO, JO2;
    private RecyclerView r1, r2;
    private int toggle = 0;
    private CardView Bus1, Bus2;
    private TextView Head1, Head2;
    private String item;
    private RequestQueue queue;
    private MultiStateToggleButton bustoggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());
        View view = inflater.inflate(R.layout.bs_layout, container, false);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        spinner2 = view.findViewById(R.id.spinner_2);
        r1 = view.findViewById(R.id.r1);
        r2 = view.findViewById(R.id.r2);
        Bus1 = view.findViewById(R.id.card1);
        Bus2 = view.findViewById(R.id.Card2);
        Head1 = view.findViewById(R.id.List1Head);
        Head2 = view.findViewById(R.id.List2Head);
        final float scale = getResources().getDisplayMetrics().density;


        double width2 = convertPxToDp(getContext(), Launch.width / 2);

        ViewGroup.LayoutParams params = Bus1.getLayoutParams();
        params.width = (int) ((width2 - 15) * scale + 0.5f);
        Bus1.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = Bus2.getLayoutParams();
        params1.width = (int) ((width2 - 15) * scale + 0.5f);
        Bus2.setLayoutParams(params1);
        int Day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        bustoggle = view.findViewById(R.id.BusToggle);
        spinner2.setOnItemSelectedListener(this);
        item = spinner2.getItemAtPosition(0).toString();
        bustoggle.setOnValueChangedListener(new org.honorato.multistatetogglebutton.ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                toggle = value;
                if (item.equals("Lingampally") && toggle == 0) {
                    display("LINGAMPALLY", "IITH to Lingampally", "Lingampally to IITH");
                } else if (item.equals("Lingampally") && toggle == 1) {
                    display("LINGAMPALLYW", "IITH to Lingampally", "Lingampally to IITH");
                } else if (item.equals("ODF") && toggle == 0) {
                    display("ODF", "Kandi to ODF", "ODF to Kandi");
                    // Toast.makeText(getContext(), "Only Sunday is Weekend for ODF buses", Toast.LENGTH_SHORT).show();

                } else if (item.equals("ODF") && toggle == 1) {
                    display("ODFS", "Kandi to ODF", "ODF to Kandi");
                    //Toast.makeText(getContext() , "Only Sunday is Weekend for ODF buses" , Toast.LENGTH_SHORT).show();
                } else if (item.equals("Maingate")) {


                    display("LAB", "Hostel to Maingate", "Maingate to Hostel");
                } else if (item.equals("Sangareddy")) {


                    display("SANGAREDDY", "IITH to Sangareddy", "Sangareddy to IITH");
                }
            }
        });


        Init init = new Init();
        try {
            JO = new JSONObject(sharedPreferences.getString("FromIITH", Init.DEF2));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JO2 = new JSONObject(sharedPreferences.getString("ToIITH", Init.DEF1));

        } catch (JSONException e) {
            e.printStackTrace();
        }


        if (Day == 7 || Day == 1) {
            bustoggle.setValue(1);
        } else {
            bustoggle.setValue(0);
        }

        return view;
    }

    public double convertPxToDp(Context context, double px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        // Toast.makeText(getActivity(),parent.getItemAtPosition(position).toString() + " selected",Toast.LENGTH_SHORT).show();
        item = parent.getItemAtPosition(position).toString();
        if (parent.getItemAtPosition(position).toString().equals("Lingampally") && toggle == 0) {
            display("LINGAMPALLY", "IITH to Lingampally", "Lingampally to IITH");
        } else if (parent.getItemAtPosition(position).toString().equals("Lingampally") && toggle == 1) {
            display("LINGAMPALLYW", "IITH to Lingampally", "Lingampally to IITH");
        } else if (parent.getItemAtPosition(position).toString().equals("ODF") && toggle == 0) {
            display("ODF", "Kandi to ODF", "ODF to Kandi");
        } else if (parent.getItemAtPosition(position).toString().equals("ODF") && toggle == 1) {
            display("ODFS", "Kandi to ODF", "ODF to Kandi");
        } else if (parent.getItemAtPosition(position).toString().equals("Maingate")) {
            display("LAB", "Hostel to Maingate", "Maingate to Hostel");
        } else if (parent.getItemAtPosition(position).toString().equals("Sangareddy")) {
            display("SANGAREDDY", "IITH to Sangareddy", "Sangareddy to IITH");
        }


    }

    private void display(String s1, String s2, String s3) {
        ArrayList<String> mArray = new ArrayList<>();
        ArrayList<String> mArray2 = new ArrayList<>();
        Head1.setText(s2);

        try {
            JSONArray BusTimes = JO.getJSONArray(s1);

            for (int i = 0; i < BusTimes.length(); i++) {
                if (BusTimes.getString(i).length() == 4) {
                    mArray.add("0" + BusTimes.getString(i));
                } else {
                    mArray.add(BusTimes.getString(i));
                }


            }
            Collections.sort(mArray);


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Head2.setText(s3);


        try {
            JSONArray BusTimes = JO2.getJSONArray(s1);
            for (int i = 0; i < BusTimes.length(); i++) {
                if (BusTimes.getString(i).length() == 4) {
                    mArray2.add("0" + BusTimes.getString(i));
                } else {
                    mArray2.add(BusTimes.getString(i));
                }


            }
            Collections.sort(mArray2);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        BSAdapter adapter = new BSAdapter(getContext(), mArray);
        BSAdapter adapter1 = new BSAdapter(getContext(), mArray2);

        r1.setAdapter(adapter);
        r1.setLayoutManager(new LinearLayoutManager(getContext()));
        r2.setAdapter(adapter1);
        r2.setLayoutManager(new LinearLayoutManager(getContext()));

    }


    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


}