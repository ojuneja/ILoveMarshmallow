package com.zappos.ojasjuneja.ilovemarshmallow.async;

import android.os.AsyncTask;
import android.util.Log;

import com.zappos.ojasjuneja.ilovemarshmallow.main.MainActivity;
import com.zappos.ojasjuneja.ilovemarshmallow.main.ProductInformationPageAdaptor;
import com.zappos.ojasjuneja.ilovemarshmallow.main.ProductListPageAdaptor;
import com.zappos.ojasjuneja.ilovemarshmallow.main.ProductListPageAdaptorNoView;
import com.zappos.ojasjuneja.ilovemarshmallow.utils.NetworkUtility;
import com.zappos.ojasjuneja.ilovemarshmallow.variables.Tag;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ojas Juneja on 9/13/2015.
 * This java file uses AsyncTask to download details from URL.
 * The URL has a link to JSON file
 * Both tasks eg.product listing page and product information page
 * uses this code to download details.
 */
public class MyAsyncTaskDownloadDetails extends AsyncTask<String,Void,String> {
    private ArrayList<HashMap<String,String>> arrayListProductDetails;
    private HashMap<String,String> hashMap;
    private String jsonString;
    private boolean errorFlag = false;
    private String price;
    @Override
    protected String doInBackground(String... urlAndTypePage) {
        //download json data and place it into string using Network Utility file
        jsonString = NetworkUtility.downloadJSON(urlAndTypePage[0]);
        arrayListProductDetails = new ArrayList<>();
        if(jsonString != null) {
            try {
                JSONObject jsonObject = new JSONObject(jsonString);
                if (urlAndTypePage[1].equals(Tag.PLP)) {
                    JSONArray jsonArray = (JSONArray) jsonObject.get(Tag.RESULT);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        hashMap = new HashMap<>();
                        JSONObject jsonObjectInner = (JSONObject) jsonArray.get(i);
                        hashMap.put(Tag.IMAGE, (String) jsonObjectInner.get(Tag.IMAGE));
                        hashMap.put(Tag.TITLE, (String) jsonObjectInner.get(Tag.TITLE));
                        hashMap.put(Tag.PRODUCT_FULL_TITLE, (String) jsonObjectInner.get(Tag.PRODUCT_FULL_TITLE));
                        hashMap.put(Tag.PRICE, (String) jsonObjectInner.get(Tag.PRICE));
                        hashMap.put(Tag.RATING, jsonObjectInner.get(Tag.RATING).toString());
                        hashMap.put(Tag.ASIN, (String) jsonObjectInner.get(Tag.ASIN));
                        //store the data into an arraylist which will be used later
                        arrayListProductDetails.add(hashMap);
                    }
                } else {
                    price = urlAndTypePage[2];
                    hashMap = new HashMap<>();
                    String description = (String) jsonObject.get(Tag.PIP_DESCRIPTION);
                    String gender = "";
                    JSONArray jsonArrayGenders = (JSONArray) jsonObject.get(Tag.GENDER);
                    for (int i = 0; i < jsonArrayGenders.length(); i++) {
                        gender = gender + "," + jsonArrayGenders.get(i);
                    }
                    if(gender.length() > 0)
                    gender = gender.substring(1,gender.length());
                   try {
                       hashMap.put(Tag.IMAGE_PIP, (String) jsonObject.get(Tag.IMAGE_PIP));
                   }
                   catch(Exception e)
                    {
                    hashMap.put(Tag.IMAGE_PIP, "");
                    }
                    hashMap.put(Tag.PRODUCT_FULL_TITLE, (String) jsonObject.get(Tag.PRODUCT_FULL_TITLE));
                    hashMap.put(Tag.PIP_DESCRIPTION, description);
                    hashMap.put(Tag.GENDER, gender);
                    arrayListProductDetails.add(hashMap);
                }
            } catch (JSONException ee) {
                Log.e(Tag.EXCEPTION_CATCH, "", ee);
            }
        }
        else
        {
            errorFlag = true;
        }
        return urlAndTypePage[1];
    }


    @Override
    protected void onPostExecute(String typePage)
    {
        NetworkUtility.onDismiss();
            if (typePage.equals(Tag.PLP)) //refreshes the adaptor
            {
                if(!errorFlag)
                {
                    MainActivity.PlaceHolderFragment.setPLPRecyclerView();
                    ProductListPageAdaptor.SingletonInstance().updateData(arrayListProductDetails);
                    ProductListPageAdaptor.SingletonInstance().notifyDataSetChanged();
                }
                else
                {
                    MainActivity.PlaceHolderFragment.setPLPRecyclerNoResultView();
                    ProductListPageAdaptorNoView.SingletonInstance().updateData(errorFlag);
                    ProductListPageAdaptorNoView.SingletonInstance().notifyDataSetChanged();
                }
                MainActivity.PlaceHolderFragment.updateData(arrayListProductDetails);
            }
            else
            {
                ProductInformationPageAdaptor.SingletonInstance().updateData(arrayListProductDetails, price);
                ProductInformationPageAdaptor.SingletonInstance().notifyDataSetChanged();
            }
    }
}
