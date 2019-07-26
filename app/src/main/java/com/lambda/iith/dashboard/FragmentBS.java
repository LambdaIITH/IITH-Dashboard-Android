package com.lambda.iith.dashboard;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

import Adapters.BSAdapter;


public class FragmentBS extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner2;
    private DatabaseReference ref;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private JSONObject JO , JO2;
    private RecyclerView r1,r2;
    private ToggleButton toggleButton;
    private int toggle = 0;
    private String item;
    private RequestQueue queue;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());
        View view = inflater.inflate(R.layout.bs_layout,container,false);
        spinner2 = (Spinner) view.findViewById(R.id.spinner_2);

        r1 = view.findViewById(R.id.r1);
        r2 = view.findViewById(R.id.r2);
        final AtomicInteger requestsCounter = new AtomicInteger(0);
        toggleButton = view.findViewById(R.id.messToggle);
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (toggleButton.isChecked()){
                    toggle = 1;
                }
                else {toggle=0;}

                if (item.equals("Lingampally") && toggle == 0) {
                    display("LINGAMPALLY", "IITH to Lingampally" , "Lingampally to IITH");
                }
                else if (item.equals("Lingampally") && toggle == 1) {
                    display("LINGAMPALLYW", "IITH to Lingampally" , "Lingampally to IITH");
                }

                else if (item.equals("ODF") && toggle == 0){
                    display("ODF" , "Kandi to ODF", "ODF to Kandi");
                    Toast.makeText(getContext(), "Only Sunday is Weekend for ODF buses", Toast.LENGTH_SHORT).show();

                }
                else if (item.equals("ODF") && toggle==1){
                    display("ODFS" , "Kandi to ODF", "ODF to Kandi");
                    Toast.makeText(getContext() , "Only Sunday is Weekend for ODF buses" , Toast.LENGTH_SHORT).show();
                }

                else if (item.equals("Maingate")){


                    display("LAB" , "Hostel to Maingate" , "Maingate to Hostel");
                }
                else if (item.toString().equals("Sangareddy")){


                    display("SANGAREDDY" , "IITH to Sangareddy" , "Sangareddy to IITH");
                }
            }
        });


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        try {
            JO = new JSONObject(sharedPreferences.getString("FromIITH", "{  \"LAB\": \"01:45 ,02:15 ,03:00 ,03:45 ,04:30 ,05:15 ,06:00 ,06:45 ,07:15 ,07:45 ,08:15 ,13:00 ,14:15 ,14:45 ,19:30 ,20:15,\",  \"LINGAMPALLY\": \"11:30 ,13:15 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODF\": \"09:00 ,10:30 ,12:10 ,13:10 ,14:45 ,17:45 ,18:00 ,19:00 ,20:15 ,21:00 ,22:00 ,23:00,\",  \"SANGAREDDY\": \"08:30 ,13:30 ,17:45 ,18:25 ,20:40,\",  \"LINGAMPALLYW\": \"10:30 ,12:30 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODFS\": \"08:40 ,10:15 ,12:10 ,13:15 ,14:45 ,16:10 ,17:45 ,19:10 ,20:30 ,21:10 ,22:10 ,23:30,\"}"));
            System.out.println("HELLO" + JO.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JO2 = new JSONObject(sharedPreferences.getString("ToIITH", "{\"LAB\":\"02:00 ,02:30 ,03:15 ,04:00 ,04:45 ,05:30 ,06:15 ,07:00 ,07:30 ,08:00 ,08:30 ,13:15 ,14:30 ,15:00 ,19:45 ,20:30,\",\"LINGAMPALLY\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODF\":\"08:20 , 09:30 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,19:35 ,20:30 ,21:00 ,22:00 ,23:00,\",\"SANGAREDDY\":\"08:45 ,13:45 ,18:00 ,18:40 ,22:00,\",\"LINGAMPALLYW\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODFS\":\"08:00 ,09:15 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,18:30 ,19:45 ,20:30 ,21:30 ,23:00,\"}"));
            System.out.println("HELLO2" + JO2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinner2.setOnItemSelectedListener(this);






        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


       // Toast.makeText(getActivity(),parent.getItemAtPosition(position).toString() + " selected",Toast.LENGTH_SHORT).show();
        item = parent.getItemAtPosition(position).toString();
        if (parent.getItemAtPosition(position).toString().equals("Lingampally") && toggle == 0) {
            display("LINGAMPALLY", "IITH to Lingampally" , "Lingampally to IITH");
        }
        else if (parent.getItemAtPosition(position).toString().equals("Lingampally") && toggle == 1) {
            display("LINGAMPALLYW", "IITH to Lingampally" , "Lingampally to IITH");
        }

        else if (parent.getItemAtPosition(position).toString().equals("ODF") && toggle == 0){
            display("ODF" , "Kandi to ODF", "ODF to Kandi");
            Toast.makeText(getContext() , "Only Sunday is Weekend for ODF buses" , Toast.LENGTH_SHORT).show();
        }
        else if (parent.getItemAtPosition(position).toString().equals("ODF") && toggle==1) {
            display("ODFS", "Kandi to ODF", "ODF to Kandi");
            Toast.makeText(getContext(), "Only Sunday is Weekend for ODF buses", Toast.LENGTH_SHORT).show();
        }

        else if (parent.getItemAtPosition(position).toString().equals("Maingate")){


              display("LAB" , "Hostel to Maingate" , "Maingate to Hostel");
        }
        else if (parent.getItemAtPosition(position).toString().equals("Sangareddy")){


            display("SANGAREDDY" , "IITH to Sangareddy" , "Sangareddy to IITH");
        }



    }

    private void display(String s1 , String s2 , String s3){
        ArrayList <String> mArray = new ArrayList<>();
        ArrayList <String> mArray2 = new ArrayList<>();
        mArray.add(s2);
        System.out.println("TMKC");
        try {
            String string = JO.getString(s1);
            System.out.println("FUCK" + string);
            String temp = " ";
            for(int i = 0 ; i<string.length() ; i++){

                if (string.substring(i,i+1).equals(",")){
                    mArray.add(temp);
                    temp = "";
                    continue;
                }
                temp+=string.substring(i,i+1);


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }

        mArray2.add(s3);
        System.out.println("TMKC");
        try {
            String string = JO2.getString(s1);
            System.out.println("FUCK" + string);
            String temp = "";
            for(int i = 0 ; i<string.length() ; i++){

                if (string.substring(i,i+1).equals(",")){
                    mArray2.add(temp);
                    temp = "";
                    continue;
                }
                temp+=string.substring(i,i+1);


            }



        } catch (JSONException e) {
            e.printStackTrace();
        }
        BSAdapter adapter = new BSAdapter(getContext() , mArray);
        BSAdapter adapter1 = new BSAdapter(getContext() , mArray2);

        r1.setAdapter(adapter);
        r1.setLayoutManager(new LinearLayoutManager(getContext()));
        r2.setAdapter(adapter1);
        r2.setLayoutManager(new LinearLayoutManager(getContext()));

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }




}