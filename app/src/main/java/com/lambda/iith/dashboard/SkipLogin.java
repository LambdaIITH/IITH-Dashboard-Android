package com.lambda.iith.dashboard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import org.json.JSONArray;
import org.json.JSONException;

public class SkipLogin extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    private SignInButton button;
    private RequestQueue queue, queue2, queue3;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout pulltorefresh;
    private CountDownTimer mCountDownTimer ;
    public final static int RC_SIGN_IN = 0;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_skip_login);
        MainActivity.initiate();
        queue = Volley.newRequestQueue(getApplicationContext());

        mCountDownTimer = new CountDownTimer(3000, 6000) {


            @Override
            public void onTick(long millisUntilFinished) {


                findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {

                findViewById(R.id.loadingPanel).setVisibility(View.GONE);

            }




        };
        pulltorefresh = findViewById(R.id.pullToRefresh);
        pulltorefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mCountDownTimer.start();

                refresh();
            }
        });
        refresh();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("IITH Dashboard");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        fragmentManager = getSupportFragmentManager();


        fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();

        bottomNavigationView = (BottomNavigationView) findViewById(R.id.BottomNavigationSL);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toolbar toolbar = findViewById(R.id.toolbar);


                FragmentManager fragmentManager = getSupportFragmentManager();
                switch (menuItem.getItemId()) {
                    case R.id.sl_mess: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new MessMenu()).commit();
                        //toolbar.setTitle("Mess");
                        return true;
                    }

                    case R.id.sl_bus: {
                        fragmentManager.beginTransaction().replace(R.id.fragmentlayout, new FragmentBS()).commit();
                        //toolbar.setTitle("Bus");
                        return true;
                    }
                    case R.id.login: {
                        signIn();
                    }


                }
                return false;

            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void signIn() {
        FirebaseAuth.getInstance().signOut();
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("Info", "Google sign in failed", e);
                // ...
            }
        }
    }

    @Override
    public void onStart() {


        super.onStart();


    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d("Info", "firebaseAuthWithGoogle:" + acct.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Info", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            startActivity(new Intent(SkipLogin.this, MainActivity.class));
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Info", "signInWithCredential:failure", task.getException());

                        }

                        // ...
                    }
                });
    }

    private void refresh() {
        String url = "https://iith.dev/bus";

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
        String url2 = "https://iith.dev/dining";

        MainActivity.initiate();


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
                Toast.makeText(getApplicationContext() , "Server Refresh Failed ..." , Toast.LENGTH_SHORT).show();
            }

        });

        queue.add(stringRequest);
        queue.add(stringRequest2);



        queue.addRequestFinishedListener(new RequestQueue.RequestFinishedListener<Object>() {
            @Override
            public void onRequestFinished(Request<Object> request) {
                mCountDownTimer.onFinish();
                findViewById(R.id.loadingPanel).setVisibility(View.GONE);
                Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentlayout);
                FragmentTransaction ft = fragmentManager.beginTransaction();
                pulltorefresh.setRefreshing(false);
                mCountDownTimer.cancel();
                ft.detach(fragment);
                ft.attach(fragment);
                ft.commit();
                return;
            }
        });


    }
}
