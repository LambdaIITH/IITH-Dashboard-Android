package Model;

import android.content.SharedPreferences;

import com.lambda.iith.dashboard.MainActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Filter {
    public String response;
    public SharedPreferences sharedPreferences;



    public void set(String response , SharedPreferences sharedPreferences) throws JSONException, ParseException {
        this.response = response;
        this.sharedPreferences = sharedPreferences;


    }
}
