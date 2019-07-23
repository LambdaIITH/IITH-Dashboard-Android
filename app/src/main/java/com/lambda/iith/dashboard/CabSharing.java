package com.lambda.iith.dashboard;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;



import Adapters.RecyclerViewAdapter;

public class CabSharing extends AppCompatActivity {


    private ImageButton refresh;
    public int flag;
    public static RecyclerView recyclerView;

    private TextView textView;

    public static ArrayList <String> mNames = new ArrayList<>();
    public static ArrayList <String> mEmails= new ArrayList<>();
    private String email , startTime , endTime;
    private TextView Date , time1 , time2 , cab;
    private int CabID;
    private RequestQueue queue;
    private SharedPreferences sharedPref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_sharing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        textView = findViewById(R.id.CAbType);
        recyclerView = (RecyclerView) findViewById(R.id.Recycler);
        flag = 0;
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url

            email = user.getEmail().toString();
        }

        sharedPref = PreferenceManager.getDefaultSharedPreferences(CabSharing.this);
        startTime = sharedPref.getString("startTime","    NA      NA  " );
        endTime = sharedPref.getString("endTime","    NA      NA  " );
        CabID = sharedPref.getInt("Route",100 );


        Date = findViewById(R.id.CS_Date);
        time1 = findViewById(R.id.CS_Time1);
        time2 = findViewById(R.id.CS_Time2);
        cab = findViewById(R.id.CAbType);
        Date.setText(startTime.substring(0,10));
        time1.setText(startTime.substring(11,16));
        time2.setText(endTime.substring(11,16));

        if(CabID == 0){
            cab.setText( "RGIA -> IITH");
        }else if(CabID==1){cab.setText( "IITH -> RGIA");
        }else{ cab.setText("NA");}




        queue = Volley.newRequestQueue(this);
        refresh();
        refresh = findViewById(R.id.CabSharingRefresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refresh();
            }
        });




    FloatingActionButton floatingActionButton = findViewById(R.id.menu_item1);
   floatingActionButton.setOnClickListener(new View.OnClickListener() {
       @Override
        public void onClick(View v) {
            startActivity(new Intent(CabSharing.this , CabSharingRegister.class));
       }
   });


   FloatingActionButton floatingActionButton1 = findViewById(R.id.menu_item2);
   floatingActionButton1.setOnClickListener(new View.OnClickListener() {
       @Override
       public void onClick(View v) {
           DeleteBooking();
           refresh();
       }
   });

    }

    private void refresh(){
        //Updating Booking Info
        sharedPref = PreferenceManager.getDefaultSharedPreferences(CabSharing.this);
        startTime = sharedPref.getString("startTime","    NA      NA  " );
        endTime = sharedPref.getString("endTime","    NA      NA  " );
        CabID = sharedPref.getInt("Route",100 );

        Date.setText(startTime.substring(0,10));
        time1.setText(startTime.substring(11,16));
        time2.setText(endTime.substring(11,16));

        if(CabID == 0){
            cab.setText( "Cab : RGIA to IITH");
        }else if(CabID==1){cab.setText( "Cab : IITH to RGIA");
        }else{ cab.setText("NA");}

        //Updating Entries
        String url = "http://13.233.90.143/query";
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
                            UpdateRecycler(mNames , mEmails);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                textView.setText("That didn't work!");
            }
        });


        queue.add(stringRequest);
    }

    private boolean checkforupdates() {
        try {
            String URL = "http://13.233.90.143/update";
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("QueryTimeStart", startTime);
            jsonBody.put("QueryTimeEnd", endTime);
            jsonBody.put("RouteID", CabID);

            final String requestBody = jsonBody.toString();
            System.out.println(requestBody);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(getApplication().getBaseContext(), "Booked Successfully",
                            Toast.LENGTH_SHORT).show();



                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    try {
                        return requestBody == null ? null : requestBody.getBytes("utf-8");

                    } catch (UnsupportedEncodingException uee) {
                        VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                        return null;
                    }
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        System.out.println(responseString);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };

            queue.add(stringRequest);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return false;

    }


    private void UpdateRecycler(ArrayList <String> Names , ArrayList <String> Emails ){



        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, Names, Emails);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void DeleteBooking(){

        //Deleting Locally
        SharedPreferences.Editor edit = sharedPref.edit();
        edit.remove("startTime");
        edit.remove("endTime");
        edit.remove("Route");
        edit.putBoolean("Registered" , false);
        edit.commit();

        //Deleting from server




    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }
}


