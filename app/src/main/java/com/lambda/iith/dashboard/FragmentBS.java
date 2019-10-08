package com.lambda.iith.dashboard;

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
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import Adapters.BSAdapter;


public class FragmentBS extends Fragment implements AdapterView.OnItemSelectedListener {
    Spinner spinner2;
    private DatabaseReference ref;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor edit;
    private JSONObject JO, JO2;
    private RecyclerView r1, r2;
    private int toggle = 0;
    private CardView Bus1 , Bus2;
    private TextView Head1, Head2;
    private String item;
    private RequestQueue queue;
    private MultiStateToggleButton bustoggle;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        queue = Volley.newRequestQueue(getContext());
        View view = inflater.inflate(R.layout.bs_layout, container, false);
        spinner2 = view.findViewById(R.id.spinner_2);

        r1 = view.findViewById(R.id.r1);
        r2 = view.findViewById(R.id.r2);
        Bus1 = view.findViewById(R.id.card1);
        Bus2 = view.findViewById(R.id.Card2);
        Head1 = view.findViewById(R.id.List1Head);
        Head2 = view.findViewById(R.id.List2Head);
        final float scale = getResources().getDisplayMetrics().density;


        double width2 = convertPxToDp(getContext(),Launch.width/2);

        ViewGroup.LayoutParams params = Bus1.getLayoutParams();
        params.width = (int) ((width2-15) * scale + 0.5f);
        Bus1.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = Bus2.getLayoutParams();
        params1.width = (int) ((width2-15) * scale + 0.5f);
        Bus2.setLayoutParams(params1);
        int Day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);

        bustoggle = view.findViewById(R.id.BusToggle);
        if (Day == 7 || Day == 1) {
            bustoggle.setValue(1);
        } else {
            bustoggle.setValue(0);
        }

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


        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());

        try {
            JO = new JSONObject(sharedPreferences.getString("FromIITH", "{    \"LAB\": \"00:00 ,00:15 ,00:45 ,01:15 ,01:45 ,02:15 ,03:15 ,04:15 ,05:15 ,06:15 ,07:15 ,07:45 ,08:15 ,08:45 ,09:00 ,09:15 ,09:30 ,09:45 ,10:00 ,10:15 ,10:30 ,10:45 ,11:00 ,11:15 ,11:30 ,11:45 ,12:00 ,12:15 ,12:30 ,12:45 ,13:00 ,13:15 ,13:30 ,13:45 ,14:00 ,14:15 ,14:30 ,14:45 ,15:00 ,15:15 ,15:30 ,15:45 ,16:00 ,16:15 ,16:30 ,16:45 ,17:00 ,17:15 ,17:30 ,17:45 ,18:00 ,18:15 ,18:30 ,18:45 ,19:00 ,19:15 ,19:30 ,19:45 ,20:00 ,20:15 ,20:30 ,20:45 ,21:00 ,21:15 ,21:30 ,21:45 ,22:00 ,22:15 ,22:30 ,22:45 ,23:00 ,23:15 ,23:30 ,23:45 ,\",    \"LINGAMPALLY\": \"09:45 ,14:30 ,16:30 ,17:45 ,19:15 ,21:30 ,\",    \"ODF\": \"09:00 ,10:30 ,12:30 ,14:45 ,17:45 ,19:00 ,20:00 ,21:00 ,22:15 ,\",    \"SANGAREDDY\": \"08:00 ,08:30 ,17:45 ,18:25 ,20:40,\",    \"LINGAMPALLYW\": \"09:45 ,12:30 ,14:30 ,17:45 ,19:15 ,21:30,\",    \"ODFS\": \"09:00 ,10:30 ,12:30 ,14:45 ,17:45 ,19:00 ,20:00 ,21:00 ,22:15,\"  }"));
            System.out.println("HELLO" + JO.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            JO2 = new JSONObject(sharedPreferences.getString("ToIITH", "{    \"LAB\": \"00:00 ,0:30 ,01:00 ,01:30 ,02:00 ,02:30 ,03:30 ,04:30 ,05:30 ,06:30 ,07:30 ,08:00 ,08:30 ,09:00 ,09:15 ,09:30 ,09:45 ,10:00 ,10:15 ,10:30 ,10:45 ,11:00 ,11:15 ,11:30 ,11:45 ,12:00 ,12:15 ,12:30 ,12:45 ,13:00 ,13:15 ,13:30 ,13:45 ,14:00 ,14:15 ,14:30 ,14:45 ,15:00 ,15:15 ,15:30 ,15:45 ,16:00 ,16:15 ,16:30 ,16:45 ,17:00 ,17:15 ,17:30 ,17:45 ,18:00 ,18:15 ,18:30 ,18:45 ,19:00 ,19:15 ,19:30 ,19:45 ,20:00 ,20:15 ,20:30 ,20:45 ,21:00 ,21:15 ,21:30 ,21:45 ,22:00 ,22:15 ,22:30 ,22:45 ,23:00 ,23:15 ,23:30 ,23:45 ,\",    \"LINGAMPALLY\": \"09:15 ,11:00 ,16:30 ,18:00 ,20:15 ,22:30,\",    \"ODF\": \"08:20 , 09:30 ,12:00 ,13:30 ,16:30 ,18:30 ,19:35 ,20:30 ,21:45,\",    \"SANGAREDDY\": \"08:15 ,08:45 ,18:00 ,18:40 ,21:00,\",    \"LINGAMPALLYW\": \"09:30 ,11:30 ,16:30 ,18:00 ,20:15 ,22:30,\",    \"ODFS\": \"08:20 , 09:30 ,12:00 ,13:30 ,16:30 ,18:30 ,19:35 ,20:30 ,21:45,\"  }"));
            System.out.println("HELLO2" + JO2.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        spinner2.setOnItemSelectedListener(this);


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
            //Toast.makeText(getContext() , "Only Sunday is Weekend for ODF buses" , Toast.LENGTH_SHORT).show();
        } else if (parent.getItemAtPosition(position).toString().equals("ODF") && toggle == 1) {
            display("ODFS", "Kandi to ODF", "ODF to Kandi");
            //Toast.makeText(getContext(), "Only Sunday is Weekend for ODF buses", Toast.LENGTH_SHORT).show();
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
        System.out.println("TMKC");
        try {
            String string = JO.getString(s1);
            System.out.println("FUCK" + string);
            String temp = " ";
            for (int i = 0; i < string.length(); i++) {

                if (string.substring(i, i + 1).equals(",")) {
                    mArray.add(temp);
                    temp = "";
                    continue;
                }
                temp += string.substring(i, i + 1);


            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        Head2.setText(s3);
        System.out.println("TMKC");
        try {
            String string = JO2.getString(s1);
            System.out.println("FUCK" + string);
            String temp = "";
            for (int i = 0; i < string.length(); i++) {

                if (string.substring(i, i + 1).equals(",")) {
                    mArray2.add(temp);
                    temp = "";
                    continue;
                }
                temp += string.substring(i, i + 1);


            }


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