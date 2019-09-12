package com.lambda.iith.dashboard.Timetable;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;

import com.lambda.iith.dashboard.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DPload extends AsyncTask<String , Void , Void> {

    private Bitmap myBitmap;
    @Override
    protected Void doInBackground(String... strings) {
        try {

            URL url = new URL(strings[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            myBitmap = BitmapFactory.decodeStream(input);

        } catch (IOException e) {

            return null;
        }

        return null;

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        MainActivity.imageView.setImageBitmap(myBitmap);

    }
}
