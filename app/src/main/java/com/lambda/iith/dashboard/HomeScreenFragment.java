package com.lambda.iith.dashboard;

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
import android.widget.Space;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Adapters.RecyclerViewAdapter_CSHOME;

public class HomeScreenFragment extends Fragment {

    //Fragment to be displayed on Home screen which will have 3 cards as layout.
    private CardView bus , mess ,timetable ,cab ;
    private Space space1;
    private RecyclerView CabSharing;
    private ArrayList <String> mNames = new ArrayList<>();
    private ArrayList <String> mEmails = new ArrayList<>();
    private View view;
    private String email;
    private SharedPreferences sharedPref;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_screen , container , false);
        mess = (CardView) view.findViewById(R.id.MessCard);
        cab = view.findViewById(R.id.CabCard);
        timetable = view.findViewById(R.id.TimetableCard);
        bus = view.findViewById(R.id.BusCard);
        mEmails = com.lambda.iith.dashboard.CabSharing.mEmails;
        mNames = com.lambda.iith.dashboard.CabSharing.mNames;
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        System.out.println(sharedPref.getBoolean("cab", false));
        cabCardMake(sharedPref.getBoolean("cab" , false));
        busmake(sharedPref.getBoolean("bus" , true));
        messmake(sharedPref.getBoolean("mess" , true));
        timetablemake(sharedPref.getBoolean("timetable" , true));


        //cardView2.setVisibility(View.GONE);
        //space1.setVisibility(View.GONE);
        return view;
    }
    private void busmake(boolean b){
        if(b){bus.setVisibility(View.VISIBLE);}
    }

    private void messmake(boolean b){
        if(b){mess.setVisibility(View.VISIBLE);}
    }
    private void timetablemake(boolean b){
        if(b){timetable.setVisibility(View.VISIBLE);}
    }



    private void cabCardMake(boolean b){
        if(b){
            cab.setVisibility(View.VISIBLE);
            final String startTime = sharedPref.getString("startTime","    NA      NA  " );
            final String endTime = sharedPref.getString("endTime","    NA      NA  " );
            final int CabID = sharedPref.getInt("Route",100 );
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://13.233.90.143/query";
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // Name, email address, and profile photo Url

                email = user.getEmail().toString();
            }
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the first 500 characters of the response string.
                            JSONArray JA = null;

                            try {
                                JA = new JSONArray(response);
                                mNames.clear();
                                mEmails.clear();
                                for (int i = 0; i < JA.length(); i++) {
                                    JSONObject JO = (JSONObject) JA.get(i);


                                    SimpleDateFormat format1 = new SimpleDateFormat("YYYY-mm-dd:HH:MM");
                                    java.util.Date T1 = format1.parse(startTime.substring(0,10) +":" + startTime.substring(11,16));
                                    java.util.Date T2 = format1.parse(endTime.substring(0,10) +":" + endTime.substring(11,16));
                                    java.util.Date T3 = format1.parse(JO.getString("StartTime").substring(0,10) +":" +JO.getString("StartTime").substring(11,16));
                                    java.util.Date T4 = format1.parse(JO.getString("EndTime").substring(0,10)+":" +JO.getString("EndTime").substring(11,16));
                                    System.out.println(T3.toString() + T1.toString());
                                    if ((JO.getInt("RouteID") == CabID &&!((JO.getString("RollNo")).equals(email)) && (JO.getString("StartTime").substring(0,10)).equals(startTime.substring(0,10)) && ((T3.compareTo(T1) >=0 && T3.compareTo(T2) <=0) || (T4.compareTo(T1)>=0 && T4.compareTo(T2)<=0 ))  )) {

                                        mNames.add(JO.get("Name").toString());
                                        mEmails.add(JO.get("RollNo").toString());

                                    }

                                }
                                CabSharing = view.findViewById(R.id.cs_home_recycler);
                                RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mNames, mEmails);
                                CabSharing.setAdapter(adapter);
                                CabSharing.setLayoutManager(new LinearLayoutManager(getContext()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });


            queue.add(stringRequest);

        }

    }
}
