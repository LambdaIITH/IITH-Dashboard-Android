package com.lambda.iith.dashboard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Launch extends Activity {



    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser==null) {
            startActivity(new Intent(Launch.this, SkipLogin.class));


        }
        else{
            startActivity(new Intent(Launch.this, MainActivity.class));
        }


        finish();

    }





}
