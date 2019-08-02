package com.lambda.iith.dashboard;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;

import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.GestureDetector;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener  {
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    public static String LDH;
    public static String UDH;
    public String name;
    public  String email;
    private int t;
    public String photoUrl;
    private ImageButton MasterRefresh;
    private SharedPreferences sharedPreferences;
    public static BottomNavigationView bottomNavigationView;
    private  TextView Nav_Bar_Header; //Navigation Bar Header i.e User Name
    private  TextView Nav_Bar_Email; //Navigation Bar email
    private  ImageView Nav_Bar_DP; //DP in navigation bar
    public static String idToken;
    int a;
    private List<String> courseList ;
    private List<String> courseSegmentList;
    private List<String> slotList;
    private ArrayList<String> CourseName;
    private RequestQueue queue,queue2,queue3;
    private CountDownTimer mCountDownTimer;
    private SwipeRefreshLayout pullToRefresh;
    private FragmentManager fragmentManager;
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        initiate();
        findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        fragmentManager = getSupportFragmentManager();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setOnCreateContextMenuListener(this);
        mCountDownTimer = new CountDownTimer(3500, 2000) {
            @Override
            public void onTick(long millisUntilFinished) {
                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                //findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            }
        };
        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        MasterRefresh = findViewById(R.id.MainRefresh);
        MasterRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);

                mCountDownTimer.start();
                refresh();
                //fetchData();









            }
        });
        pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setRefreshing(false);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pullToRefresh.setRefreshing(false);

                refresh();
                mCountDownTimer.start();// your code


            }
        });


        //if(sharedPreferences.getString("CourseList" , "NULL").equals("NULL")){
          //  fetchData();
        //}
        queue = Volley.newRequestQueue(MainActivity.this);
        queue2 = Volley.newRequestQueue(MainActivity.this);
        queue3= Volley.newRequestQueue(MainActivity.this);
        refresh();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName().toString();
            email = user.getEmail().toString();
            photoUrl = user.getPhotoUrl().toString();
            user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
                @Override
                public void onComplete(@NonNull Task<GetTokenResult> task) {
                    if (task.isSuccessful()) {
                        idToken = task.getResult().getToken();

                    }
                }
            });



        }

        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentlayout , new HomeScreenFragment()).commit();



        if (sharedPreferences.getBoolean("firstrun", true)) {
            fetchData();
            sharedPreferences.edit().putBoolean("firstrun", false).commit();
        }



    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Toolbar toolbar = findViewById(R.id.toolbar);



            switch (menuItem.getItemId()) {
                case R.id.nav_home: {
                    a=1;
                    MasterRefresh.setVisibility(View.VISIBLE);
                    pullToRefresh.setEnabled(true);
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new HomeScreenFragment()).commit();

                    return  true;
                }

                case R.id.nav_acads:{
                    a=2;

                    final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
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

                    t=0;
                    findViewById(R.id.TimeTableRefresh).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(t==0){
                                Toast.makeText(getApplicationContext() , "Syncing with server will delete all local data \n Press again to confirm " , Toast.LENGTH_SHORT).show();
                                t=1;
                            }
                            else if(t==1){
                                fetchData();
                            }

                        }
                    });
                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new Timetable()).commit();

                    findViewById(R.id.addcourse).setVisibility(View.VISIBLE);
                    findViewById(R.id.addcourse).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startActivity(new Intent(MainActivity.this , AddCourse.class));
                        }
                    });


                    return  true;
                }

                case R.id.nav_bus :{
                    a=3;
                    MasterRefresh.setVisibility(View.VISIBLE);
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    pullToRefresh.setEnabled(true);

                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new FragmentBS()).commit();

                    return  true;
                }

                case R.id.nav_mess:{
                    findViewById(R.id.TimeTableRefresh).setVisibility(View.GONE);
                    MasterRefresh.setVisibility(View.VISIBLE);
                    findViewById(R.id.addcourse).setVisibility(View.GONE);
                    pullToRefresh.setEnabled(true);

                    fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new MessMenu()).commit();
                    a=4;
                    return  true;
                }

            }
            return false;
        }

    };


        @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Nav_Bar_Header = (TextView) findViewById(R.id.Nav_Bar_name);
        Nav_Bar_Header.setText(name); // Setting Username recieved from google account in navigation bar

        Nav_Bar_Email = (TextView) findViewById(R.id.nav_bar_email);
        Nav_Bar_Email.setText(email); // Setting email recieved from google account in navigation bar



        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


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
                        startActivity(new Intent(MainActivity.this , SkipLogin.class));
                    }
                });
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Toolbar toolbar = findViewById(R.id.toolbar);

        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();


        if (id == R.id.nav_cab_sharing)

        {

            startActivity(new Intent(MainActivity.this , CabSharing.class));


        } else if (id == R.id.nav_lost_found) {


            Toast.makeText(getBaseContext() , "Coming Soon"  , Toast.LENGTH_SHORT).show();




        } else if (id == R.id.logout){
            signOut();




        } else if (id == R.id.nav_Settings){
        startActivity(new Intent(MainActivity.this ,Settings.class));}

        else if (id == R.id.about){
            startActivity(new Intent(MainActivity.this , About.class));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onStart() {

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser==null) {
            startActivity(new Intent(MainActivity.this, SignIn.class));
        }

        super.onStart();

    }

    //This method is used to set the default screen that will be shown.
    private void setDefaultFragment(Fragment defaultFragment){
        this.replaceFragment(new HomeScreenFragment());
    }


    //replaces the current fragment with the destination one.
    private void replaceFragment(Fragment destFragment) {
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        //beginning fragment transaction
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Replacing layout holder with the required fragment object
        fragmentTransaction.replace(R.id.drawer_layout,destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();

    }





private void refresh(){
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
                        edit.putString("FromIITH" , JA.getString(0));
                        edit.commit();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }



            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Toast.makeText(getApplicationContext() , "Server Refresh Failed ..." , Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext() , "Server Refresh Failed ..." , Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getApplicationContext() , "Server Refresh Failed ..." , Toast.LENGTH_SHORT).show();
        }

    });

    queue.add(stringRequest);
    queue2.add(stringRequest2);

    queue3.add(stringRequest3);




}
    private String getUID() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user!= null) {
            return user.getUid();

        }else {
            return null;
        }

    }
    private void fetchData() {
        String UUID = getUID();

        DocumentReference users = FirebaseFirestore.getInstance().document("users/"+UUID);
        users.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    courseList = (List<String>) documentSnapshot.get("identifiedCourses");
                    System.out.println("HFfd");
                    courseSegmentList = (List<String>) documentSnapshot.get("identifiedSegments");
                    slotList = (List<String>) documentSnapshot.get("identifiedSlots");

                }
                CourseName = new ArrayList<>();
                System.out.println("FDF" + courseList.size());

                for (int j = 0; j < courseList.size(); j++) {
                    CourseName.add("Name");
                }

              saveArrayList(courseList , "CourseList");

                saveArrayList(courseSegmentList , "Segment");

                saveArrayList(slotList , "SlotList");
                saveArrayList2(CourseName , "CourseName");




            }
        });








    }
    private void saveArrayList(List<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }
    private void saveArrayList2(ArrayList<String> list, String key){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }





























    public static void initiate(){
           UDH = "[\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Aloo Paratha, Pudina chutney and curd \\n Extras: Omelet(UDH), Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad , Mixed Dal , Crispy veg , Bhendi do pyaza ,\\n Extras:Egg burji-20/-, Paneer Kadaiwala-30/-, Chicken curry-35/- \",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras : Pav Bhaji(2 pieces)-10/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Thotakura pappu , Manchurian Dry,Tomato brinjal salan \\n Extras: Paneer pulao-60/-, Chicken biryani-60/-, Fruit bowl(150 Grams)-30/-\"\n" +
                   "  },\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Poha, Upma with cocunut chutney \\n Extras: Omelet(UDH), Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad , Dal Tadka    Aloo black channa\\tMutter masala ,\\n Extras:Egg masala-20/-, Babycorn adraki-25/-, Fish curry-45/- \",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras : Bread Pakoda(1 piece)-15/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Channa dal fry    Yam(Kanda) fry\\tKadai veg\\tGhee chapathi(sub for phulka) \\n Extras: Egg burji-20/-, Paneer butter masala-30/-, Pepper Chicken-45/-, Kheema fry-60/-, Double ka meetha-10/-\"\n" +
                   "  },\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Uttapam with Chutney and Sambar\\n Extras: Omelet(UDH), Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad , Palak dal    Dondakaya fry\\tAloo methi curry ,\\n Extras:Egg burji-20/-, Palak paneer-30/- , Butter chicken-40/- \",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Corn (200 grams)-15/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,CRajma    Cabbage poriyal\\tVeg Chatpata\\tGhee chapathi(sub for phulka) \\n Extras: Egg masala-20/-, Crispy Corn-30/-, Chicken curry-35/-, Prawns fry-50/- , Fruit bowl(150 Grams)-30/-\"\n" +
                   "  },\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Pongal and Vada with Chutney, Sambar, \\n Extras: Omelet(UDH), Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad ,Tomato dal    Palak channa dry\\tDum aloo banarasi \\n Extras:Egg burji-20/-, Chilli Paneer-40/- , Chicken 65-45/-\",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Onion Pakoda(50 gm)-10/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Methi dal    Gobi 65\\tBottle guard curry \\n Extras: Paneer pulao-60/-, Chicken biryani-60/-, Fruit bowl(150 Grams)-30/-\"\n" +
                   "  },\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Poori with aloo subzi/ Chole subzi, \\n Extras:Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad ,TMasoor dal    Bhendi peanut ,  fry\\tMix veg curry \\n Extras:Egg masala-20/-, Veg biryani-45/-, Chicken Chettinadu-40/-\",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Samosa(100 gm)-10/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Dal makhani    Raw banana fry, \\tDhai Baingan , Methi/pudina chapathi(sub for phulka) \\n Extras: Egg burji-20/-, Babycorn Manchuria-30/-, Kadai chicken-40/-, Fish curry-45/-, Gajar ka halwa-10/-\"\n" +
                   "  },\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Veg and normal idli, groundnut/Coconut Chutney \\n Extras:Omelet(UDH),Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad ,Vegetable dal    Veg jalfrezi\\tAvial \\n Extras:Egg burji-20/-, Paneer hariyali masala- 30/-,Chilli Chicken-45/-\",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Mirchi Bhaji(2 pieces)-10/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Dal panchmel    Bhendi jaipuri\\tAloo gobi masala \\n Extras: Egg masala-20/-, Palak paneer-30/-, Butter chicken-40/-, Mutton curry-50/-, Fruit bowl-30/-\"\n" +
                   "  },\n" +
                   "  {\n" +
                   "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Masala dosa/ Onion dosa, peanut / coconut chutney, sambar \\n Extras:Omelet(UDH),Boiled Egg, cornflakes, Bananas\",\n" +
                   "    \"Lunch\": \"Green salad, Roti(with and without ghee), rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad ,Malai dal    Aloo deep fry\\tKadai Veg \\n Extras:Egg burji-20/-, Mushroom biryani-50/-, Chicken hariyali masala-40/-\",\n" +
                   "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Vada Pav(1 piece) - 10/-\",\n" +
                   "    \"Dinner\": \"Green salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Dal fry    Gobi mutter Adraki\\tChole masala\\tBhatura(Substitute for phulka) \\n Extras: Paneer hariyali masala-40/-, Chicken 65-45/-, Punjabi fish-40/-, Gulab jamun-10/-\"\n" +
                   "  }\n" +
                   "]";

            LDH ="[\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Aloo Paratha, Pudina chutney and curd \\n Extras:  Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, Roti, rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad ,Dal Panchmel,Dondakaya fry(Tindora fry),Corn palak masala,\\n Extras:Egg burji-20/-, Paneer Kadaiwala-30/-, Chicken curry-35/- \",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras : Pav Bhaji(2 pieces)-10/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle,Dal fry,Gobi 65,Tomato brinjal salan \\n Extras: Paneer pulao-60/-, Chicken biryani-60/-, Fruit bowl(150 Grams)-30/-\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Pongal and Vada with Chutney ,Sambar \\n Extras:  Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, Roti, rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad , Methi dal    Veg Jalfrezi\\tRidge gourd with milk curry ,\\n Extras:Egg masala-20/-, Babycorn adraki-25/-, Fish curry-45/- \",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras : Bread Pakoda(1 piece)-15/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Gongura dal    Aloo palak dry\\tMutter do pyaza\\tMethi/pudina chapathi(sub for phulka) \\n Extras: Egg burji-20/-, Paneer butter masala-30/-, Pepper Chicken-45/-, Kheema fry-60/-, Double ka meetha-10/-\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Pessarattu(Poha alternate weeks), upma with Coconut chutney\\n Extras: Omelet(UDH, LDH), Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, Roti, rice, sambar, Curd(100ml),Papad, Chutney, vegetable salad , Dal Makhani    Bhendi peanut fry\\tmix veg curry ,\\n Extras:Egg burji-20/-, Palak paneer-30/- , Butter chicken-40/- \",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Corn (200 grams)-15/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Chana dal    Raw banana fry\\tDum Aloo curry\\tGhee chapathi(sub for phulka) \\n Extras: Egg masala-20/-, Crispy Corn-30/-, Chicken curry-35/-, Prawns fry-50/- , Fruit bowl(150 Grams)-30/-\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee, Veg and normal idli, groundnut/Coconut Chutney \\n Extras: Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, roti, rice, sambar, Curd (100ml), Papad, Chutney, vegetable salad ,Dosakaya pappu    Cluster Beans\\tMeal maker with thin gravy \\n Extras:Egg burji-20/-, Chilli Paneer-40/- , Chicken 65-45/-\",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Onion Pakoda(50 gm)-10/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,lasooni dal tadka    Crispy Veg\\tMalai Kofta \\n Extras: Paneer pulao-60/-, Chicken biryani-60/-, Fruit bowl(150 Grams)-30/-\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Uttapam with Chutney and Sambar, \\n Extras:Omelet(LDH), Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, roti, rice, sambar, Curd (100ml), Papad, Chutney, vegetable salad ,Mango dal    Yam(Kanda) fry\\tKadai veg \\n Extras:Egg masala-20/-, Veg biryani-45/-, Chicken Chettinadu-40/-\",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Samosa(100 gm)-10/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Rajma   Aloo deep fry, \\tDhai Baingan \\n Extras: Egg burji-20/-, Babycorn Manchuria-30/-, Kadai chicken-40/-, Fish curry-45/-, Gajar ka halwa-10/-\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Poori With Aloo/Chana Sabji \\n Extras:Omelet(LDH),Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, roti, rice, sambar, Curd (100ml), Papad, Chutney, vegetable salad ,Dal fry    Cauliflower dry\\tTomato drumstick curry \\n Extras:Egg burji-20/-, Paneer hariyali masala- 30/-,Chilli Chicken-45/-\",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Mirchi Bhaji(2 pieces)-10/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Palak dal    Veg Manchurian\\tAvial \\n Extras: Egg masala-20/-, Palak paneer-30/-, Butter chicken-40/-, Mutton curry-50/-, Fruit bowl-30/-\"\n" +
                    "  },\n" +
                    "  {\n" +
                    "    \"Breakfast\": \"Milk(untoned), toasted (white & wheat) bread, Jam, Butter, tea and coffee ,Masala dosa, peanut / coconut chutney, sambar \\n Extras:Omelet(LDH),Boiled Egg, cornflakes, Bananas\",\n" +
                    "    \"Lunch\": \"Salad, roti, rice, sambar, Curd (100ml), Papad, Chutney, vegetable salad ,Tomato Pappu    Bhendi dry\\tVeg chatpata \\n Extras:Egg burji-20/-, Mushroom biryani-50/-, Chicken hariyali masala-40/-\",\n" +
                    "    \"Snacks\": \"Milk, tea, coffee \\n Extras :Vada Pav(1 piece) - 10/-\",\n" +
                    "    \"Dinner\": \"Salad, Phulka(with and without ghee), rice, rasam, curd(100ml),Fryums,Pickle ,Masoor dal   Beans Poriyal\\tChole masala\\tBhatura(sub for Phulka) \\n Extras: Paneer hariyali masala-40/-, Chicken 65-45/-, Punjabi fish-40/-, Gulab jamun-10/-\"\n" +
                    "  }\n" +
                    "]";

    }



}
