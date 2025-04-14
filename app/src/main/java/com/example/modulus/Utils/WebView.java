package com.example.modulus.Utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import com.example.modulus.Model.ModuleModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
public class WebView {
    public static final String UTILS_TAG = "UtilsTag";

    public static InputStream getInputStream(URL url){

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        InputStream inputStream = null;

        try{
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);
            urlConnection.setUseCaches(false);
            Log.i("getInputStream", "Connecting...");
            urlConnection.connect();
            Log.i("getInputStream", "Connected");
            inputStream = urlConnection.getInputStream();
        }catch(IOException e) {
            e.printStackTrace();
            inputStream = null;
        }
        return inputStream;
    }
    public static String getJson(URL url){
        return convertStreamToString(getInputStream(url));
    }

    public static String convertStreamToString(InputStream inputStream){

        BufferedReader reader = null;
        String outString;

        StringBuffer buffer = new StringBuffer();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;

        try{
            while ((line = reader.readLine()) != null) {
        /* Since it's JSON, adding a newline isn't necessary (it won't affect
        parsing) but it does make debugging a *lot* easier if you print out the
        completed buffer for debugging. */
                buffer.append(line + "\n");
            }

        } catch( IOException e){
            e.printStackTrace();

        }
        if (buffer.length() == 0) {
            // Stream was empty.  No point in parsing.
            return null;
        }
        outString = buffer.toString();
        Log.i(UTILS_TAG,outString);
        return outString;

    }

    public static boolean isNetworkAvailable(Context context) {

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean haveNetwork = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        Log.i(UTILS_TAG, "Active Network: " + haveNetwork);
        return haveNetwork;
    }

    public static URL getURLFromDesc(ModuleModel module){
        URL url = null;
        try{
            url = new URL(module.getDescription());
        }catch(MalformedURLException e) {
            Log.i(UTILS_TAG, "malformed URL: " + url.toString());
        }

        return url;
    }

}