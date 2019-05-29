package com.lambda.iith.dashboard;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;
import android.widget.ImageView;

import java.net.URL;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private GoogleSignInClient mGoogleSignInClient;
    private GoogleSignInOptions gso;
    public String name;
    public  String email;
    public String photoUrl;

    private  TextView Nav_Bar_Header; //Navigation Bar Header i.e User Name
    private  TextView Nav_Bar_Email; //Navigation Bar email
    private  ImageView Nav_Bar_DP; //DP in navigation bar
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setOnCreateContextMenuListener(this);

        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            name = user.getDisplayName().toString();
            email = user.getEmail().toString();
            photoUrl = user.getPhotoUrl().toString();


        }








    }

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


        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

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
                        startActivity(new Intent(MainActivity.this , SignIn.class));
                    }
                });
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        int id = item.getItemId();
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_mess_menu) {
            fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new MessMenu()).commit();

        } else if (id == R.id.nav_bus_schedule) {
            fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new FragmentBS()).commit();
        } else if (id == R.id.nav_timetable) {

        } else if (id == R.id.nav_cab_sharing) {
            fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new CabSharing()).commit();
        } else if (id == R.id.nav_lost_found) {
            fragmentManager.beginTransaction().replace(R.id.fragmentlayout , new Main2Activity()).commit();



        } else if (id == R.id.logout)
            signOut();

        {


        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser==null) {
            startActivity(new Intent(MainActivity.this, SignIn.class));
        }
    }

    //This method is used to set the default screen that will be shown.
    private void setDefaultFragment(Fragment defaultFragment){
        this.replaceFragment(defaultFragment);
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


}
