package com.lambda.iith.dashboard.cabsharing;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import Adapters.RecyclerViewAdapter;

public class CabSharing extends Fragment {


    public int flag;

    private RecyclerView recyclerView;

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
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_cab_sharing, container, false);
        dateCard = view.findViewById(R.id.cardView2);
        RouteC = view.findViewById(R.id.cardView);
        Time1 = view.findViewById(R.id.cardView3);
        textView = view.findViewById(R.id.CAbType);
        recyclerView = view.findViewById(R.id.Recycler);
        flag = 0;
        queue = Volley.newRequestQueue(getContext());
        int width = (int) convertPxToDp(getContext() , Launch.width/2);

        ViewGroup.LayoutParams params = dateCard.getLayoutParams();
        final float scale = getContext().getResources().getDisplayMetrics().density;
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


        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());

        startTime = sharedPref.getString("startTime", "    NA      NA  ");
        endTime = sharedPref.getString("endTime", "    NA      NA  ");
        CabID = sharedPref.getInt("Route", 100);


        Date = view.findViewById(R.id.CS_Date);
        time1 = view.findViewById(R.id.CS_Time1);
        time2 = view.findViewById(R.id.CS_Time2);
        Date2 = view.findViewById(R.id.CS_Date2);
        cab = view.findViewById(R.id.CAbType);
       ;
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


        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE);

        //we are connected to a network
        ParseJSON();


        FloatingActionButton floatingActionButton = view.findViewById(R.id.menu_item1);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CabSharingRegister.class));
            }
        });


        FloatingActionButton floatingActionButton1 = view.findViewById(R.id.menu_item2);
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

        return view;
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


        RecyclerViewAdapter adapter = new RecyclerViewAdapter(getContext(), Emails);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void DeleteBooking() throws JSONException {

        if (sharedPref.getInt("Private", -1) == 1) {
            Toast.makeText(getActivity().getBaseContext(), "Deleted Successfully",
                    Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.remove("startTime");
            edit.remove("endTime");
            edit.remove("Route");
            edit.putBoolean("Registered", false);
            edit.remove("Private");
            edit.remove("CabShares");
            edit.commit();
            Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentlayout);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.detach(fragment);
            ft.attach(fragment);
            ft.commit();
            return;
        }

        if ((sharedPref.getInt("Private", -1) == -1)) {
            Toast.makeText(getActivity().getBaseContext(), "No Booking",
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
                    Toast.makeText(getActivity().getBaseContext(), "Deleted Successfully",
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
                    Toast.makeText(getActivity().getBaseContext(), "Delete Unuccessfull",
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
                    Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentlayout);
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.detach(fragment);
                    ft.attach(fragment);
                    ft.commit();
                }
            });
        }


    }

    //Deleting from server
    public double convertPxToDp(Context context, double px) {
        return px / context.getResources().getDisplayMetrics().density;
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
                    Toast.makeText(getActivity().getBaseContext(), "Booked Successfully",
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


