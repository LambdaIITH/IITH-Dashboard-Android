package Model;

import android.content.SharedPreferences;

public class Filter {
    public String response;
    public SharedPreferences sharedPreferences;

    public void set(String response , SharedPreferences sharedPreferences){
        this.response = response;
        this.sharedPreferences = sharedPreferences;
    }
}
