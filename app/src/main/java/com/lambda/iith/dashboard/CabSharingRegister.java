package com.lambda.iith.dashboard;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;

import android.widget.ImageButton;

import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;


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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CabSharingRegister extends AppCompatActivity {
    private Button date , from, to , date2;

    private Button Book ;
    public View view;
    private ImageButton img;
    public static RequestQueue queue ;
    private  String name , email , start , end , idToken;
    private ToggleButton toggleButton;
    private CheckBox checkBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cab_sharing_register2);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkBox = findViewById(R.id.checkBox);
        date = (Button) findViewById(R.id.reg_Date);
        date2 = (Button) findViewById(R.id.Reg_Date_2);

        from = (Button) findViewById(R.id.csr_from);
        to = (Button) findViewById(R.id.to);

        Book = (Button) findViewById(R.id.cs_book);
        toggleButton = (ToggleButton) findViewById(R.id.messToggle);
        Calendar calendar = Calendar.getInstance();



        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(calendar.getTime());
        date.setText(formattedDate);
        date2.setText(formattedDate);

        SimpleDateFormat df2 = new SimpleDateFormat("HH:mm");
        String formattedDate2 = df2.format(calendar.getTime());
        from.setText(formattedDate2);
        SimpleDateFormat df3 = new SimpleDateFormat("HH:mm");
        calendar.add(Calendar.HOUR_OF_DAY , 1);
        String formattedDate3 = df3.format(calendar.getTime());

        to.setText(formattedDate3);



        queue = Volley.newRequestQueue(this);
        Book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(CabSharingRegister.this);
                SharedPreferences.Editor editor = sharedPref.edit();
                if (sharedPref.getBoolean("Registered", false)) {
                    Toast.makeText(getApplication().getBaseContext(), "Please delete previous booking before proceeding",
                            Toast.LENGTH_SHORT).show();

                } else {


                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user != null) {
                        // Name, email address, and profile photo Url
                        name = user.getDisplayName().toString();
                        email = user.getEmail().toString();



                    }
                    int route;
                    if (toggleButton.isChecked()) {
                        route = 1;
                    } else {
                        route = 0;
                    }


                    start = date.getText().toString() + "T" + from.getText().toString() + ":00.321+05:30";
                    end = date2.getText().toString() + "T" + to.getText().toString() + ":00.321+05:30";

                    editor.putBoolean("Registered", true);
                    editor.putString("startTime", start);
                    editor.putString("endTime", end);
                    editor.putInt("Route", route);
                    editor.commit();

                    if (checkBox.isChecked()) {
                        Toast.makeText(getApplication().getBaseContext(), "Booked Successfully",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        try {

                            String URL = "http://www.iith.dev/publish";
                            JSONObject jsonBody = new JSONObject();
                            //jsonBody.put("Name" , name);

                            jsonBody.put("Email", email);
                            jsonBody.put("StartTime", start);
                            jsonBody.put("EndTime", end);
                            jsonBody.put("RouteID", route);
                            System.out.println("ID1" +MainActivity.idToken);
                            jsonBody.put("token", MainActivity.idToken);
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
                                    Toast.makeText(getApplication().getBaseContext(), "Booking Unuccessfull",
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
                                        // can get more details such as response.headers
                                    }
                                    return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                }
                            };

                            queue.add(stringRequest);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    onBackPressed();
                }
            }



        });

        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerObj(from ,to, 0);




            }
        });


        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerObj(date2);
            }
        });

        to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TimePickerObj( to,to, 1);
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerObj(date);
            }
        });


    }

    public void TimePickerObj(final TextView textView , final TextView textView2 ,  final int k){
        final Calendar mcurrentTime = Calendar.getInstance();
        String Date = textView.getText().toString();
        int hour = Character.getNumericValue(Date.charAt(0))*10 +  Character.getNumericValue(Date.charAt(1));

        int minute = Character.getNumericValue(Date.charAt(3))*10 + Character.getNumericValue(Date.charAt(4));


        final TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                if(k==0){
                textView.setText(String.format("%02d:%02d", hourOfDay, minutes));
                textView2.setText(String.format("%02d:%02d", hourOfDay+1, minutes));}

                else if (k==1){
                    textView.setText(String.format("%02d:%02d", hourOfDay, minutes));
                }
            }
        }, hour, minute, false);


        timePickerDialog.show();

    }

    public void DatePickerObj(final Button textView){
        final Calendar mcurrentTime = Calendar.getInstance();
        //int day = mcurrentTime.get(Calendar.DAY_OF_MONTH);
        //final int month = mcurrentTime.get(Calendar.MONTH);
        //int year = mcurrentTime.get(Calendar.YEAR);

        String Date = textView.getText().toString();
        int day = Character.getNumericValue(Date.charAt(8))*10 +  Character.getNumericValue(Date.charAt(9));

        int year = Character.getNumericValue(Date.charAt(0))*1000 + Character.getNumericValue(Date.charAt(1))*100+ Character.getNumericValue(Date.charAt(2))*10 + Character.getNumericValue(Date.charAt(3));
        int month = Character.getNumericValue(Date.charAt(5))*10 + Character.getNumericValue(Date.charAt(6));



        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                textView.setText(String.format("%02d-%02d-%02d", year , month+1, dayOfMonth ));
            }


        }, year, month-1, day);

        datePickerDialog.show();

    }

    @Override
    public boolean onSupportNavigateUp() {

        onBackPressed();
        return true;
    }


}
