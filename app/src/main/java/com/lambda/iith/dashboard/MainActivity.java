package com.lambda.iith.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.lambda.iith.dashboard.BackgroundTasks.DPload;
import com.lambda.iith.dashboard.Cabsharing.BookingFilter;
import com.lambda.iith.dashboard.Cabsharing.CabSharing;
import com.lambda.iith.dashboard.Cabsharing.CabSharingBackgroundWork;
import com.lambda.iith.dashboard.MainFragments.FragmentBS;
import com.lambda.iith.dashboard.MainFragments.HomeScreenFragment;
import com.lambda.iith.dashboard.MainFragments.MessMenu;
import com.lambda.iith.dashboard.Timetable.AddCourse;
import com.lambda.iith.dashboard.Timetable.NotificationInitiator;
import com.lambda.iith.dashboard.Timetable.Timetable;
import com.lambda.iith.dashboard.Timetable.timetableComp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import Model.Filter;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static BottomNavigationView bottomNavigationView;
    public static String idToken;
    public static ImageView imageView;

    public String name;
    public static String email;
    public Uri photoUrl;
    int a;
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;

    private ImageButton MasterRefresh;
    private SharedPreferences sharedPreferences;
    private TextView Nav_Bar_Header; //Navigation Bar Header i.e User Name
    private TextView Nav_Bar_Email; //Navigation Bar email
    private List<String> courseList;
    private List<String> courseSegmentList;
    private List<String> slotList;
    private ArrayList<String> CourseName;
    private RequestQueue queue , queue1;
    private SwipeRefreshLayout pullToRefresh;
    private FragmentManager fragmentManager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Toolbar toolbar = findViewById(R.id.toolbar);


            switch (menuItem.getItemId()) {
                case R.id.nav_home: {
                    a = 1;
                    MasterRefresh.setVisibility(View.VISIBLE);
                    pullToRefresh.setEnabled(true);
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    toolbar.setTitle("IITH Dashboard");
                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new HomeScreenFragment()).commit();

                    return true;
                }

                case R.id.nav_acads: {
                    a = 2;

                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case DialogInterface.BUTTON_POSITIVE:
                                    fetchData();
                                    break;

                                case DialogInterface.BUTTON_NEGATIVE:
                                    //No button clicked
                                    break;
                            }
                        }
                    };


                    findViewById(R.id.TimeTableRefresh).setVisibility(View.VISIBLE);
                    pullToRefresh.setEnabled(false);
                    pullToRefresh.setRefreshing(false);
                    MasterRefresh.setVisibility(View.GONE);
                    toolbar.setTitle("Timetable");

                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new Timetable()).commit();

                    findViewById(R.id.addcourse).setVisibility(View.VISIBLE);
                    findViewById(R.id.addcourse).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this, AddCourse.class));
                        }
                    });


                    return true;
                }

                case R.id.nav_bus: {
                    a = 3;
                    MasterRefresh.setVisibility(View.VISIBLE);
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    pullToRefresh.setEnabled(true);
                    toolbar.setTitle("Bus Schedule");
                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new FragmentBS()).commit();

                    return true;
                }

                case R.id.nav_mess: {
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    MasterRefresh.setVisibility(View.VISIBLE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    pullToRefresh.setEnabled(true);
                    toolbar.setTitle("Mess Menu");
                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
                    a = 4;
                    return true;
                }

                case R.id.nav_cab: {
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    MasterRefresh.setVisibility(View.VISIBLE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    pullToRefresh.setEnabled(true);
                    toolbar.setTitle("Cab Sharing");
                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new CabSharing()).commit();
                    a = 4;
                    return true;
                }

            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        fragmentManager = getSupportFragmentManager();
        queue = Volley.newRequestQueue(MainActivity.this);
        queue1 = Volley.newRequestQueue(MainActivity.this);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        refresh();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Generate Timetable from Storage
        timetableComp Timetablecomp = new timetableComp();
        Timetablecomp.execute(getApplicationContext());

        //Getting User Login details
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName();
            email = user.getEmail();
            photoUrl = user.getPhotoUrl();


            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        idToken = task.getResult().getToken();



                    }
                }
            });
        }
        sharedPreferences.edit().putString("UserEmail" , email).apply();

        //Setting Refresh and Swipe to Refresh
        MasterRefresh = findViewById(R.id.MainRefresh);

        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setRefreshing(false);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                refresh();


            }
        });
        MasterRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                pullToRefresh.setRefreshing(true);
                refresh();


            }
        });
        //Initiating Navigation Drawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setOnCreateContextMenuListener(this);

        //Setting Up BottomNav
        bottomNavigationView = findViewById(R.id.BottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        bottomNavigationView.setSelectedItemId(R.id.nav_home);

        //GoogleSignInClient
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        //Getting Settings menu for first run
        if (sharedPreferences.getBoolean("firstrun", true)) {
            fetchData();
            sharedPreferences.edit().putBoolean("firstrun", false).commit();
        }


        //Setting up Timetable Refresh ( Displayed only when timetable fragment is active
        findViewById(R.id.TimeTableRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Warning");
                alert.setMessage("This will clear any changes you made and restore your timetable to the AIMS version. Press Sync to proceed or cancel to discontinue");
                alert.setPositiveButton("Sync", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        fetchData();

                    }
                });

                alert.setNegativeButton("Cancel", null);
                alert.show();


            }
        });

        if(sharedPreferences.getBoolean("Registered" , false)){
            WorkManager.getInstance().cancelAllWorkByTag("CAB");
            PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(CabSharingBackgroundWork.class , 1 , TimeUnit.HOURS).addTag("CAB").build();
            WorkManager.getInstance().enqueue(periodicWorkRequest);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        if (sharedPreferences.getBoolean("PleaseUpdateCAB", false)) {
            pullToRefresh.setRefreshing(true);
            refresh();

            sharedPreferences.edit().putBoolean("PleaseUpdateCAB", false).commit();
        }

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            this.finishAffinity();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Nav_Bar_Header = findViewById(R.id.Nav_Bar_name);
        // Setting Username recieved from google account in navigation bar
        Nav_Bar_Email = findViewById(R.id.nav_bar_email);
        if (sharedPreferences.getBoolean("DEVELOPER", false)) {
            NavigationView navigationView = findViewById(R.id.nav_view);
            navigationView.getMenu().findItem(R.id.nav_dev).setVisible(true);
            sharedPreferences.edit().putBoolean("DEVELOPER", false).commit();

        }
        imageView = findViewById(R.id.nav_bar_dp);
        try {

            Nav_Bar_Email.setText(email); // Setting email recieved from google account in navigation bar
            Nav_Bar_Header.setText(name);
            new DPload().execute(photoUrl.toString());
        } catch (Exception e) {

            e.printStackTrace();
        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        return super.onOptionsItemSelected(item);
    }

    private void signOut() {
        // Firebase sign out

        FirebaseAuth.getInstance().signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(MainActivity.this, NoLogin.class));
                    }
                });
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Toolbar toolbar = findViewById(R.id.toolbar);

        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();

        //SideNav Settings
        if (id == R.id.logout) {
            signOut();


        } else if (id == R.id.nav_Settings) {
            startActivity(new Intent(MainActivity.this, Settings.class));
        } else if (id == R.id.about) {
            startActivity(new Intent(MainActivity.this, About.class));
        } else if (id == R.id.nav_dev) {
            startActivity(new Intent(MainActivity.this, Developer.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onStart() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        Launch.height = displayMetrics.heightPixels;
        Launch.width = displayMetrics.widthPixels;
        if (sharedPreferences.getInt("MESSDEF", -1) == -1 || sharedPreferences.getString("DefaultSegment", "0").equals('0')) {
            setValues();


        }

        if (sharedPreferences.getBoolean("FirstAfterV1.22", true)) {
            final AlertDialog.Builder b1 = new AlertDialog.Builder(this);
            b1.setTitle("How many minutes before you need notification");
            b1.setCancelable(false);
            String[] types1 = {"5 mins", "10 mins" ,"15 mins","30 mins","45 mins" , "60 mins"};
            b1.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    checkBatteryStatus();
                }
            });
            b1.setItems(types1, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(which==0){
                        sharedPreferences.edit().putInt("NotificationTime" , 5).commit();}

                    if(which==1) {
                        sharedPreferences.edit().putInt("NotificationTime", 10).commit();
                    }
                    if(which==2) {
                        sharedPreferences.edit().putInt("NotificationTime", 15).commit();

                    }if(which==3){
                        sharedPreferences.edit().putInt("NotificationTime" , 30).commit();
                    }if(which==4){
                        sharedPreferences.edit().putInt("NotificationTime" , 45).commit();
                    }if(which==5){
                        sharedPreferences.edit().putInt("NotificationTime" , 60).commit();
                    }
                    WorkManager.getInstance().cancelAllWorkByTag("LECTUREREMINDER");
                    WorkManager.getInstance().cancelAllWorkByTag("TIMETABLE");
                    PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(NotificationInitiator.class, 6, TimeUnit.HOURS).addTag("TIMETABLE").build();
                    WorkManager.getInstance().enqueue(periodicWorkRequest);
                    dialog.dismiss();

                }

            });


            AlertDialog.Builder b = new AlertDialog.Builder(this);
            b.setTitle("Want to recieve notifications before lectures?");
            String[] types = {"YES", "NO"};
            b.setCancelable(false);
            b.setItems(types, new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (which == 0) {
                        sharedPreferences.edit().putBoolean("EnableLectureNotification", true).commit();
                        b1.show();

                        sharedPreferences.edit().putBoolean("RequestAutostart", true).commit();

                    } else {
                        sharedPreferences.edit().putBoolean("EnableLectureNotification", false).commit();
                    }

                }


            });
            b.show();


            sharedPreferences.edit().putBoolean("FirstAfterV1.20", false).commit();

        }

        super.onStart();


    }

    private void checkBatteryStatus() {
        System.out.println(getBaseContext().getPackageName());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent();
            String packageName = getPackageName();
            PowerManager pm = (PowerManager) getSystemService(POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Please Disable Battery Optimization");
                alert.setMessage("Please exempt IITH Dashboard to receive uninterrupted notifications");
                alert.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS), 0);
                    }
                });
                alert.show();



            }
        }
    }


    //First run values for default segment and mess

    private void setValues() {
        final AlertDialog.Builder b1 = new AlertDialog.Builder(this);
        b1.setTitle("What segment is currently running?");
        b1.setCancelable(false);
        String[] types1 = {"Segment 1-2", "Segment 3-4", "Segment 5-6"};
        b1.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                recreate();
            }
        });
        b1.setItems(types1, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                sharedPreferences.edit().putString("DefaultSegment", Integer.toString(22 * which + 12)).commit();
                dialog.dismiss();



            }

        });



        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Select Your Mess");
        String[] types = {"LDH", "UDH"};
        b.setCancelable(false);
        b.setItems(types, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {


                sharedPreferences.edit().putInt("MESSDEF", which).commit();
                dialog.dismiss();
                if (sharedPreferences.getString("DefaultSegment", "0").equals("0")) {
                    b1.show();
                }

            }

        });
        b.show();

    }

    //This method is used to set the default screen that will be shown.
    private void setDefaultFragment(Fragment defaultFragment) {
        this.replaceFragment(new HomeScreenFragment());
    }

    //replaces the current fragment with the destination one.
    private void replaceFragment(Fragment destFragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        //beginning fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Replacing layout holder with the required fragment object
        fragmentTransaction.replace(R.id.drawer_layout, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();

    }

    private int Switch = 0;

    //Fetching Data for Cabs Buses and Mess
    private void refresh() {
        if (!sharedPreferences.getBoolean("Registered", false) && sharedPreferences.getBoolean("FIRSTCABLAUNCH" , true)) {


            RetrieveBooking();
        }
        /**
        if(sharedPreferences.getBoolean("Registered", false) ) {


            checkForUpdates();

        }
        queue1.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {

            @Override
            public void onRequestFinished(Request<Object> request) {
                if(Switch==1){
                updateShares();}
            }
        });
         **/

        BusData();
        messData();

        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {


                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);

                FragmentTransaction ft = fragmentManager.beginTransaction();
                pullToRefresh.setRefreshing(false);

                ft.detach(fragment);
                ft.attach(fragment);
                ft.commitAllowingStateLoss();
                return;


            }
        });



    }


    private void BusData(){
        String url = "https://iith.dev/v2/bus";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject JO = null;
                        // Display the first 500 characters of the response string.
                        try {

                            JO = new JSONObject(response);

                            JSONObject JA = JO.getJSONObject("TOIITH");
                            Iterator iterator = JA.keys();
                            ArrayList<String> Buses = new ArrayList<>();
                            while (iterator.hasNext()) {
                                String key = (String) iterator.next();

                                Buses.add(key);


                            }
                            System.out.println(Buses);
                            SharedPreferences.Editor edit = sharedPreferences.edit();

                            Buses.remove("LINGAMPALLYW");
                            Buses.remove("ODFS");
                            edit.putString("BusTypes", Buses.toString());
                            edit.putString("ToIITH", JO.getJSONObject("TOIITH").toString());
                            edit.putString("FromIITH", JO.getJSONObject("FROMIITH").toString());
                            edit.commit();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });

        queue.add(stringRequest);
    }





    private void messData(){
        String url2 = "https://iith.dev/dining";


        StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONArray JA = null;
                        // Display the first 500 characters of the response string.


                        SharedPreferences.Editor edit = sharedPreferences.edit();
                        edit.putString("MESSJSON", response);

                        edit.commit();


                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });
        queue.add(stringRequest2);
    }

    //Retrieve Cab shares
    public void updateShares(){
        final String url4 = "https://iith.dev/query";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Filter filter = new Filter();
                        try {
                            filter.set(response , sharedPreferences);
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

    //Check if shares available
    private void checkForUpdates(){


        String URL9 = "https://iith.dev/update";
        JSONObject jsonBody = new JSONObject();


        final String start = sharedPreferences.getString("startTime", "    NA      NA  ");
        final String end = sharedPreferences.getString("endTime", "    NA      NA  ");
        final int route = sharedPreferences.getInt("Route", 100);


        try {
            jsonBody.put("QueryTimeStart", start);
            jsonBody.put("QueryTimeEnd", end);
            jsonBody.put("RouteID", route);

        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String requestBody = jsonBody.toString();


        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.POST, URL9, jsonBody, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {

                try {
                    if(response.getBoolean("IsUpdateReqd")){ Switch = 1;}
                    else{ Switch = 0;}
                } catch (JSONException e) {
                    Switch = 1;
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // TODO: Handle error

            }
        });

        queue1.add(stringRequest);
    }

    //Retrieve already existing booking of current user from server if exists
    private void RetrieveBooking(){
        final String url4 = "https://iith.dev/query";
        final String startTime = sharedPreferences.getString("startTime", "    NA      NA  ");
        final String endTime = sharedPreferences.getString("endTime", "    NA      NA  ");
        final int CabID = sharedPreferences.getInt("Route", 100);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url4,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        sharedPreferences.edit().putBoolean("FIRSTCABLAUNCH", false);
                        JSONArray JA = null;
                        JSONArray JA2 = null;
                        try {
                            JA = new JSONArray(response);
                            JA2 = new JSONArray();

                            for (int i = 0; i < JA.length(); i++) {
                                JSONObject JO = (JSONObject) JA.get(i);

                                if (JO.getString("Email").equals(email)) {
                                    SharedPreferences.Editor edit = sharedPreferences.edit();

                                    edit.putString("startTime", JO.getString("StartTime"));
                                    edit.putString("endTime", JO.getString("EndTime"));
                                    edit.putBoolean("Registered", true);
                                    edit.putInt("Private", 0);
                                    edit.putInt("Route", JO.getInt("RouteID"));
                                    edit.commit();
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Server Refresh Failed ...", Toast.LENGTH_SHORT).show();
            }

        });

        queue.add(stringRequest);

    }

    //UID
    private String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            return user.getUid();

        } else {
            return null;
        }

    }

    //+
    // Fetching timetable
    private void fetchData() {
        String UUID = getUID();
        pullToRefresh.setRefreshing(true);
        DocumentReference users = FirebaseFirestore.getInstance().document("users/" + UUID);
        users.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    courseList = (List<String>) documentSnapshot.get("identifiedCourses");
                    courseSegmentList = (List<String>) documentSnapshot.get("identifiedSegments");
                    slotList = (List<String>) documentSnapshot.get("identifiedSlots");

                }
                CourseName = new ArrayList<>();
                try {

                    for (int j = 0; j < courseList.size(); j++) {
                        CourseName.add("Name");
                    }

                } catch (Exception e) {

                }

                sharedPreferences.edit().putInt("seg1_begin", -1).apply();
                sharedPreferences.edit().putInt("seg2_begin", -1).apply();
                sharedPreferences.edit().putInt("seg3_begin", -1).apply();
                saveArrayList(courseList, "CourseList");

                saveArrayList(courseSegmentList, "Segment");
                sharedPreferences.edit().putBoolean("RequireReset", true).commit();
                saveArrayList(slotList, "SlotList");
                saveArrayList2(CourseName, "CourseName");
                new timetableComp().execute(getBaseContext());

                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commitAllowingStateLoss();

                Toast.makeText(getBaseContext(), "Data Synced", Toast.LENGTH_SHORT).show();
                pullToRefresh.setRefreshing(false);


            }
        });


    }

    //Saving timetable
    private void saveArrayList(List<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    private void saveArrayList2(ArrayList<String> list, String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }


}
