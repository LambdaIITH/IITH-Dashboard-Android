package com.lambda.iith.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.internal.BottomNavigationItemView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.honorato.multistatetogglebutton.MultiStateToggleButton;
import org.honorato.multistatetogglebutton.ToggleButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import Adapters.RecyclerViewAdapter_CSHOME;

public class HomeScreenFragment extends Fragment {

    //Fragment to be displayed on Home screen which will have 3 cards as layout.
    private CardView bus, mess, timetable, cab;
    private Space space1;
    private RecyclerView CabSharing;
    private TextView mess1;
    private ArrayList<String> mNames = new ArrayList<>();
    private ArrayList<String> mEmails = new ArrayList<>();
    private View view;
    private String email;
    private int flag;
    private SharedPreferences sharedPref, sharedPreferences;
    private TextView t1, t2, t3, t4;
    private MultiStateToggleButton toggleButton;
    private RequestQueue queue, queue2, queue3;
    private ScrollView MessScroll;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.home_screen, container, false);
        queue = Volley.newRequestQueue(getContext());
        queue2 = Volley.newRequestQueue(getContext());
        queue3 = Volley.newRequestQueue(getContext());
        mess = (CardView) view.findViewById(R.id.MessCard);
        MessScroll = view.findViewById(R.id.MessScroll);
        cab = view.findViewById(R.id.CabCard);
        MessScroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new Timetable()).commit();
            }
        });
        timetable = view.findViewById(R.id.TimetableCard);
        timetable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new Timetable()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_acads);
            }
        });
        bus = view.findViewById(R.id.BusCard);
        bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction().replace(R.id.fragmentlayout, new FragmentBS()).commit();
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.nav_bus);
            }
        });


        cab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), CabSharing.class));

            }
        });


        t1 = view.findViewById(R.id.toLing);
        t2 = view.findViewById(R.id.toODF);
        toggleButton = view.findViewById(R.id.BusToggleHome);
        toggleButton.setValue(0);
        t3 = view.findViewById(R.id.toLing2);
        t4 = view.findViewById(R.id.toODF2);


        toggleButton.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int value) {
                busmake(true);
            }
        });



        mess1 = view.findViewById(R.id.menu_home);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getContext());
        sharedPreferences = sharedPref;
        System.out.println(sharedPref.getBoolean("cab", false) && sharedPref.getBoolean("Registered", false));
        cabCardMake(sharedPref.getBoolean("cab", false) && sharedPref.getBoolean("Registered", false));
        busmake(sharedPref.getBoolean("bus", true));
        messmake(sharedPref.getBoolean("mess", true));
        timetablemake(sharedPref.getBoolean("timetable" , true));


        //cardView2.setVisibility(View.GONE);
        //space1.setVisibility(View.GONE);
        return view;
    }

    private void busmake(boolean b) {
        if (b) {
            String string;
            String temp;
            bus.setVisibility(View.VISIBLE);
            try {
                JSONObject JO = null;
                if (toggleButton.getValue() == 1) {
                    JO = new JSONObject(sharedPref.getString("ToIITH", "{\"LAB\":\"02:00 ,02:30 ,03:15 ,04:00 ,04:45 ,05:30 ,06:15 ,07:00 ,07:30 ,08:00 ,08:30 ,13:15 ,14:30 ,15:00 ,19:45 ,20:30,\",\"LINGAMPALLY\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODF\":\"08:20 , 09:30 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,19:35 ,20:30 ,21:00 ,22:00 ,23:00,\",\"SANGAREDDY\":\"08:45 ,13:45 ,18:00 ,18:40 ,22:00,\",\"LINGAMPALLYW\":\"09:15 ,13:15 ,15:00 ,17:00 ,19:15 ,22:15,\",\"ODFS\":\"08:00 ,09:15 ,11:30 ,12:30 ,14:00 ,15:30 ,17:00 ,18:30 ,19:45 ,20:30 ,21:30 ,23:00,\"}"));
                } else {
                    JO = new JSONObject(sharedPref.getString("FromIITH", "{  \"LAB\": \"01:45 ,02:15 ,03:00 ,03:45 ,04:30 ,05:15 ,06:00 ,06:45 ,07:15 ,07:45 ,08:15 ,13:00 ,14:15 ,14:45 ,19:30 ,20:15,\",  \"LINGAMPALLY\": \"11:30 ,13:15 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODF\": \"09:00 ,10:30 ,12:10 ,13:10 ,14:45 ,17:45 ,18:00 ,19:00 ,20:15 ,21:00 ,22:00 ,23:00,\",  \"SANGAREDDY\": \"08:30 ,13:30 ,17:45 ,18:25 ,20:40,\",  \"LINGAMPALLYW\": \"10:30 ,12:30 ,14:45 ,17:45 ,19:00 ,20:45,\",  \"ODFS\": \"08:40 ,10:15 ,12:10 ,13:15 ,14:45 ,16:10 ,17:45 ,19:10 ,20:30 ,21:10 ,22:10 ,23:30,\"}"));
                }
                Date date = Calendar.getInstance().getTime();

                SimpleDateFormat dateFormat = new SimpleDateFormat("YYYY:MM:dd");
                String da = dateFormat.format(Calendar.getInstance().getTime());
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 7 || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                    string = JO.getString("LINGAMPALLYW");
                } else {
                    string = JO.getString("LINGAMPALLY");
                }

                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
                        //t1.setText(format1.format(date));
                        java.util.Date T1 = format1.parse(da + ":" + temp.trim());

                        if (T1.compareTo(date) > 0) {
                            t1.setText(temp);
                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }
                if (Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == 1) {
                    string = JO.getString("ODFS");
                } else {
                    string = JO.getString("ODF");
                }
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:dd:HH:mm");
                        ;
                        java.util.Date T1 = format1.parse(da + ":" + temp.trim());

                        if (T1.compareTo(date) > 0) {
                            t2.setText(temp);
                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }


                //String da = dateFormat.format(Calendar.getInstance().getTime());
                string = JO.getString("LAB");
                temp = "";
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:ddHH:mm");
                        ;
                        java.util.Date T1 = format1.parse(da + temp.trim());

                        if (T1.compareTo(date) > 0) {
                            t3.setText(temp);
                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }
                string = JO.getString("SANGAREDDY");
                temp = "";
                flag = 0;
                for (int i = 0; i < string.length(); i++) {
                    if (string.substring(i, i + 1).equals(",")) {
                        SimpleDateFormat format1 = new SimpleDateFormat("yyyy:MM:ddHH:mm");
                        ;
                        t4.setText(temp);

                        java.util.Date T1 = format1.parse(da + temp.trim());

                        if (T1.compareTo(date) > 0) {

                            break;
                        }
                        temp = "";
                        continue;
                    }

                    temp += string.substring(i, i + 1);


                }

            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        } else {
            bus.setVisibility(View.GONE);
        }
    }

    private void messmake(boolean b) {
        if (b) {
            mess.setVisibility(View.VISIBLE);

            String data;
            try {
                if (sharedPref.getInt("MESSDEF", 1) == 1) {
                    data = sharedPref.getString("UDH", MainActivity.UDH);
                } else {
                    data = sharedPref.getString("LDH", MainActivity.LDH);
                }

                JSONArray Data = new JSONArray(data);
                int day = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);


                String string1 = "10:30:00";
                Date time1 = new SimpleDateFormat("HH:mm:ss").parse(string1);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.setTime(time1);

                String string2 = "14:30:00";
                Date time2 = new SimpleDateFormat("HH:mm:ss").parse(string2);
                Calendar calendar2 = Calendar.getInstance();
                calendar2.setTime(time2);

                String string3 = "18:30:00";
                Date time3 = new SimpleDateFormat("HH:mm:ss").parse(string3);
                Calendar calendar3 = Calendar.getInstance();
                calendar3.setTime(time3);

                String string4 = "22:30:00";
                Date time4 = new SimpleDateFormat("HH:mm:ss").parse(string4);
                Calendar calendar4 = Calendar.getInstance();
                calendar4.setTime(time4);


                Calendar calendarn = Calendar.getInstance();
                Date d = calendarn.getTime();

                DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                String formattedDate = dateFormat.format(d);
                Date timen = new SimpleDateFormat("HH:mm:ss").parse(formattedDate);
                JSONObject JO = Data.getJSONObject(day - 1);
                System.out.println(day);

                JSONObject JO2 = Data.getJSONObject(day % 7);

                if (timen.before(time1)) {

                    mess1.setText("Today's Breakfast \n \n" + JO.getString("Breakfast"));

                } else if (timen.after(calendar1.getTime()) && timen.before(calendar2.getTime())) {
                    //checkes whether the current time is between 10:30:00 and 14:30:00.
                    mess1.setText("Today's Lunch \n" + JO.getString("Lunch"));

                } else if (timen.after(calendar2.getTime()) && timen.before(calendar3.getTime())) {
                    mess1.setText("Today's Snacks \n" + JO.getString("Snacks"));

                } else if (timen.after(calendar3.getTime()) && timen.before(calendar4.getTime())) {
                    mess1.setText("Today's Dinner \n" + JO.getString("Dinner"));

                } else if (timen.after(time4)) {


                    mess1.setText("Tomorrow's Breakfast \n \n" + JO2.getString("Breakfast"));


                }


            } catch (ParseException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else {
            mess.setVisibility(View.GONE);
        }
    }

    private void timetablemake(boolean b) {
        if (b) {
            timetable.setVisibility(View.VISIBLE);
        } else {
            timetable.setVisibility(View.GONE);
        }
    }


    private void cabCardMake(boolean b) {
        if (b) {
            cab.setVisibility(View.VISIBLE);
            final String startTime = sharedPref.getString("startTime", "    NA      NA  ");
            final String endTime = sharedPref.getString("endTime", "    NA      NA  ");
            final int CabID = sharedPref.getInt("Route", 100);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String url = "http://www.iith.dev/query";
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
                                JSONArray JA2 = new JSONArray();
                                for (int i = 0; i < JA.length(); i++) {
                                    JSONObject JO = (JSONObject) JA.get(i);


                                    SimpleDateFormat format1 = new SimpleDateFormat("YYYY-mm-dd:HH:MM");
                                    java.util.Date T1 = format1.parse(startTime.substring(0, 10) + ":" + startTime.substring(11, 16));
                                    java.util.Date T2 = format1.parse(endTime.substring(0, 10) + ":" + endTime.substring(11, 16));
                                    java.util.Date T3 = format1.parse(JO.getString("StartTime").substring(0, 10) + ":" + JO.getString("StartTime").substring(11, 16));
                                    java.util.Date T4 = format1.parse(JO.getString("EndTime").substring(0, 10) + ":" + JO.getString("EndTime").substring(11, 16));
                                    System.out.println(T3.toString() + T1.toString());
                                    if ((JO.getInt("RouteID") == CabID && !((JO.getString("Email")).equals(email)) && (JO.getString("StartTime").substring(0, 10)).equals(startTime.substring(0, 10)) && ((T3.compareTo(T1) >= 0 && T3.compareTo(T2) <= 0) || (T4.compareTo(T1) >= 0 && T4.compareTo(T2) <= 0)))) {

                                        JA2.put(JO);
                                        //mNames.add(JO.getString(""));
                                        mEmails.add(JO.getString("Email"));

                                    }

                                }
                                SharedPreferences.Editor editor = sharedPref.edit();
                                editor.putString("CabShares", JA2.toString());
                                editor.commit();


                                CabSharing = view.findViewById(R.id.cs_home_recycler);
                                RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mEmails);
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
                    try {
                        mNames.clear();
                        mEmails.clear();
                        JSONArray JA4 = new JSONArray(sharedPref.getString("CabShares", "NULL"));
                        JSONObject JO = new JSONObject();
                        for (int k = 0; k < JA4.length(); k++) {
                            JO = JA4.getJSONObject(k);
                            mEmails.add(JO.getString("Email"));

                            CabSharing = view.findViewById(R.id.cs_home_recycler);
                            RecyclerViewAdapter_CSHOME adapter = new RecyclerViewAdapter_CSHOME(getContext(), mEmails);
                            CabSharing.setAdapter(adapter);
                            CabSharing.setLayoutManager(new LinearLayoutManager(getContext()));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });


            queue.add(stringRequest);

        } else {
            cab.setVisibility(View.GONE);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        cabCardMake(sharedPref.getBoolean("cab", false) && sharedPref.getBoolean("Registered", false));
        busmake(sharedPref.getBoolean("bus", true));
        messmake(sharedPref.getBoolean("mess", true));
        timetablemake(sharedPref.getBoolean("timetable", true));
    }


    private void refresh() {
        String url = "https://jsonblob.com/api/jsonBlob/835519fb-ae2b-11e9-8313-bf8495d5f167";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray JA = null;
                        // Display the first 500 characters of the response string.
                        try {
                            JA = new JSONArray(response);


                            SharedPreferences.Editor edit = sharedPreferences.edit();
                            edit.putString("ToIITH", JA.getString(1));
                            edit.putString("FromIITH", JA.getString(0));
                            edit.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });
        String url2 = "https://jsonblob.com/api/6336df25-aeb3-11e9-99ce-c9fa198f2f2e";
        String url3 = "https://jsonblob.com/api/c2d3dd6e-aebc-11e9-99ce-116fae627a57";
        MainActivity.initiate();


        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray JA = null;
                        // Display the first 500 characters of the response string.


                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("UDH", response);

                        edit.commit();


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });
        StringRequest stringRequest3 = new StringRequest(Request.Method.GET, url3,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray JA = null;
                        // Display the first 500 characters of the response string.


                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("LDH", response);

                        edit.commit();


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });

        queue.add(stringRequest);
        queue2.add(stringRequest2);

        queue3.add(stringRequest3);


    }
}
