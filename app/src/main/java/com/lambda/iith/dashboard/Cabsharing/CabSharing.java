package com.lambda.iith.dashboard.Cabsharing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.WorkManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.clans.fab.FloatingActionButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.lambda.iith.dashboard.Launch;
import com.lambda.iith.dashboard.MainActivity;
import com.lambda.iith.dashboard.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;

import Adapters.RecyclerViewAdapter;
import Model.Filter;

public class CabSharing extends AppCompatActivity {


    public int flag;

    private RecyclerView recyclerView;
    private ImageButton CabRefresh;
    private TextView textView;
    private CardView dateCard , Time1 , RouteC;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private String email, startTime, endTime;
    private TextView Date, time1, time2, cab , Date2;
    private int CabID;
    private RequestQueue queue;
    private SharedPreferences sharedPref;
    private String idToken;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_sharing);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        dateCard = findViewById(R.id.cardView2);
        RouteC = findViewById(R.id.cardView);
        Time1 = findViewById(R.id.cardView3);
        textView = findViewById(R.id.CAbType);
        recyclerView = findViewById(R.id.Recycler);
        CabRefresh = findViewById(R.id.CabRefresh);
        CabRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateShares();
            }
        });

        flag = 0;
        queue = Volley.newRequestQueue(this);
        int width = (int) convertPxToDp(this , Launch.width/2);

        ViewGroup.LayoutParams params = dateCard.getLayoutParams();
        final float scale = this.getResources().getDisplayMetrics().density;
        params.width = (int) ((width - 20) * scale + 0.5f);
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        dateCard.setLayoutParams(params);

        ViewGroup.LayoutParams params1 = Time1.getLayoutParams();

        params1.width = (int) ((width - 20) * scale + 0.5f);
        params1.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        Time1.setLayoutParams(params1);
        ViewGroup.LayoutParams params2 = RouteC.getLayoutParams();
        params2.width = (int) ((width*2 - 22) * scale + 0.5f);
        params2.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        RouteC.setLayoutParams(params2);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url

            email = user.getEmail();

            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        idToken = task.getResult().getToken();


                    }
                }
            });

        }


        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        startTime = sharedPref.getString("startTime", "    NA      NA  ");
        endTime = sharedPref.getString("endTime", "    NA      NA  ");
        CabID = sharedPref.getInt("Route", 100);


        Date = findViewById(R.id.CS_Date);
        time1 = findViewById(R.id.CS_Time1);
        time2 = findViewById(R.id.CS_Time2);
        Date2 = findViewById(R.id.CS_Date2);
        cab = findViewById(R.id.CAbType);
        if(sharedPref.getBoolean("Registered" , false)) {

            Date.setText(startTime.substring(8, 10) + "-" + startTime.substring(5, 7) +"-" + startTime.substring(0, 4));
            time1.setText(startTime.substring(11, 16));
            time2.setText(endTime.substring(11, 16));

            Date2.setText(endTime.substring(8, 10) + "-" + endTime.substring(5, 7) +"-" + endTime.substring(0, 4));
        }

        if (CabID == 0) {
            cab.setText("RGIA -> IITH");
        } else if (CabID == 1) {
            cab.setText("IITH -> RGIA");
        } else {
            cab.setText("NA");
        }


        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(this.CONNECTIVITY_SERVICE);

        //we are connected to a network
        ParseJSON();


        FloatingActionButton floatingActionButton = findViewById(R.id.menu_item1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CabSharing.this, CabSharingRegister.class));
            }
        });


        FloatingActionButton floatingActionButton1 = findViewById(R.id.menu_item2);
        floatingActionButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    DeleteBooking();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
    }


    private void ParseJSON() {
        try {

            mEmails.clear();

            JSONArray JA3 = new JSONArray(sharedPref.getString("CabShares", null));
            JSONObject JO3;
            for (int j = 0; j < JA3.length(); j++) {
                JO3 = (JSONObject) JA3.get(j);
                mEmails.add(JO3.getString("Email"));


            }
            UpdateRecycler(mEmails);

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
        }

    }


    private void UpdateRecycler(ArrayList<String> Emails) {


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this, Emails);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void DeleteBooking() throws JSONException {

        WorkManager.getInstance().cancelAllWorkByTag("CAB");

        if (sharedPref.getInt("Private", -1) == 1) {
            Toast.makeText(this.getBaseContext(), "Deleted Successfully",
                    Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.remove("startTime");
            edit.remove("endTime");
            edit.remove("Route");
            edit.putBoolean("Registered", false);
            edit.remove("Private");
            edit.remove("CabShares");
            edit.commit();
            Intent i = new Intent(CabSharing.this, CabSharing.class);
            finish();
            overridePendingTransition(0, 0);
            startActivity(i);
            overridePendingTransition(0, 0);
//            Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentlayout);
//            FragmentTransaction ft = getFragmentManager().beginTransaction();
//            ft.detach(fragment);
//            ft.attach(fragment);
//            ft.commit();
            return;
        }

        if ((sharedPref.getInt("Private", -1) == -1)) {
            Toast.makeText(this.getBaseContext(), "No Booking",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if ((sharedPref.getInt("Private", -1) == 0)) {

            String URL = "https://iith.dev/delete";
            JSONObject jsonBody = new JSONObject();
            //jsonBody.put("Name" , name);

            jsonBody.put("Email", email);
            jsonBody.put("StartTime", startTime);
            jsonBody.put("EndTime", endTime);
            jsonBody.put("RouteID", CabID);

            jsonBody.put("Token", MainActivity.idToken);
            final String requestBody = jsonBody.toString();
            StringRequest stringRequest1 = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(CabSharing.this, "Deleted Successfully",
                            Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.remove("startTime");
                    edit.remove("endTime");
                    edit.remove("Route");
                    edit.remove("Private");
                    edit.remove("CabShares");
                    edit.putBoolean("Registered", false);
                    edit.commit();

                    Log.i("VOLLEY", response);
                }
            }, new Response.ErrorListener() {


                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(CabSharing.this.getBaseContext(), "Delete Unuccessfull",
                            Toast.LENGTH_SHORT).show();
                    Log.e("VOLLEY", error.toString());
                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
                }

                @Override
                public byte[] getBody() throws AuthFailureError {
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);
                        // can get more details such as response.headers
                    }
                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                }
            };
            queue.add(stringRequest1);
            queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
                @Override
                public void onRequestFinished(Request<Object> request) {
//                    Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentlayout);
//                    FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.detach(fragment);
//                    ft.attach(fragment);
//                    ft.commit();
                      Intent i = new Intent(CabSharing.this, CabSharing.class);
                      finish();
                      overridePendingTransition(0, 0);
                      startActivity(i);
                      overridePendingTransition(0, 0);
                }
            });
        }


    }

    //Deleting from server
    public double convertPxToDp(Context context, double px) {
        return px / context.getResources().getDisplayMetrics().density;
    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }

    public void updateShares(){
        final String url4 = "https://iith.dev/query";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Filter filter = new Filter();
                        try {
                            filter.set(response , sharedPref);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        new BookingFilter().execute(filter);


                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
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


            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Toast.makeText(CabSharing.this.getBaseContext(), "Booked Successfully",
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
                    return requestBody == null ? null : requestBody.getBytes(StandardCharsets.UTF_8);

                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    String responseString = "";
                    if (response != null) {
                        responseString = String.valueOf(response.statusCode);

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


}


