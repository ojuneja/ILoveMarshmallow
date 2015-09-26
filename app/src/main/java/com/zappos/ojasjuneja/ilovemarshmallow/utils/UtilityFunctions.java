package com.zappos.ojasjuneja.ilovemarshmallow.utils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * Created by Ojas Juneja on 9/13/2015.
 * This is a utility function that helps in networking
 * uses HTTP connection to download json and images
 * provides utilities like show and dismiss progress bar
 */
public class UtilityFunctions {

    private static ProgressDialog contentLoadingProgressBar;

    // Download an image from given URL
    public static Bitmap downloadImage(String url) {
        Bitmap bitmap = null;
        InputStream stream = getHttpConnection(url);
        if(stream!=null) {
            bitmap = BitmapFactory.decodeStream(stream);
            try {
                stream.close();
            }catch (IOException e1) {
                Log.e(Tag.EXCEPTION_CATCH, "", e1);
                e1.printStackTrace();
            }
        }

        return bitmap;
    }

    // Download a Json file from given URL
    public static String downloadJSON(String url) {
        String json=null, line;

        InputStream stream = getHttpConnection(url);
        if (stream != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder out = new StringBuilder();
            try {
                while ((line = reader.readLine()) != null) {
                    out.append(line);
                }
                reader.close();
                json = out.toString();
            } catch (IOException ex) {
                Log.d("MyDebugMsg", "IOException in downloadJSON()");
                ex.printStackTrace();
            }
        }
        return json;
    }


    // Makes HttpURLConnection and returns InputStream
    public static InputStream getHttpConnection(String urlString) {
        InputStream stream = null;
        try {
            URL url = new URL(urlString);
            HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();
            httpConnection.setRequestMethod("GET");
            httpConnection.connect();
            if (httpConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                stream = httpConnection.getInputStream();
            }
        }  catch (UnknownHostException e1) {
            Log.d(Tag.EXCEPTION_CATCH, "",e1);
            e1.printStackTrace();
        } catch (Exception ex) {
            Log.d(Tag.EXCEPTION_CATCH, "",ex);
            ex.printStackTrace();
        }
        return stream;
    }

    //show progress bar
    public static void onProgressBarShow(Activity activity)
    {
        contentLoadingProgressBar = new ProgressDialog(activity);
        contentLoadingProgressBar.setCancelable(false);
        contentLoadingProgressBar.setMessage("We r almost there");
        contentLoadingProgressBar.show();
    }

    //dismiss progress bar
    public static void onDismiss()
    {
        if(contentLoadingProgressBar!=null)
        contentLoadingProgressBar.dismiss();
    }
}
